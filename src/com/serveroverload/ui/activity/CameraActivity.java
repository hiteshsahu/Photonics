package com.serveroverload.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.serveroverload.yago.R;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CameraActivity extends Activity implements SensorEventListener {

	private static final String TAG = "NativeCameraLauncher";

	private SurfaceView preview;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;
	private int led = 0;
	private int cam = 0;
	private boolean pressed = false;
	private int degrees = 0;
	private boolean isFlash = false;
	private boolean isFrontCamera = false;
	SensorManager sm;
	WindowManager mWindowManager;

	public int mOrientationDeg;
	private static final int _DATA_X = 0;
	private static final int _DATA_Y = 1;
	private static final int _DATA_Z = 2;
	int ORIENTATION_UNKNOWN = -1;

	private int screenWidth;
	private int screenHeight;

	private float viewfinderHalfPx;
	private ImageView takenPic;

	private Button flipCamera;

	private Button flashButton;

	private Button captureButton;
	private static final int FOCUS_AREA_SIZE = 300;
	private ImageView viewfinder;

	private RelativeLayout focusButton;

	private int maxZoomLevel;

	protected int currentZoomLevel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_camera);
		// setContentView(getResources().getIdentifier("nativecameraplugin",
		// "layout", getPackageName()));

		preview = (SurfaceView) findViewById(R.id.preview);
		flipCamera = (Button) findViewById(R.id.flipCamera);
		flashButton = (Button) findViewById(R.id.flashButton);
		captureButton = (Button) findViewById(R.id.captureButton);
		viewfinder = (ImageView) findViewById(R.id.viewfinder);
		focusButton = (RelativeLayout) findViewById(R.id.viewfinderArea);
		// final int imgFlashNo =
		// getResources().getIdentifier("@drawable/btn_flash_no", null,
		// getPackageName());
		// final int imgFlashAuto =
		// getResources().getIdentifier("@drawable/btn_flash_auto", null,
		// getPackageName());
		// final int imgFlashOn =
		// getResources().getIdentifier("@drawable/btn_flash_on", null,
		// getPackageName());
		viewfinderHalfPx = pxFromDp(72) / 2;

		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		}

		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			flashButton.setVisibility(View.VISIBLE);
			isFlash = true;
		} else {
			flashButton.setVisibility(View.INVISIBLE);
			isFlash = false;
		}

		if (Camera.getNumberOfCameras() > 1) {
			flipCamera.setVisibility(View.VISIBLE);
			isFrontCamera = true;
		} else {
			flipCamera.setVisibility(View.INVISIBLE);
			isFrontCamera = false;
		}

		Display display = getWindowManager().getDefaultDisplay();
		// Necessary to use deprecated methods for Android 2.x support
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();

		focusButton.setOnTouchListener(new View.OnTouchListener() {
			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float x = 0;
				float y = 0;
				Rect focusRect;
				y = (((event.getX() * 2000) / screenWidth) - 1000) * -1;
				x = (((event.getY() * 2000) / screenHeight) - 1000);

				if ((int) x - 100 > -1000 && (int) x + 100 < 1000 && (int) y - 100 > -1000 && (int) y + 100 < 1000) {
					focusRect = new Rect((int) x - 100, (int) y - 100, (int) x + 100, (int) y + 100);
				} else {
					focusRect = new Rect(-100, -100, 100, 100);
				}

				if (camera == null)
					return true;

				Parameters parameters = camera.getParameters();

				if (parameters.getMaxNumFocusAreas() > 0) {

					if (event.getX() - viewfinderHalfPx < 0) {
						viewfinder.setX(0);
					} else if (event.getX() + viewfinderHalfPx > screenWidth) {
						viewfinder.setX(screenWidth - viewfinderHalfPx * 2);
					} else {
						viewfinder.setX(event.getX() - viewfinderHalfPx);
					}

					if (event.getY() - viewfinderHalfPx < 0) {
						viewfinder.setY(0);
					} else if (event.getY() + viewfinderHalfPx > screenHeight - pxFromDp(125)) {
						viewfinder.setY((screenHeight - pxFromDp(125)) - viewfinderHalfPx * 2);
					} else {
						viewfinder.setY(event.getY() - viewfinderHalfPx);
					}

					camera.cancelAutoFocus();

					parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

					List<Camera.Area> focusArea = new ArrayList<Camera.Area>();
					focusArea.add(new Camera.Area(focusRect, 750));
					parameters.setFocusAreas(focusArea);
					if (parameters.getMaxNumMeteringAreas() > 0) {
						parameters.setMeteringAreas(focusArea);
					}

					camera.setParameters(parameters);
				}
				return true;
			}
		});

		if (isFrontCamera) {
			flipCamera.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (cam == 0) {
						cam = 1;
						led = 0;
						viewfinder.setVisibility(View.INVISIBLE);
						if (isFlash)
							flashButton.setVisibility(View.INVISIBLE);
						if (isFlash)
							flashButton.setBackgroundResource(R.drawable.ic_launcher);
					} else {
						cam = 0;
						led = 0;
						viewfinder.setVisibility(View.VISIBLE);
						viewfinder.setX(screenWidth / 2 - viewfinderHalfPx);
						viewfinder.setY(screenHeight / 2 - viewfinderHalfPx * 3);
						if (isFlash)
							flashButton.setVisibility(View.VISIBLE);
						if (isFlash)
							flashButton.setBackgroundResource(R.drawable.ic_launcher);
					}
					cameraConfigured = false;
					restartPreview(cam);
				}
			});
		}

		if (isFlash) {
			flashButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Parameters p = camera.getParameters();
					if (led == 0) {
						p.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
						flashButton.setBackgroundResource(R.drawable.ic_launcher);
						led = 1;
					} else if (led == 1) {
						p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
						flashButton.setBackgroundResource(R.drawable.ic_launcher);
						led = 2;
					} else if (led == 2) {
						p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						flashButton.setBackgroundResource(R.drawable.ic_launcher);
						led = 0;
					}
					camera.setParameters(p);
					camera.startPreview();
				}
			});
		}

		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (pressed || camera == null)
					return;

				Parameters p = camera.getParameters();

				p.setRotation(degrees);

				if (p.getMaxNumFocusAreas() > 0) {

					camera.cancelAutoFocus();

					p.setFocusMode(Parameters.FOCUS_MODE_AUTO);
				}

				camera.setParameters(p);

				pressed = true;
				// Auto-focus first, catching rare autofocus error
				try {
					camera.autoFocus(new AutoFocusCallback() {
						public void onAutoFocus(boolean success, Camera camera) {
							// Catch take picture error
							try {
								camera.takePicture(null, null, mPicture);
							} catch (RuntimeException ex) {
								// takePicture crash. Ignore.
								Toast.makeText(getApplicationContext(), "Error taking picture", Toast.LENGTH_SHORT)
										.show();
								Log.e(TAG, "Auto-focus crash");
							}
						}
					});
				} catch (RuntimeException ex) {
					// Auto focus crash. Ignore.
					Toast.makeText(getApplicationContext(), "Error focusing", Toast.LENGTH_SHORT).show();
					Log.e(TAG, "Auto-focus crash");
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {

			ZoomOut();
			return true;

		} else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
			ZoomIn();
			return true;

		} else
			return super.onKeyDown(keyCode, event);
	}

	void ZoomIn() {
		Parameters p = camera.getParameters();

		if (p.isZoomSupported() && p.isSmoothZoomSupported()) {

			// most phones
			maxZoomLevel = p.getMaxZoom();

			if (currentZoomLevel < maxZoomLevel) {
				currentZoomLevel++;
				camera.startSmoothZoom(currentZoomLevel);

			}
		} else if (p.isZoomSupported() && !p.isSmoothZoomSupported()) {
			// stupid HTC phones
			maxZoomLevel = p.getMaxZoom();

			if (currentZoomLevel < maxZoomLevel) {
				currentZoomLevel++;
				p.setZoom(currentZoomLevel);
				camera.setParameters(p);

			}
		}

	}

	void ZoomOut() {
		Parameters p = camera.getParameters();

		if (p.isZoomSupported() && p.isSmoothZoomSupported()) {

			// most phones
			maxZoomLevel = p.getMaxZoom();

			if (currentZoomLevel > 0) {
				currentZoomLevel--;
				camera.startSmoothZoom(currentZoomLevel);
			}
		} else if (p.isZoomSupported() && !p.isSmoothZoomSupported()) {
			// stupid HTC phones
			maxZoomLevel = p.getMaxZoom();

			if (currentZoomLevel > 0) {
				currentZoomLevel--;
				p.setZoom(currentZoomLevel);
				camera.setParameters(p);
			}
		}

	}

	/* Here is probably where we should add the button */
	private PictureCallback mPicture = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {

			// if (null != data && null!=getIntent().getExtras()) {

			// Uri fileUri = (Uri)
			// getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);
			Calendar cal = Calendar.getInstance();

			File file = new File(getImageDirectory(), (cal.getTimeInMillis() + ".png"));

			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Failed to make file", 500).show();
				}
			} else {
				file.delete();
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Failed to make file", 500).show();
				}
			}

			// File imageDirectory = new
			// File(Environment.getExternalStorageDirectory(), "test"+".png");
			// imageDirectory.ce
			// File pictureFile = new File(fileUri.getPath());
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
			pressed = false;

			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

			setResult(RESULT_OK);
			finish();
			// }
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		previewHolder = preview.getHolder();
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		previewHolder.addCallback(surfaceCallback);
		if (Camera.getNumberOfCameras() >= 1) {
			camera = Camera.open(cam);
		}

		// Initialize preview if surface still exists
		if (preview.getHeight() > 0) {
			initPreview(preview.getHeight());
			startPreview();
		}
	}

	private float pxFromDp(float dp) {
		return dp * CameraActivity.this.getResources().getDisplayMetrics().density;
	}

	void restartPreview(int isFront) {
		if (inPreview) {
			camera.stopPreview();
		}
		camera.release();
		camera = Camera.open(isFront);
		initPreview(preview.getHeight());
		startPreview();
	}

	@Override
	public void onPause() {
		if (inPreview) {
			camera.stopPreview();
		}
		camera.release();
		camera = null;
		inPreview = false;
		super.onPause();
	}

	private Camera.Size getBestPreviewSize(int height, Camera.Parameters parameters) {

		final double ASPECT_TOLERANCE = 0.1;
		Camera.Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		// Try to find an size match aspect ratio and size
		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - height) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - height) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - height);
			}
		}
		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
				if (Math.abs(size.height - height) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - height);
				}
			}
		}
		return optimalSize;
	}

	private void initPreview(int height) {
		if (camera != null && previewHolder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(height, parameters);
				Camera.Size pictureSize = getSmallestPictureSize(parameters);
				if (size != null && pictureSize != null) {
					parameters.setPreviewSize(size.width, size.height);
					parameters.setPictureSize(pictureSize.width, pictureSize.height);

					parameters.setPictureFormat(ImageFormat.JPEG);
					// For Android 2.3.4 quirk
					if (parameters.getSupportedFocusModes() != null) {
						if (parameters.getSupportedFocusModes()
								.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
						} else if (parameters.getSupportedFocusModes()
								.contains(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO)) {
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
						}
					}
					if (parameters.getSupportedSceneModes() != null) {
						if (parameters.getSupportedSceneModes().contains(Camera.Parameters.SCENE_MODE_AUTO)) {
							parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
						}
					}
					if (parameters.getSupportedWhiteBalance() != null) {
						if (parameters.getSupportedWhiteBalance().contains(Camera.Parameters.WHITE_BALANCE_AUTO)) {
							parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
						}
					}
					cameraConfigured = true;
					camera.setParameters(parameters);
				}
			}
		}
	}

	private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
		Camera.Size result = null;
		for (Camera.Size size : parameters.getSupportedPictureSizes()) {
			if (result == null) {
				result = size;
			} else {
				int resultArea = result.width * result.height;
				int newArea = size.width * size.height;

				if (newArea > resultArea) {
					result = size;
				}
			}
		}
		return (result);
	}

	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.setDisplayOrientation(90);
			camera.startPreview();
			inPreview = true;
		}
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			if (camera != null) {
				camera.setDisplayOrientation(90);
			}
			initPreview(preview.getHeight());
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera != null) {
				camera.stopPreview();
				camera.release();
				camera = null;
			}
		}
	};

	@Override
	protected void onDestroy() {
		// Stop listening to sensor
		sm.unregisterListener(this);
		super.onDestroy();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			int orientation = ORIENTATION_UNKNOWN;
			float X = -values[_DATA_X];
			float Y = -values[_DATA_Y];
			float Z = -values[_DATA_Z];
			float magnitude = X * X + Y * Y;
			// Don't trust the angle if the magnitude is small compared to the y
			// value
			if (magnitude * 4 >= Z * Z) {
				float OneEightyOverPi = 57.29577957855f;
				float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
				orientation = 90 - Math.round(angle);
				// normalize to 0 - 359 range
				while (orientation >= 360) {
					orientation -= 360;
				}
				while (orientation < 0) {
					orientation += 360;
				}
			}
			// ^^ thanks to google for that code
			// now we must figure out which orientation based on the degrees
			if (orientation != mOrientationDeg) {
				mOrientationDeg = orientation;
				// figure out actual orientation
				if (orientation == -1) {// basically flat
					if (cam == 1) {
						degrees = 270;
					} else {
						degrees = 90;
					}
				} else if (orientation <= 45 || orientation > 315) {// round to
																	// 0
					if (cam == 1) {
						degrees = 270;
					} else {
						degrees = 90;
					}
				} else if (orientation > 45 && orientation <= 135) {// round to
																	// 90
					degrees = 180;
					// RotateAnimation a = new RotateAnimation(0, 90, 34, 34);
				} else if (orientation > 135 && orientation <= 225) {// round to
																		// 180
					if (cam == 1) {
						degrees = 90;
					} else {
						degrees = 270;
					}
				} else if (orientation > 225 && orientation <= 315) {// round to
																		// 270
					degrees = 0;
				}
			}

		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	private void focusOnTouch(MotionEvent event) {
		if (camera != null) {

			Camera.Parameters parameters = camera.getParameters();
			if (parameters.getMaxNumMeteringAreas() > 0) {
				Log.i(TAG, "fancy !");
				Rect rect = calculateFocusArea(event.getX(), event.getY());

				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
				meteringAreas.add(new Camera.Area(rect, 800));
				parameters.setFocusAreas(meteringAreas);

				camera.setParameters(parameters);
				camera.autoFocus(mAutoFocusTakePictureCallback);
			} else {
				camera.autoFocus(mAutoFocusTakePictureCallback);
			}
		}
	}

	private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				// do something...
				Log.i("tap_to_focus", "success!");
			} else {
				// do something...
				Log.i("tap_to_focus", "fail!");
			}
		}
	};

	private Rect calculateFocusArea(float x, float y) {
		int left = clamp(Float.valueOf((x / focusButton.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
		int top = clamp(Float.valueOf((y / focusButton.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);

		return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
	}

	private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
		int result;
		if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
			if (touchCoordinateInCameraReper > 0) {
				result = 1000 - focusAreaSize / 2;
			} else {
				result = -1000 + focusAreaSize / 2;
			}
		} else {
			result = touchCoordinateInCameraReper - focusAreaSize / 2;
		}
		return result;
	}

	public String getImageDirectory() {

		return createDirIfNotExists().getAbsolutePath();
	}

	private static final String CAM_DIRECTORY = "CamDirectory";

	public File createDirIfNotExists() {

		File imageDirectory = new File(Environment.getExternalStorageDirectory(), CAM_DIRECTORY);
		if (!imageDirectory.exists()) {

			if (!imageDirectory.mkdirs()) {

				Log.e("TravellerLog :: ", "Problem creating Image folder");
			}
		}
		return imageDirectory;
	}

}
