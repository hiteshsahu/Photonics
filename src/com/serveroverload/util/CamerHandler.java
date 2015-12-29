package com.serveroverload.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class CamerHandler {

	private CamerHandler() {

		getAllImages();
	}

	private static CamerHandler camerHandler;

	public static CamerHandler GetCamerHandlerInstance() {
		if (null == camerHandler) {
			camerHandler = new CamerHandler();
		}
		return camerHandler;
	}

	private static final String CAM_DIRECTORY = "CamDirectory";
	private static final int MAX_HEIGHT = 1024;
	private static final int MAX_WIDTH = 1280;

	public ArrayList<String> getAllImages() {

		ArrayList<String> imageURL = new ArrayList<>();

		File folder = new File(getImageDirectory());

		File[] listOfFiles = folder.listFiles();

		if (null != listOfFiles && listOfFiles.length != 0) {

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {

					imageURL.add(listOfFiles[i].getAbsolutePath());

					System.out.println("File " + listOfFiles[i].getName());

				} else if (listOfFiles[i].isDirectory()) {

					System.out.println("Directory " + listOfFiles[i].getName());
				}
			}
		}

		return imageURL;
	}

	// ------------------------------------------------------------TRY II
	// _------------------------------------------------------------------------

	/**
	 * This method is responsible for solving the rotation issue if exist. Also
	 * scale the images to 1024x1024 resolution
	 *
	 * @param context
	 *            The current context
	 * @param selectedImage
	 *            The Image URI
	 * @return Bitmap image results
	 * @throws IOException
	 */
	public Bitmap handleSamplingAndRotationBitmap(Context context,
			Uri selectedImage) throws IOException {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		InputStream imageStream = context.getContentResolver().openInputStream(
				selectedImage);
		BitmapFactory.decodeStream(imageStream, null, options);
		imageStream.close();

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH,
				MAX_HEIGHT);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		imageStream = context.getContentResolver().openInputStream(
				selectedImage);
		Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

		// img = rotateImageIfRequired(img, selectedImage);

		img = rotateBitmap(context, img, selectedImage);
		return img;
	}

	public Bitmap rotateBitmap(Context context, Bitmap bitmap, Uri selectedImage) {

		ExifInterface exif;
		try {
			exif = new ExifInterface(selectedImage.getPath());

			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);

			Matrix matrix = new Matrix();
			switch (orientation) {
			case ExifInterface.ORIENTATION_NORMAL:
				return bitmap;
			case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
				matrix.setScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.setRotate(180);
				break;
			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
				matrix.setRotate(180);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_TRANSPOSE:
				matrix.setRotate(90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix.setRotate(90);
				break;
			case ExifInterface.ORIENTATION_TRANSVERSE:
				matrix.setRotate(-90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				matrix.setRotate(-90);
				break;
			default:
				return bitmap;
			}
			// try {
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;

		} catch (IOException e) {

			Toast.makeText(context, "FUCK IO Exception", 200).show();
			e.printStackTrace();

			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();

			Toast.makeText(context, "FUCK OOM", 200).show();
			return null;
		}
	}

	/**
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
	 * object when decoding bitmaps using the decode* methods from
	 * {@link BitmapFactory}. This implementation calculates the closest
	 * inSampleSize that will result in the final decoded bitmap having a width
	 * and height equal to or larger than the requested width and height. This
	 * implementation does not ensure a power of 2 is returned for inSampleSize
	 * which can be faster when decoding but results in a larger bitmap which
	 * isn't as useful for caching purposes.
	 *
	 * @param options
	 *            An options object with out* params already populated (run
	 *            through a decode* method with inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	public void openGallery(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("content://media/internal/images/media"));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void convertToBase64(Bitmap bitmap) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 30, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

		// bitmap.recycle();

		encoded = null;
		byteArray = null;
	}

	public String getImageDirectory() {

		return createDirIfNotExists().getAbsolutePath();
	}

	public File createDirIfNotExists() {

		File imageDirectory = new File(
				Environment.getExternalStorageDirectory(), CAM_DIRECTORY);
		if (!imageDirectory.exists()) {

			if (!imageDirectory.mkdirs()) {

				Log.e("TravellerLog :: ", "Problem creating Image folder");
			}
		}
		return imageDirectory;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public ArrayList<String> getAllImage() {

		ArrayList<String> imageURL = new ArrayList<>();

		File folder = new File(getImageDirectory());

		File[] listOfFiles = folder.listFiles();

		if (null != listOfFiles && listOfFiles.length != 0) {

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {

					imageURL.add(listOfFiles[i].getAbsolutePath());

					System.out.println("File " + listOfFiles[i].getName());

				} else if (listOfFiles[i].isDirectory()) {

					System.out.println("Directory " + listOfFiles[i].getName());
				}
			}
		}

		return imageURL;
	}
	
	
	
	
	
	
	
	
	
	
	
	private ArrayList<File> imageURL = new ArrayList<>();

	public ArrayList<File> getImageURL() {
		return imageURL;
	}

	public void setImageURL(ArrayList<File> imageURL) {
		this.imageURL = imageURL;
	}
	
	
	
//	
//	
//	
//	
//	
//	
//	private CamerHandler() {
//
//		getAllImages();
//	}
//
//	private static CamerHandler camerHandler;
//
//	public static CamerHandler GetCamerHandlerInstance() {
//		if (null == camerHandler) {
//			camerHandler = new CamerHandler();
//		}
//		return camerHandler;
//	}
//
//	private static final String CAM_DIRECTORY = "CamDirectory";
//	private static final int MAX_HEIGHT = 1024;
//	private static final int MAX_WIDTH = 1280;
//
//	private ArrayList<File> imageURL = new ArrayList<>();
//
//	public ArrayList<File> getImageURL() {
//		return imageURL;
//	}
//
//	public void setImageURL(ArrayList<File> imageURL) {
//		this.imageURL = imageURL;
//	}
//
//	public void getAllImages() {
//
//		imageURL.clear();
//
//		File folder = new File(getImageDirectory());
//		
//		File[] listOfFiles = folder.listFiles();
//
//		if (null != listOfFiles && listOfFiles.length != 0) {
//
//			for (int i = 0; i < listOfFiles.length; i++) {
//				if (listOfFiles[i].isFile()) {
//
//					imageURL.add(listOfFiles[i]);
//
//					System.out.println("File " + listOfFiles[i].getName());
//					
//				} else if (listOfFiles[i].isDirectory()) {
//					
//					System.out.println("Directory " + listOfFiles[i].getName());
//				}
//			}
//		}
//	}
//	
//	public ArrayList<String> getAllImage() {
//
//		ArrayList<String> imageURL = new ArrayList<>();
//
//		File folder = new File(getImageDirectory());
//
//		File[] listOfFiles = folder.listFiles();
//
//		if (null != listOfFiles && listOfFiles.length != 0) {
//
//			for (int i = 0; i < listOfFiles.length; i++) {
//				if (listOfFiles[i].isFile()) {
//
//					imageURL.add(listOfFiles[i].getAbsolutePath());
//
//					System.out.println("File " + listOfFiles[i].getName());
//
//				} else if (listOfFiles[i].isDirectory()) {
//
//					System.out.println("Directory " + listOfFiles[i].getName());
//				}
//			}
//		}
//
//		return imageURL;
//	}
//
//
//	// ------------------------------------------------------------TRY II
//	// _------------------------------------------------------------------------
//
//	/**
//	 * This method is responsible for solving the rotation issue if exist. Also
//	 * scale the images to 1024x1024 resolution
//	 *
//	 * @param context
//	 *            The current context
//	 * @param selectedImage
//	 *            The Image URI
//	 * @return Bitmap image results
//	 * @throws IOException
//	 */
//	public Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
//
//		// First decode with inJustDecodeBounds=true to check dimensions
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//
//		InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
//		BitmapFactory.decodeStream(imageStream, null, options);
//		imageStream.close();
//
//		// Calculate inSampleSize
//		options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
//
//		// Decode bitmap with inSampleSize set
//		options.inJustDecodeBounds = false;
//		imageStream = context.getContentResolver().openInputStream(selectedImage);
//		Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);
//
//		// img = rotateImageIfRequired(img, selectedImage);
//
//		img = rotateBitmap(context, img, selectedImage);
//		return img;
//	}
//
//	public Bitmap rotateBitmap(Context context, Bitmap bitmap, Uri selectedImage) {
//
//		ExifInterface exif;
//		try {
//			exif = new ExifInterface(selectedImage.getPath());
//
//			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//
//			Matrix matrix = new Matrix();
//			switch (orientation) {
//			case ExifInterface.ORIENTATION_NORMAL:
//				return bitmap;
//			case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//				matrix.setScale(-1, 1);
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_180:
//				matrix.setRotate(180);
//				break;
//			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//				matrix.setRotate(180);
//				matrix.postScale(-1, 1);
//				break;
//			case ExifInterface.ORIENTATION_TRANSPOSE:
//				matrix.setRotate(90);
//				matrix.postScale(-1, 1);
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_90:
//				matrix.setRotate(90);
//				break;
//			case ExifInterface.ORIENTATION_TRANSVERSE:
//				matrix.setRotate(-90);
//				matrix.postScale(-1, 1);
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_270:
//				matrix.setRotate(-90);
//				break;
//			default:
//				return bitmap;
//			}
//			// try {
//			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//			bitmap.recycle();
//			return bmRotated;
//
//		} catch (IOException e) {
//
//			Toast.makeText(context, "FUCK IO Exception", 200).show();
//			e.printStackTrace();
//
//			return null;
//		} catch (OutOfMemoryError e) {
//			e.printStackTrace();
//
//			Toast.makeText(context, "FUCK OOM", 200).show();
//			return null;
//		}
//	}
//
//	/**
//	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
//	 * object when decoding bitmaps using the decode* methods from
//	 * {@link BitmapFactory}. This implementation calculates the closest
//	 * inSampleSize that will result in the final decoded bitmap having a width
//	 * and height equal to or larger than the requested width and height. This
//	 * implementation does not ensure a power of 2 is returned for inSampleSize
//	 * which can be faster when decoding but results in a larger bitmap which
//	 * isn't as useful for caching purposes.
//	 *
//	 * @param options
//	 *            An options object with out* params already populated (run
//	 *            through a decode* method with inJustDecodeBounds==true
//	 * @param reqWidth
//	 *            The requested width of the resulting bitmap
//	 * @param reqHeight
//	 *            The requested height of the resulting bitmap
//	 * @return The value to be used for inSampleSize
//	 */
//	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//		// Raw height and width of image
//		final int height = options.outHeight;
//		final int width = options.outWidth;
//		int inSampleSize = 1;
//
//		if (height > reqHeight || width > reqWidth) {
//
//			// Calculate ratios of height and width to requested height and
//			// width
//			final int heightRatio = Math.round((float) height / (float) reqHeight);
//			final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//			// Choose the smallest ratio as inSampleSize value, this will
//			// guarantee a final image
//			// with both dimensions larger than or equal to the requested height
//			// and width.
//			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//
//			// This offers some additional logic in case the image has a strange
//			// aspect ratio. For example, a panorama may have a much larger
//			// width than height. In these cases the total pixels might still
//			// end up being too large to fit comfortably in memory, so we should
//			// be more aggressive with sample down the image (=larger
//			// inSampleSize).
//
//			final float totalPixels = width * height;
//
//			// Anything more than 2x the requested pixels we'll sample down
//			// further
//			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
//
//			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
//				inSampleSize++;
//			}
//		}
//		return inSampleSize;
//	}
//
//	public void openGallery(Context context) {
//		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(intent);
//	}
//
//	private void convertToBase64(Bitmap bitmap) {
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		bitmap.compress(CompressFormat.JPEG, 30, byteArrayOutputStream);
//		byte[] byteArray = byteArrayOutputStream.toByteArray();
//		String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
//
//		// bitmap.recycle();
//
//		encoded = null;
//		byteArray = null;
//	}
//
//	public String getImageDirectory() {
//
//		return createDirIfNotExists().getAbsolutePath();
//	}
//
//	public File createDirIfNotExists() {
//
//		File imageDirectory = new File(Environment.getExternalStorageDirectory(), CAM_DIRECTORY);
//		if (!imageDirectory.exists()) {
//
//			if (!imageDirectory.mkdirs()) {
//
//				Log.e("TravellerLog :: ", "Problem creating Image folder");
//			}
//		}
//		return imageDirectory;
//	}
}
