/**
 * 
 */
package com.serveroverload.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.serveroverload.util.UtilFunctions;
import com.serveroverload.yago.R;

/**
 * @author 663918
 *
 */
public class EstimateFragment extends Fragment {

	public static Fragment newInstance() {
		return new EstimateFragment();
	}

	private View rootView;
	private TextView source;
	private TextView destination;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.estimate_fragment, container,
				false);
		Bundle bdl = getArguments();

		final TextView fareInfo = (TextView) rootView
				.findViewById(R.id.fare_info);

		final TextView diatanceInfo = (TextView) rootView
				.findViewById(R.id.distance_info);

		final TextView timeInfo = (TextView) rootView
				.findViewById(R.id.time_info);

		source = (TextView) rootView.findViewById(R.id.pickUp_add);

		destination = (TextView) rootView.findViewById(R.id.drop_add);

		rootView.findViewById(R.id.txt_fare_estimate).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.vibrate(getActivity());

						rootView.findViewById(R.id.fare_info).setVisibility(
								View.VISIBLE);

						diatanceInfo.setVisibility(View.GONE);

						timeInfo.setVisibility(View.GONE);

						rootView.findViewById(R.id.txt_fare_estimate)
								.setBackgroundColor(
										getResources().getColor(
												R.color.app_theme_color));

						((TextView) rootView
								.findViewById(R.id.txt_fare_estimate))
								.setTextColor(getResources().getColor(
										R.color.white));

						rootView.findViewById(R.id.txt_distance_estimate)
								.setBackgroundColor(
										getResources().getColor(R.color.white));

						((TextView) rootView
								.findViewById(R.id.txt_distance_estimate))
								.setTextColor(getResources().getColor(
										R.color.app_theme_color));

						rootView.findViewById(R.id.txt_time_estimate)
								.setBackgroundColor(
										getResources().getColor(R.color.white));

						((TextView) rootView
								.findViewById(R.id.txt_time_estimate))
								.setTextColor(getResources().getColor(
										R.color.app_theme_color));

						if (!(source.getText().toString().isEmpty() || destination
								.getText().toString().isEmpty())) {
							String[] add = { source.getText().toString(),
									destination.getText().toString() };
//
//							new DistanceBetweenAddressTask(getActivity(),
//									fareInfo, null, 0).execute(add);
						} else {
							Toast.makeText(getActivity(), "Invalid Locations",
									1000).show();
						}

					}
				});

		rootView.findViewById(R.id.txt_distance_estimate).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.vibrate(getActivity());

						rootView.findViewById(R.id.fare_info).setVisibility(
								View.GONE);

						diatanceInfo.setVisibility(View.VISIBLE);

						timeInfo.setVisibility(View.GONE);

						rootView.findViewById(R.id.txt_fare_estimate)
								.setBackgroundColor(
										getResources().getColor(R.color.white));

						((TextView) rootView
								.findViewById(R.id.txt_fare_estimate))
								.setTextColor(getResources().getColor(
										R.color.app_theme_color));

						rootView.findViewById(R.id.txt_distance_estimate)
								.setBackgroundColor(
										getResources().getColor(
												R.color.app_theme_color));

						((TextView) rootView
								.findViewById(R.id.txt_distance_estimate))
								.setTextColor(getResources().getColor(
										R.color.white));

						rootView.findViewById(R.id.txt_time_estimate)
								.setBackgroundColor(
										getResources().getColor(R.color.white));

						((TextView) rootView
								.findViewById(R.id.txt_time_estimate))
								.setTextColor(getResources().getColor(
										R.color.app_theme_color));

						if (!(source.getText().toString().isEmpty() || destination
								.getText().toString().isEmpty())) {
							String[] add = { source.getText().toString(),
									destination.getText().toString() };
//
//							new DistanceBetweenAddressTask(getActivity(),
//									diatanceInfo, null, 1).execute(add);
						} else {
							Toast.makeText(getActivity(), "Invalid Locations",
									1000).show();
						}

					}
				});

		rootView.findViewById(R.id.txt_time_estimate).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.vibrate(getActivity());

						rootView.findViewById(R.id.fare_info)

						.setVisibility(View.GONE);

						diatanceInfo.setVisibility(View.GONE);

						timeInfo.setVisibility(View.VISIBLE);

						rootView.findViewById(R.id.txt_fare_estimate)
								.setBackgroundColor(
										getResources().getColor(R.color.white));

						((TextView) rootView
								.findViewById(R.id.txt_fare_estimate))
								.setTextColor(getResources().getColor(
										R.color.app_theme_color));

						rootView.findViewById(R.id.txt_distance_estimate)
								.setBackgroundColor(
										getResources().getColor(R.color.white));

						((TextView) rootView
								.findViewById(R.id.txt_distance_estimate))
								.setTextColor(getResources().getColor(
										R.color.app_theme_color));

						rootView.findViewById(R.id.txt_time_estimate)
								.setBackgroundColor(
										getResources().getColor(
												R.color.app_theme_color));

						((TextView) rootView
								.findViewById(R.id.txt_time_estimate))
								.setTextColor(getResources().getColor(
										R.color.white));

						if (!(source.getText().toString().isEmpty() || destination
								.getText().toString().isEmpty())) {
							String[] add = { source.getText().toString(),
									destination.getText().toString() };

//							new DistanceBetweenAddressTask(getActivity(),
//									timeInfo, null, 2).execute(add);
						} else {
							Toast.makeText(getActivity(), "Invalid Locations",
									1000).show();
						}

					}
				});

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {
					//
					// UtilFunctions.switchFragmentWithAnimation(R.id.content_frame,
					// new HomeFragment(),
					// ((GoDhamHomeActivity) (getActivity())), "MainActivity",
					// AnimationType.SLIDE_LEFT);

				}
				return true;
			}
		});

		return rootView;
	}
}
