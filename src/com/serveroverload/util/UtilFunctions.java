package com.serveroverload.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.serveroverload.cube.ui.fragments.AboutAppFragment;
import com.serveroverload.cube.ui.fragments.FullScreenPagerFragment;
import com.serveroverload.cube.ui.fragments.HomeFragment;
import com.serveroverload.cube.ui.fragments.SelectImageFragment;
import com.serveroverload.cube.ui.fragments.SettingsFragment;
import com.serveroverload.ui.fragments.ChangePasswordFragment;
import com.serveroverload.ui.fragments.EstimateFragment;
import com.serveroverload.ui.fragments.HistoryFragment;
import com.serveroverload.ui.fragments.LoginFragment;
import com.serveroverload.ui.fragments.OffersFragment;
import com.serveroverload.ui.fragments.ProfileFragment;
import com.serveroverload.ui.fragments.RegistrationFragment;
import com.serveroverload.ui.fragments.SelectUserTypeFragment;
import com.serveroverload.yago.R;

// TODO: Auto-generated Javadoc
/**
 * The Class UtilFunctions.
 */
@SuppressLint("NewApi")
public class UtilFunctions {

	private static String CURRENT_TAG = null;

	public static String DETAIL_FRAGMENT_TAG = "ViewPager";

	public static final String CHANGE_PASSWORD_TAG = "ChangePasswordFragment";
	public static final String HOME_FRAGMENT_TAG = "HomeFragment";
	public static final String SETTINGS_TAG = "AboutApp";
	public static final String PROFILE_TAG = "MyProfile";
	public static final String SELECT_IMAGE_TAG = "SelectImageFragment";
	public static final String OFFERS_TAG = "Offers";
	public static final String RIDE_HISTORY_TAG = "History";
	public static final String ABOUT_APP_TAG = "About";
	public static final String SELECT_USER_TYPE_TAG = "SelectUserType";
	/** The Constant IS_ISC. */
	// public static final boolean IS_JBMR2 = Build.VERSION.SDK_INT ==
	// Build.VERSION_CODES.JELLY_BEAN_MR2;
	public static final boolean IS_ISC = Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH;

	/** The Constant IS_GINGERBREAD_MR1. */
	public static final boolean IS_GINGERBREAD_MR1 = Build.VERSION.SDK_INT == Build.VERSION_CODES.GINGERBREAD_MR1;

	public static final String REGISTRATION_FRAG_TAG = "RegistrationFragment";

	public static final String LOGIN_TAG = "LoginFragmentTag";

	public static final String ESTIMATE_TAG = "EstimateFragment";

	public static void switchContent(int id, String TAG,
			FragmentActivity baseActivity, AnimationType transitionStyle) {

		Fragment fragmentToReplace = null;

		FragmentManager fragmentManager = baseActivity
				.getSupportFragmentManager();

		FragmentTransaction transaction = fragmentManager.beginTransaction();

		// If our current fragment is null, or the new fragment is different, we
		// need to change our current fragment
		if (CURRENT_TAG == null || !TAG.equals(CURRENT_TAG)) {

			if (transitionStyle != null) {
				switch (transitionStyle) {
				case SLIDE_DOWN:
					// Exit from down
					transaction.setCustomAnimations(R.anim.slide_up,
							R.anim.slide_down);

					break;
				case SLIDE_UP:
					// Enter from Up
					transaction.setCustomAnimations(R.anim.slide_in_up,
							R.anim.slide_out_up);
					break;
				case SLIDE_LEFT:
					// Enter from left
					transaction.setCustomAnimations(R.anim.slide_left,
							R.anim.slide_out_left);
					break;
				// Enter from right
				case SLIDE_RIGHT:
					transaction.setCustomAnimations(R.anim.slide_right,
							R.anim.slide_out_right);
					break;
				case FADE_IN:
					transaction.setCustomAnimations(R.anim.fade_in,
							R.anim.fade_out);
				case FADE_OUT:
					transaction.setCustomAnimations(R.anim.fade_in,
							R.anim.donot_move);
					break;
				case SLIDE_IN_SLIDE_OUT:
					transaction.setCustomAnimations(R.anim.slide_left,
							R.anim.slide_out_left);
					break;
				default:
					break;
				}
			}

			// Try to find the fragment we are switching to
			Fragment fragment = fragmentManager.findFragmentByTag(TAG);

			// If the new fragment can't be found in the manager, create a new
			// one
			if (fragment == null) {

				if (TAG.equals(HOME_FRAGMENT_TAG)) {
					fragmentToReplace = HomeFragment.newInstance();
				}

				else if (TAG.equals(PROFILE_TAG)) {
					fragmentToReplace = ProfileFragment.newInstance();
				}

				else if (TAG.equals(SELECT_IMAGE_TAG)) {
					fragmentToReplace = SelectImageFragment.newInstance();
				}

				else if (TAG.equals(OFFERS_TAG)) {
					fragmentToReplace = OffersFragment.newInstance();
				}

				else if (TAG.equals(RIDE_HISTORY_TAG)) {
					fragmentToReplace = HistoryFragment.newInstance();
				}

				else if (TAG.equals(ABOUT_APP_TAG)) {
					fragmentToReplace = AboutAppFragment.newInstance();
				}

				else if (TAG.equals(SETTINGS_TAG)) {
					fragmentToReplace = SettingsFragment.newInstance();
				}

				else if (TAG.equals(CHANGE_PASSWORD_TAG)) {
					fragmentToReplace = ChangePasswordFragment.newInstance();
				}

				else if (TAG.equals(SELECT_USER_TYPE_TAG)) {

					fragmentToReplace = SelectUserTypeFragment.newInstance();

				} else if (TAG.equals(LOGIN_TAG)) {

					fragmentToReplace = LoginFragment.newInstance();

				} else if (TAG.equals(ESTIMATE_TAG)) {

					fragmentToReplace = EstimateFragment.newInstance();

				} else if (TAG.equals(DETAIL_FRAGMENT_TAG)) {

					fragmentToReplace = FullScreenPagerFragment.newInstance(0);

				}
			}
			// Otherwise, we found our fragment in the manager, so we will reuse
			// it
			else {

				if (TAG.equals(HOME_FRAGMENT_TAG)) {
					fragmentToReplace = (HomeFragment) fragment;

				}

				else if (TAG.equals(PROFILE_TAG)) {

					fragmentToReplace = (ProfileFragment) fragment;
				}

				else if (TAG.equals(SELECT_IMAGE_TAG)) {

					fragmentToReplace = (SelectImageFragment) fragment;
				}

				else if (TAG.equals(OFFERS_TAG)) {

					fragmentToReplace = (OffersFragment) fragment;
				}

				else if (TAG.equals(RIDE_HISTORY_TAG)) {

					fragmentToReplace = (HistoryFragment) fragment;
				}

				else if (TAG.equals(ABOUT_APP_TAG)) {

					fragmentToReplace = (AboutAppFragment) fragment;
				}

				else if (TAG.equals(SETTINGS_TAG)) {

					fragmentToReplace = (SettingsFragment) fragment;
				}

				else if (TAG.equals(CHANGE_PASSWORD_TAG)) {

					fragmentToReplace = (ChangePasswordFragment) fragment;

				} else if (TAG.equals(SELECT_USER_TYPE_TAG)) {

					fragmentToReplace = (SelectUserTypeFragment) fragment;

				} else if (TAG.equals(LOGIN_TAG)) {

					fragmentToReplace = (LoginFragment) fragment;
				} else if (TAG.equals(ESTIMATE_TAG)) {

					fragmentToReplace = (EstimateFragment) fragment;
				} else if (TAG.equals(DETAIL_FRAGMENT_TAG)) {

					fragmentToReplace = (FullScreenPagerFragment) fragment;
				}
			}

			CURRENT_TAG = TAG;

			// Replace our current fragment with the one we are changing to
			transaction.replace(id, fragmentToReplace, TAG);
			transaction.commit();

		} else

		{
			// Do nothing since we are already on the fragment being changed to
		}
	}
	
	
	public static void switchFragmentWithAnimation(int id, Fragment fragment,
			FragmentActivity activity, String TAG, AnimationType transitionStyle) {

		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		if (transitionStyle != null) {
			switch (transitionStyle) {
			case SLIDE_DOWN:

				// Exit from down
				fragmentTransaction.setCustomAnimations(R.anim.slide_up,
						R.anim.slide_down);

				break;

			case SLIDE_UP:

				// Enter from Up
				fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,
						R.anim.slide_out_up);

				break;

			case SLIDE_LEFT:

				// Enter from left
				fragmentTransaction.setCustomAnimations(R.anim.slide_left,
						R.anim.slide_out_left);

				break;

			// Enter from right
			case SLIDE_RIGHT:
				fragmentTransaction.setCustomAnimations(R.anim.slide_right,
						R.anim.slide_out_right);

				break;

			case FADE_IN:
				fragmentTransaction.setCustomAnimations(R.anim.fade_in,
						R.anim.fade_out);

			case FADE_OUT:
				fragmentTransaction.setCustomAnimations(R.anim.fade_in,
						R.anim.donot_move);

				break;

			case SLIDE_IN_SLIDE_OUT:

				fragmentTransaction.setCustomAnimations(R.anim.slide_left,
						R.anim.slide_out_left);

				break;

			default:
				break;
			}
		}

		fragmentTransaction.replace(id, fragment);
		fragmentTransaction.addToBackStack(TAG);
		fragmentTransaction.commit();
	}


	public static void switchContentWithOneBundle(int id, String TAG,
			FragmentActivity baseActivity, AnimationType transitionStyle,
			String data) {

		Fragment fragmentToReplace = null;

		FragmentManager fragmentManager = baseActivity
				.getSupportFragmentManager();

		FragmentTransaction transaction = fragmentManager.beginTransaction();

		// If our current fragment is null, or the new fragment is different, we
		// need to change our current fragment
		if (CURRENT_TAG == null || !TAG.equals(CURRENT_TAG)) {

			if (transitionStyle != null) {
				switch (transitionStyle) {
				case SLIDE_DOWN:
					// Exit from down
					transaction.setCustomAnimations(R.anim.slide_up,
							R.anim.slide_down);

					break;
				case SLIDE_UP:
					// Enter from Up
					transaction.setCustomAnimations(R.anim.slide_in_up,
							R.anim.slide_out_up);
					break;
				case SLIDE_LEFT:
					// Enter from left
					transaction.setCustomAnimations(R.anim.slide_left,
							R.anim.slide_out_left);
					break;
				// Enter from right
				case SLIDE_RIGHT:
					transaction.setCustomAnimations(R.anim.slide_right,
							R.anim.slide_out_right);
					break;
				case FADE_IN:
					transaction.setCustomAnimations(R.anim.fade_in,
							R.anim.fade_out);
				case FADE_OUT:
					transaction.setCustomAnimations(R.anim.fade_in,
							R.anim.donot_move);
					break;
				case SLIDE_IN_SLIDE_OUT:
					transaction.setCustomAnimations(R.anim.slide_left,
							R.anim.slide_out_left);
					break;
				default:
					break;
				}
			}

			// Try to find the fragment we are switching to
			Fragment fragment = fragmentManager.findFragmentByTag(TAG);

			// If the new fragment can't be found in the manager, create a new
			// one
			if (fragment == null) {

				if (TAG.equals(REGISTRATION_FRAG_TAG)) {
					fragmentToReplace = RegistrationFragment.newInstance(data);
				}

			}
			// Otherwise, we found our fragment in the manager, so we will reuse
			// it
			else {

				if (TAG.equals(REGISTRATION_FRAG_TAG)) {

					fragmentToReplace = (RegistrationFragment) fragment;
				}
			}

			CURRENT_TAG = TAG;

			// Replace our current fragment with the one we are changing to
			transaction.replace(id, fragmentToReplace, TAG);
			transaction.commit();

		} else

		{
			// Do nothing since we are already on the fragment being changed to
		}
	}

	/**
	 * The Enum AnimationType.
	 */
	public enum AnimationType {

		/** The slide left. */
		SLIDE_LEFT, /** The slide right. */
		SLIDE_RIGHT, /** The slide up. */
		SLIDE_UP, /** The slide down. */
		SLIDE_DOWN, /** The fade in. */
		FADE_IN, /** The slide in slide out. */
		SLIDE_IN_SLIDE_OUT, /** The fade out. */
		FADE_OUT
	}

	/**
	 * Vibrate.
	 *
	 * @param context
	 *            the context
	 */
	public static void vibrate(Context context) {
		// Get instance of Vibrator from current Context and Vibrate for 400
		// milliseconds
		((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
				.vibrate(100);
	}

	public static boolean isPortrait(Context context) {

		return context.getResources().getBoolean(R.bool.is_portrait);
	}
//
//	String getMapURL(String latitude, String longitude) {
//
//		return "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|"
//				+ latitude + "," + longitude + "&sensor=false";
//
//	}
//
//	public static String getLocationURLFromAddress(Context context,
//			String strAddress) {
//
//		Geocoder coder = new Geocoder(context);
//		List<android.location.Address> address;
//		LatLng p1 = null;
//
//		try {
//			address = coder.getFromLocationName(strAddress, 5);
//			if (address == null) {
//				return null;
//			}
//			android.location.Address location = address.get(0);
//			location.getLatitude();
//			location.getLongitude();
//
//			return "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|"
//					+ location.getLatitude()
//					+ ","
//					+ location.getLongitude()
//					+ "&sensor=false";
//
//			//
//			// p1 = new LatLng(location.getLatitude(), location.getLongitude());
//
//		} catch (Exception ex) {
//
//			ex.printStackTrace();
//		}
//		return strAddress;
//
//		// return p1;
//	}
//
//	public static String getCompleteAddressString(Context context,
//			double LATITUDE, double LONGITUDE) {
//		String strAdd = "";
//		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//		try {
//			List<Address> addresses = geocoder.getFromLocation(LATITUDE,
//					LONGITUDE, 1);
//			if (addresses != null) {
//				Address returnedAddress = addresses.get(0);
//				StringBuilder strReturnedAddress = new StringBuilder("");
//
//				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//					strReturnedAddress
//							.append(returnedAddress.getAddressLine(i)).append(
//									"\n");
//				}
//				strAdd = strReturnedAddress.toString();
//				Log.w("My Current loction address",
//						"" + strReturnedAddress.toString());
//			} else {
//				Log.w("My Current loction address", "No Address returned!");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.w("My Current loction address", "Canont get Address!");
//		}
//		return strAdd;
//	}
//
//	public static LatLng getLocationFromAddress(Context context,
//			String strAddress) {
//
//		Geocoder coder = new Geocoder(context);
//		List<Address> address;
//		LatLng p1 = null;
//
//		try {
//			address = coder.getFromLocationName(strAddress, 5);
//			if (address == null) {
//				return null;
//			}
//			Address location = address.get(0);
//			location.getLatitude();
//			location.getLongitude();
//
//			p1 = new LatLng(location.getLatitude(), location.getLongitude());
//
//		} catch (Exception ex) {
//
//			ex.printStackTrace();
//		}
//
//		return p1;
//	}

	public String makeURL(double sourcelat, double sourcelog, double destlat,
			double destlog) {
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString(sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(destlat));
		urlString.append(",");
		urlString.append(Double.toString(destlog));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		urlString.append("&key=YOUR_API_KEY");
		return urlString.toString();
	}

//	public void drawPath(String result, GoogleMap map) {
//
//		try {
//			// Tranform the string into a json object
//			final JSONObject json = new JSONObject(result);
//			JSONArray routeArray = json.getJSONArray("routes");
//			JSONObject routes = routeArray.getJSONObject(0);
//			JSONObject overviewPolylines = routes
//					.getJSONObject("overview_polyline");
//			String encodedString = overviewPolylines.getString("points");
//
//			List<LatLng> list = decodePoly(encodedString);
//
//			Polyline line = map.addPolyline(new PolylineOptions().addAll(list)
//					.width(12).color(Color.parseColor("#05b1fb"))// Google maps
//																	// blue
//																	// color
//					.geodesic(true));
//			/*
//			 * for(int z = 0; z<list.size()-1;z++){ LatLng src= list.get(z);
//			 * LatLng dest= list.get(z+1); Polyline line = mMap.addPolyline(new
//			 * PolylineOptions() .add(new LatLng(src.latitude, src.longitude),
//			 * new LatLng(dest.latitude, dest.longitude)) .width(2)
//			 * .color(Color.BLUE).geodesic(true)); }
//			 */
//		} catch (JSONException e) {
//
//		}
//	}
//
//	public static List<LatLng> decodePoly(String encoded) {
//
//		List<LatLng> poly = new ArrayList<LatLng>();
//		int index = 0, len = encoded.length();
//		int lat = 0, lng = 0;
//
//		while (index < len) {
//			int b, shift = 0, result = 0;
//			do {
//				b = encoded.charAt(index++) - 63;
//				result |= (b & 0x1f) << shift;
//				shift += 5;
//			} while (b >= 0x20);
//			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//			lat += dlat;
//
//			shift = 0;
//			result = 0;
//			do {
//				b = encoded.charAt(index++) - 63;
//				result |= (b & 0x1f) << shift;
//				shift += 5;
//			} while (b >= 0x20);
//			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//			lng += dlng;
//
//			LatLng p = new LatLng((((double) lat / 1E5)),
//					(((double) lng / 1E5)));
//			poly.add(p);
//		}
//
//		return poly;
//	}
//
//	public static int getScreenWidth(Context context) {
//		int columnWidth;
//		WindowManager wm = (WindowManager) context
//				.getSystemService(Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
//
//		final Point point = new Point();
//		try {
//			display.getSize(point);
//		} catch (java.lang.NoSuchMethodError ignore) { // Older device
//			point.x = display.getWidth();
//			point.y = display.getHeight();
//		}
//		columnWidth = point.x;
//		return columnWidth;
//	}

}
