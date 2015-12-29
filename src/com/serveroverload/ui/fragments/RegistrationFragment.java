/**
 * 
 */
package com.serveroverload.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.serveroverload.util.Constants;
import com.serveroverload.util.UtilFunctions;
import com.serveroverload.util.UtilFunctions.AnimationType;
import com.serveroverload.yago.R;

/**
 * @author 663918
 *
 */
public class RegistrationFragment extends Fragment {

	private View v;
	public static final String USER_TYPE = "UserType";

	public static Fragment newInstance(String userType) {

		Bundle bundle = new Bundle();
		bundle.putString(USER_TYPE, userType);

		RegistrationFragment registrationFragment = new RegistrationFragment();
		registrationFragment.setArguments(bundle);

		return registrationFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (null != getArguments()) {
			if (getArguments().getString(USER_TYPE).equalsIgnoreCase(
					Constants.USER_TYPE_DRIVER)) {

				v = inflater.inflate(R.layout.driver_registration_fragment,
						container, false);

			} else if (getArguments().getString(USER_TYPE).equalsIgnoreCase(
					Constants.USER_TYPE_RIDER)) {
				v = inflater.inflate(R.layout.rider_registration_fragment,
						container, false);
			}

		}

		v.findViewById(R.id.registration_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.switchContent(R.id.root,
								UtilFunctions.PROFILE_TAG, getActivity(),
								AnimationType.SLIDE_LEFT);

					}
				});

		return v;
	}

}
