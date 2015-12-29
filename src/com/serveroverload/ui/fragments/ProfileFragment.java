/**
 * 
 */
package com.serveroverload.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.serveroverload.util.Constants;
import com.serveroverload.util.PreferenceHelper;
import com.serveroverload.util.UriToUrl;
import com.serveroverload.util.UtilFunctions;
import com.serveroverload.util.UtilFunctions.AnimationType;
import com.serveroverload.yago.R;
import com.squareup.picasso.Picasso;

/**
 * @author 663918
 *
 */
public class ProfileFragment extends Fragment {

	private Uri imageUri;
	private ImageView profilePic;

	public static Fragment newInstance() {
		return new ProfileFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.profile_fragment, container, false);
		Bundle bdl = getArguments();

		profilePic = (ImageView) v.findViewById(R.id.profile_pic);

		String imageUrl = PreferenceHelper.getPrefernceHelperInstace(
				getActivity()).getString(PreferenceHelper.IMAGE_URL, null);

		if (null != imageUrl) {
			Picasso.with(getActivity()).load(Uri.parse(imageUrl)).centerCrop()
					.fit().into(profilePic);
		}

		v.findViewById(R.id.btn_change_pwd).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.switchContent(R.id.frag_root,
								UtilFunctions.CHANGE_PASSWORD_TAG,
								getActivity(), AnimationType.SLIDE_LEFT);

					}
				});

		v.findViewById(R.id.profile_pic).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						final Dialog dialog = new Dialog(getActivity());
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.dialog_short_item);
						// dialog.setCancelable(false);
						dialog.show();

						dialog.findViewById(R.id.short_by_price)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										dialog.dismiss();

										displayCamera();

										UtilFunctions.vibrate(getActivity());

										// Toast.makeText(getActivity(),
										// "Shot By price", 500).show();

									}
								});

						dialog.findViewById(R.id.short_by_discount)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										dialog.dismiss();
										
										displayGallery();

										UtilFunctions.vibrate(getActivity());

										// Toast.makeText(getActivity(),
										// "Shot By Discount", 500).show();

									}
								});

						dialog.findViewById(R.id.short_by_none)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										dialog.dismiss();

										UtilFunctions.vibrate(getActivity());

										// Toast.makeText(getActivity(),
										// "Shot By None", 500).show();

									}
								});

					}
				});

		return v;
	}

	@SuppressWarnings("unused")
	private void displayGallery() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& !Environment.getExternalStorageState().equals(
						Environment.MEDIA_CHECKING)) {
			Intent intent = new Intent();
			intent.setType("image/jpeg");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, Constants.REQUEST_GALLERY);
		} else {
			// Toast.make(getApplicationContext(), R.string.no_media);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUEST_CAMERA) {
			try {
				if (resultCode == Activity.RESULT_OK) {
					// displayPhotoActivity(1);

					PreferenceHelper.getPrefernceHelperInstace(getActivity())
							.setString(PreferenceHelper.IMAGE_URL,
									imageUri.toString());

					Picasso.with(getActivity()).load(imageUri).centerCrop()
							.fit().into(profilePic);

				} else {
					UriToUrl.deleteUri(getActivity(), imageUri);
				}
			} catch (Exception e) {
				// Toaster.make(getApplicationContext(),
				// R.string.error_img_not_found);
			}
		} else if (resultCode == Activity.RESULT_OK
				&& requestCode == Constants.REQUEST_GALLERY) {
			try {
				imageUri = data.getData();

				PreferenceHelper.getPrefernceHelperInstace(getActivity())
						.setString(PreferenceHelper.IMAGE_URL,
								imageUri.toString());

				Picasso.with(getActivity()).load(imageUri).centerCrop().fit()
						.into(profilePic);
				// displayPhotoActivity(2);
			} catch (Exception e) {
				// Toaster.make(getApplicationContext(),
				// R.string.error_img_not_found);
			}
		}
	}

	@SuppressWarnings("unused")
	private void displayCamera() {
		imageUri = getOutputMediaFile();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, Constants.REQUEST_CAMERA);
	}

	private Uri getOutputMediaFile() {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, "Camera Pro");
		values.put(MediaStore.Images.Media.DESCRIPTION, "www.appsroid.org");
		return getActivity().getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

}
