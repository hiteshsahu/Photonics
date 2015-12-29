/**
 * 
 */
package com.serveroverload.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.serveroverload.util.Constants;
import com.serveroverload.util.UtilFunctions;
import com.serveroverload.util.UtilFunctions.AnimationType;
import com.serveroverload.yago.R;

/**
 * @author 663918
 *
 */
public class SelectUserTypeFragment extends Fragment {

	public static Fragment newInstance() {
		return new SelectUserTypeFragment();
	}

	private Animation animation;
	private RelativeLayout top_holder;
	private RelativeLayout bottom_holder;
	private RelativeLayout step_number;
	private Uri imageUri;
	private boolean click_status = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.select_user_type_fragment, container,
				false);
		Bundle bdl = getArguments();

		top_holder = (RelativeLayout) rootview.findViewById(R.id.top_holder);
		bottom_holder = (RelativeLayout) rootview
				.findViewById(R.id.bottom_holder);
		step_number = (RelativeLayout) rootview.findViewById(R.id.step_number);

		bottom_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				flyOut(Constants.USER_TYPE_DRIVER);

			}
		});

		top_holder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				flyOut(Constants.USER_TYPE_RIDER);

			}
		});

		return rootview;
	}

	@Override
	public void onStart() {
		flyIn();
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void flyOut(final String method_name) {
		if (click_status) {
			click_status = false;

			animation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.step_number_back);
			step_number.startAnimation(animation);

			animation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.holder_top_back);
			top_holder.startAnimation(animation);

			animation = AnimationUtils.loadAnimation(getActivity(),
					R.anim.holder_bottom_back);
			bottom_holder.startAnimation(animation);

			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation arg0) {
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {
				}

				@Override
				public void onAnimationEnd(Animation arg0) {

					if (method_name
							.equalsIgnoreCase(Constants.USER_TYPE_DRIVER)) {
						UtilFunctions.switchContentWithOneBundle(R.id.root,
								UtilFunctions.REGISTRATION_FRAG_TAG,
								getActivity(), AnimationType.SLIDE_LEFT,
								Constants.USER_TYPE_DRIVER);

					} else if (method_name
							.equalsIgnoreCase(Constants.USER_TYPE_RIDER)) {

						UtilFunctions.switchContentWithOneBundle(R.id.root,
								UtilFunctions.REGISTRATION_FRAG_TAG,
								getActivity(), AnimationType.SLIDE_LEFT,
								Constants.USER_TYPE_RIDER);

					}
					// callMethod(method_name);
				}
			});
		}
	}

	// private void callMethod(String method_name) {
	// if (method_name.equals("finish")) {
	// //overridePendingTransition(0, 0);
	// //finish();
	// } else {
	// try {
	// Method method = getClass().getDeclaredMethod(method_name);
	// method.invoke(getActivity(), new Object[] {});
	// } catch (Exception e) {
	// }
	// }
	// }

	// @Override
	// public void onBackPressed() {
	// flyOut("finish");
	// super.onBackPressed();
	// }

	private void flyIn() {
		click_status = true;

		animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.holder_top);
		top_holder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.holder_bottom);
		bottom_holder.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.step_number);
		step_number.startAnimation(animation);
	}

}
