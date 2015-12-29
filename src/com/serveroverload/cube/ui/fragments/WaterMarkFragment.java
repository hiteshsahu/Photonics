package com.serveroverload.cube.ui.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.serveroverload.yago.R;

public class WaterMarkFragment extends Fragment {
	private static final String IMAGE_URL = "IMAGE_URL";
	private static final int DEFAULT_FONT_SIZE = 25;
	private Bitmap originalBitmap, image;
	private ImageView imageView;
	private EditText waterMarkText;
	private Paint paint;
	private EditText waterMarkSize;
	private Button clearAll;
	private int WATER_MARK_SIZE = DEFAULT_FONT_SIZE;

	private String imageURl;

	public static WaterMarkFragment newInstance(String ImageURL) {
		Bundle bundle = new Bundle();
		bundle.putString(IMAGE_URL, ImageURL);

		WaterMarkFragment waterMarkActivity = new WaterMarkFragment();
		waterMarkActivity.setArguments(bundle);

		return waterMarkActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.activity_watermark,
				container, false);
		imageURl = getArguments().getString(IMAGE_URL);

		// image view
		imageView = (ImageView) rootview.findViewById(R.id.watermark_preview);
		waterMarkText = (EditText) rootview.findViewById(R.id.watermark_text);
		waterMarkSize = (EditText) rootview.findViewById(R.id.watermark_size);
		clearAll = (Button) rootview.findViewById(R.id.clear_all);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);

		// dimentions x,y of device to create a scaled bitmap having similar
		// dimentions to screen size

		waterMarkSize.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (!s.toString().isEmpty()) {
					WATER_MARK_SIZE = Integer.parseInt(s.toString());
					paint.setTextSize(WATER_MARK_SIZE);
				} else {
					WATER_MARK_SIZE = DEFAULT_FONT_SIZE;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

				// TODO Auto-generated method stub
			}
		});
		//
		 int height1 = displaymetrics.heightPixels;
		 int width1 = displaymetrics.widthPixels;

//		int height1 = imageView.getHeight();
//		int width1 = imageView.getWidth();

		// paint object to define paint properties
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLUE);
		paint.setAlpha(1);
		paint.setTextSize(25);

		if (null == imageURl) {
			// loading bitmap from drawable
			originalBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.camera);
		} else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			originalBitmap = BitmapFactory.decodeFile(imageURl, options);

			//imageView.setImageBitmap(originalBitmap);

		}

		// scaling of bitmap
		originalBitmap = Bitmap.createScaledBitmap(originalBitmap, width1,
				height1, false);

		// creating anoter copy of bitmap to be used for editing
		image = originalBitmap.copy(Bitmap.Config.RGB_565, true);

		clearAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// loading original bitmap again (undoing all editing)
				image = originalBitmap.copy(Bitmap.Config.RGB_565, true);
				imageView.setImageBitmap(image);
			}
		});

		imageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				String user_text = waterMarkText.getText().toString();
				// gettin x,y cordinates on screen touch
				float scr_x = arg1.getRawX();
				float scr_y = arg1.getRawY();
				// funtion called to perform drawing
				createWaterMark(scr_x, scr_y, user_text);
				return true;
			}
		});
		return rootview;
	}

	private void saveImage(Bitmap img) {
		String RootDir = Environment.getExternalStorageDirectory()
				+ File.separator + "txt_imgs";
		File myDir = new File(RootDir);
		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "Image-" + n + ".jpg";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			img.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Bitmap createWaterMark(float scrX, float scrY, String watermarkText) {
		// canvas object with bitmap image as constructor
		Canvas canvas = new Canvas(image);
		int viewTop = getActivity().getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// removing title bar hight
		scrY = scrY - viewTop;
		// fuction to draw text on image. you can try more drawing funtions like
		// oval,point,rect,etc…
		canvas.drawText("" + watermarkText, scrX, scrY, paint);
		imageView.setImageBitmap(image);
		return image;
	}
}