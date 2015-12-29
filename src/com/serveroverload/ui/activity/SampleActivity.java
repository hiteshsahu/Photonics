package com.serveroverload.ui.activity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.serveroverload.cube.adapter.DrawerListArrayAdapter;
import com.serveroverload.cube.ui.fragments.WaterMarkFragment;
import com.serveroverload.customview.ActionBarDrawerToggle;
import com.serveroverload.customview.DrawerArrowDrawable;
import com.serveroverload.util.CamerHandler;
import com.serveroverload.util.Constants;
import com.serveroverload.util.PreferenceHelper;
import com.serveroverload.util.UtilFunctions;
import com.serveroverload.util.UtilFunctions.AnimationType;
import com.serveroverload.yago.R;
import com.squareup.picasso.Picasso;

@SuppressLint("ResourceAsColor")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SampleActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerArrowDrawable drawerArrow;
	private LinearLayout mDrawerLinear;

	private static final String TAG = "HomeActivity";
	static Uri capturedImageUri = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		mDrawerList = (ListView) findViewById(R.id.left_drawer_child);

		if (UtilFunctions.isPortrait(getApplicationContext())) {

			ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			ab.setHomeButtonEnabled(true);

			mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

			drawerArrow = new DrawerArrowDrawable(this) {
				@Override
				public boolean isLayoutRtl() {
					return false;
				}
			};
			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					drawerArrow, R.string.drawer_open, R.string.drawer_close) {

				public void onDrawerClosed(View view) {
					super.onDrawerClosed(view);
					invalidateOptionsMenu();
				}

				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
					invalidateOptionsMenu();
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);
			mDrawerToggle.syncState();

			String imageUrl = PreferenceHelper.getPrefernceHelperInstace(
					getApplicationContext()).getString(
					PreferenceHelper.IMAGE_URL, null);

			if (null != imageUrl) {
				Picasso.with(getApplicationContext()).load(Uri.parse(imageUrl))
						.centerCrop().fit()
						.into((ImageView) findViewById(R.id.profile_pic));
			}

		}

		UtilFunctions.switchContent(R.id.frag_root,
				UtilFunctions.HOME_FRAGMENT_TAG, SampleActivity.this,
				AnimationType.SLIDE_LEFT);

		mDrawerList.setAdapter(new DrawerListArrayAdapter(
				getApplicationContext(), getResources().getStringArray(
						R.array.drawer_list_array)));

		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						mDrawerList.setItemChecked(position, true);

						switch (position) {
						case 0:

							UtilFunctions.switchContent(R.id.frag_root,
									UtilFunctions.HOME_FRAGMENT_TAG,
									SampleActivity.this,
									AnimationType.SLIDE_LEFT);
							break;
						case 1:

//							UtilFunctions.switchContent(R.id.frag_root,
//									UtilFunctions.ESTIMATE_TAG,
//									SampleActivity.this,
//									AnimationType.SLIDE_LEFT);

							startActivity(new Intent(getApplicationContext(),
									CameraActivity.class));
							break;
						case 2:

							UtilFunctions.switchContent(R.id.frag_root,
									UtilFunctions.SELECT_IMAGE_TAG,
									SampleActivity.this,
									AnimationType.SLIDE_LEFT);
							break;
						case 3:

							// UtilFunctions.switchContent(R.id.frag_root,
							// UtilFunctions.OFFERS_TAG,
							// SampleActivity.this,
							// AnimationType.SLIDE_LEFT);

							UtilFunctions.switchFragmentWithAnimation(
									R.id.frag_root,
									WaterMarkFragment.newInstance(CamerHandler
											.GetCamerHandlerInstance()
											.getAllImage().get(0).toString()),
									SampleActivity.this, "WaterMarkFragment",
									AnimationType.SLIDE_RIGHT);
							break;
						case 4:

							UtilFunctions.switchContent(R.id.frag_root,
									UtilFunctions.RIDE_HISTORY_TAG,
									SampleActivity.this,
									AnimationType.SLIDE_LEFT);
							break;
						case 5:

							UtilFunctions.switchContent(R.id.frag_root,
									UtilFunctions.SETTINGS_TAG,
									SampleActivity.this,
									AnimationType.SLIDE_LEFT);
							break;
						case 6:

							UtilFunctions.switchContent(R.id.frag_root,
									UtilFunctions.ABOUT_APP_TAG,
									SampleActivity.this,
									AnimationType.SLIDE_LEFT);
							break;

						}

						if (UtilFunctions.isPortrait(getApplicationContext())) {

							if (mDrawerLayout.isDrawerOpen(mDrawerLinear)) {
								mDrawerLayout.closeDrawer(mDrawerLinear);
							} else {
								mDrawerLayout.openDrawer(mDrawerLinear);
							}
						}

						findViewById(R.id.profile_detail).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {

										if (UtilFunctions
												.isPortrait(getApplicationContext())) {

											if (mDrawerLayout
													.isDrawerOpen(mDrawerLinear)) {
												mDrawerLayout
														.closeDrawer(mDrawerLinear);
											} else {
												mDrawerLayout
														.openDrawer(mDrawerLinear);
											}
										}

										UtilFunctions.switchContent(
												R.id.frag_root,
												UtilFunctions.PROFILE_TAG,
												SampleActivity.this,
												AnimationType.SLIDE_LEFT);

									}
								});
					}
				});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (UtilFunctions.isPortrait(getApplicationContext())) {
			if (item.getItemId() == android.R.id.home) {
				if (mDrawerLayout.isDrawerOpen(mDrawerLinear)) {
					mDrawerLayout.closeDrawer(mDrawerLinear);
				} else {
					mDrawerLayout.openDrawer(mDrawerLinear);
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		if (UtilFunctions.isPortrait(getApplicationContext())) {
			mDrawerToggle.syncState();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (UtilFunctions.isPortrait(getApplicationContext())) {
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

	}

	// This method handles the result from recognition activity
	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CAMERA) {

			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					capturedImageUri));

			Fragment fragment = getSupportFragmentManager().findFragmentByTag(
					UtilFunctions.SELECT_IMAGE_TAG);
			fragment.onActivityResult(Constants.REQUEST_CAMERA, resultCode,
					data);

			// Bitmap bitmap = null;
			// try {
			//
			// bitmap = CamerHandler.GetCamerHandlerInstance()
			// .handleSamplingAndRotationBitmap(
			// getApplicationContext(), capturedImageUri);
			//
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// if (null != bitmap) {
			//
			// // CamerHandler.GetCamerHandlerInstance().getAllImages();
			// //
			// // mPageAdapter.notifyDataSetChanged();
			//
			// // setUpStack();
			//
			// //
			// Picasso.with(getApplicationContext()).load(capturedImageUri).fit().centerCrop().into(previewLayout);
			//
			// // previewLayout.setBackground(new
			// // BitmapDrawable(getResources(), bitmap));
			//
			// } else {
			// Log.e(TAG, "FAILED TO TAKE IMAGE");
			// }

			// file.delete();

		} else if (resultCode == RESULT_CANCELED
				&& requestCode == Constants.REQUEST_GALLERY) {

			Fragment fragment = getSupportFragmentManager().findFragmentByTag(
					UtilFunctions.SELECT_IMAGE_TAG);
			fragment.onActivityResult(Constants.REQUEST_GALLERY, resultCode,
					data);

			// if (new File(capturedImageUri.toString()).exists()) {
			// new File(capturedImageUri.toString()).delete();
			// }

		}

	}

	private void dispatchTakePictureIntent() {

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

		if (intent.resolveActivity(getPackageManager()) != null) {

			Calendar cal = Calendar.getInstance();

			File file = new File(CamerHandler.GetCamerHandlerInstance()
					.getImageDirectory(), (cal.getTimeInMillis() + ".png"));

			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"Failed to make file", 500).show();
				}
			} else {
				file.delete();
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),
							"Failed to make file", 500).show();
				}
			}

			capturedImageUri = Uri.fromFile(file);

			// File file = new File(Environment.getExternalStorageDirectory() +
			// File.separator + "image.jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
			startActivityForResult(intent, Constants.REQUEST_CAMERA);
		}

	}

}
