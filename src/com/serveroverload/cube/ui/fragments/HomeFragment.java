/**
 * 
 */
package com.serveroverload.cube.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.serveroverload.cube.adapter.HomeGridListArrayAdapter;
import com.serveroverload.customview.CardsEffect;
import com.serveroverload.customview.JazzyGridView;
import com.serveroverload.util.CamerHandler;
import com.serveroverload.util.UtilFunctions;
import com.serveroverload.util.UtilFunctions.AnimationType;
import com.serveroverload.yago.R;

/**
 * @author 663918
 *
 */
public class HomeFragment extends Fragment implements OnRefreshListener {

	protected static final String TAG = HomeFragment.class.getSimpleName();

	String tagJSONReq = "reqCategoryTask";

	/** The swipe layout. */
	private SwipeRefreshLayout swipeLayout;

	/** The double back to exit pressed once. */
	private boolean doubleBackToExitPressedOnce;

	/** The m handler. */
	private Handler mHandler = new Handler();

	/** The m runnable. */
	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			doubleBackToExitPressedOnce = false;
		}
	};

	/** The items grid view. */
	private JazzyGridView itemsGridView;

	private ProgressBar progressBar;

	private HomeGridListArrayAdapter adapter;

	private View rootView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.home_fragment, container,
				false);

		getActivity().setTitle("Search");

		itemsGridView = (JazzyGridView) rootView
				.findViewById(R.id.listView_products);
		// progressBar = (ProgressBar) rootView.findViewById(R.id.loading_bar);

		itemsGridView.setTransitionEffect(new CardsEffect(24));

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(HomeFragment.this);
		swipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);

		itemsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				UtilFunctions.switchContent(R.id.frag_root,
						UtilFunctions.DETAIL_FRAGMENT_TAG, getActivity(),
						AnimationType.SLIDE_DOWN);

			}
		});

		setUpGrid(rootView);
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {

					UtilFunctions.switchContent(R.id.frag_root,
							UtilFunctions.DETAIL_FRAGMENT_TAG, getActivity(),
							AnimationType.SLIDE_DOWN);

				}
				return true;
			}
		});

		return rootView;
	}

	/**
	 * @param rootView
	 */
	public void setUpGrid(View rootView) {
		if (!CamerHandler.GetCamerHandlerInstance().getAllImage().isEmpty()) {
			// Gridview adapter

			rootView.findViewById(R.id.no_data).setVisibility(View.GONE);
			swipeLayout.setVisibility(View.VISIBLE);

			// setting grid view adapter
			itemsGridView.setAdapter(new HomeGridListArrayAdapter(
					getActivity(), R.layout.row_swipe_grid_item, CamerHandler
							.GetCamerHandlerInstance().getAllImage()));

		} else {

			rootView.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
			swipeLayout.setVisibility(View.GONE);

		}
	}

	//
	// /**
	// * @return
	// */
	// public void fillCategoryData() {
	//
	// progressBar.setVisibility(View.VISIBLE);
	//
	//
	// JsonArrayRequest jsonObjReq = new JsonArrayRequest(Method.GET,
	// NetworkConstants.URL_GET_PRODUCTS_BY_CATEGORY,
	// new Response.Listener<JSONArray>() {
	//
	// @Override
	// public void onResponse(JSONArray response) {
	//
	// new JSONParser(
	// NetworkConstants.GET_ALL_PRODUCT_BY_CATEGORY,
	// response.toString()).parse();
	//
	// if (null != itemsGridView)
	// itemsGridView
	// .setAdapter(new HomeGridListArrayAdapter(
	// getActivity(),
	// android.R.layout.activity_list_item,
	// GlobaDataHolder
	// .getGlobaDataHolder()
	// .getListOfCategory()));
	//
	// if (null != swipeLayout) {
	// swipeLayout.setRefreshing(false);
	// }
	//
	// if (null != progressBar) {
	// progressBar.setVisibility(View.GONE);
	// }
	//
	// }
	//
	// }, new Response.ErrorListener() {
	//
	// @Override
	// public void onErrorResponse(VolleyError error) {
	//
	// Log.e(TAG, "Error-----------" + error.toString());
	//
	// if (null != swipeLayout) {
	// swipeLayout.setRefreshing(false);
	// }
	//
	// if (null != progressBar) {
	// progressBar.setVisibility(View.GONE);
	// }
	//
	// if (error instanceof TimeoutError
	// || error instanceof NoConnectionError) {
	//
	// if (null != getActivity())
	// Toast.makeText(getActivity(), "Timeout Error",
	// Toast.LENGTH_LONG).show();
	//
	// } else if (error instanceof AuthFailureError) {
	// // TODO
	// } else if (error instanceof ServerError) {
	//
	// if (null != getActivity())
	// Toast.makeText(
	// getActivity(),
	// "Server Error" + error.networkResponse
	// + error.getLocalizedMessage(),
	// Toast.LENGTH_LONG).show();
	// // TODO
	// } else if (error instanceof NetworkError) {
	//
	// if (null != getActivity())
	// Toast.makeText(
	// getActivity(),
	// "Network Error" + error.networkResponse
	// + error.getLocalizedMessage(),
	// Toast.LENGTH_LONG).show();
	//
	// } else if (error instanceof ParseError) {
	//
	// if (null != getActivity())
	// Toast.makeText(
	// getActivity(),
	// "Parsing Error" + error.networkResponse
	// + error.getLocalizedMessage(),
	// Toast.LENGTH_LONG).show();
	//
	// }
	// }
	//
	// }) {
	//
	// @Override
	// protected Map<String, String> getParams() {
	// Map<String, String> params = new HashMap<String, String>();
	//
	// return params;
	// }
	//
	// };
	//
	// // jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000 * 2, 0, 0));
	//
	// jsonObjReq.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS
	// .toMillis(60), DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
	// DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	//
	// SpeedyTurtleApp.getInstance().addToRequestQueue(jsonObjReq,
	// tagJSONReq);

	// }

	// private void showBanner() {
	// if (showVdo) {
	//
	// LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	// View headerView = layoutInflater.inflate(R.layout.banner_vdo_home,
	// null);
	// itemsGridView.addHeaderView(headerView);
	//
	// videoView = (VideoView) headerView.findViewById(R.id.vdo_banner);
	// videoView.setVideoURI(Uri.parse("android.resource://"
	// + getActivity().getPackageName() + "/" + R.raw.vdo_3));
	// // MediaController md = new MediaController(getActivity());
	// videoView.setMediaController(null);
	// videoView.requestFocus();
	// // videoView.setZOrderOnTop(true);
	// videoView.start();
	// videoView.setOnTouchListener(new OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// // TODO Auto-generated method stub
	// return true;
	// }
	// });
	//
	// videoView.setOnPreparedListener(new OnPreparedListener() {
	//
	// @Override
	// public void onPrepared(MediaPlayer mp) {
	// // TODO Auto-generated method stub
	// mp.setLooping(true);
	// }
	// });
	//
	// } else {
	// LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	// View headerView = layoutInflater
	// .inflate(R.layout.banner_home, null);
	// itemsGridView.addHeaderView(headerView);
	//
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener#onRefresh
	 * ()
	 */
	@Override
	public void onRefresh() {

		swipeLayout.setRefreshing(false);

		setUpGrid(rootView);

	}

	public static Fragment newInstance() {
		// TODO Auto-generated method stub
		return new HomeFragment();
	}

}
