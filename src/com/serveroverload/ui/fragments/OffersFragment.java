/**
 * 
 */
package com.serveroverload.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.serveroverload.yago.R;

/**
 * @author 663918
 *
 */
public class OffersFragment extends Fragment {

	public static Fragment newInstance() {
		return new OffersFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.profile_fragment, container, false);
		Bundle bdl = getArguments();

		return v;
	}

}
