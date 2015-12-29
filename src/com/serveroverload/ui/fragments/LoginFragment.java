/**
 * 
 */
package com.serveroverload.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.serveroverload.util.UtilFunctions;
import com.serveroverload.util.UtilFunctions.AnimationType;
import com.serveroverload.yago.R;

/**
 * @author 663918
 *
 */
public class LoginFragment extends Fragment {

	public static Fragment newInstance() {
		return new LoginFragment();
	}

	private boolean doubleBackToExitPressedOnce;
	private Handler mHandler = new Handler();

	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			doubleBackToExitPressedOnce = false;
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.login_fragment, container,
				false);
		Bundle bdl = getArguments();

		rootView.findViewById(R.id.new_user).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.switchContent(R.id.root,
								UtilFunctions.SELECT_USER_TYPE_TAG,
								getActivity(), AnimationType.SLIDE_UP);

					}
				});

		rootView.findViewById(R.id.forgot_password).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.switchContent(R.id.root,
								UtilFunctions.CHANGE_PASSWORD_TAG,
								getActivity(), AnimationType.SLIDE_UP);

					}
				});

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {

					if (doubleBackToExitPressedOnce) {
						// super.onBackPressed();

						if (mHandler != null) {
							mHandler.removeCallbacks(mRunnable);
						}

						getActivity().finish();

						return true;
					}

					doubleBackToExitPressedOnce = true;
					
					Toast.makeText(getActivity(),
							"Please click BACK again to exit",
							Toast.LENGTH_SHORT).show();

					mHandler.postDelayed(mRunnable, 2000);

				}
				return true;
			}
		});

		return rootView;
	}

}
