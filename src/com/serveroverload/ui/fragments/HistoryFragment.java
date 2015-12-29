/**
 * 
 */
package com.serveroverload.ui.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.serveroverload.cube.adapter.Address;
import com.serveroverload.customview.CardsEffect;
import com.serveroverload.customview.JazzyListView;
import com.serveroverload.yago.R;

/**
 * @author 663918
 *
 */
public class HistoryFragment extends Fragment implements OnRefreshListener {

	public static Fragment newInstance() {
		return new HistoryFragment();
	}

	boolean isGridMode = false;

	private JazzyListView jazzyListView;
	//private JazzyGridView jazzyGridView;

//	private AddressListArrayAdapter arrayAdapter;

	private SwipeRefreshLayout swipeLayoutList;

	private boolean doubleBackToExitPressedOnce;
	private Handler mHandler = new Handler();

	private final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			doubleBackToExitPressedOnce = false;
		}
	};

	//private HomeGridListArrayAdapter arrayAdapter1;

	private SwipeRefreshLayout swipeLayoutGrid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.history_fragment, container,
				false);
		Bundle bdl = getArguments();

		jazzyListView = (JazzyListView) rootView
				.findViewById(R.id.listView_hot_deals);
//		jazzyGridView = (JazzyGridView) rootView
//				.findViewById(R.id.gridView_hot_deals);
//		
		jazzyListView.setTransitionEffect(new CardsEffect(
				CardsEffect.ZIPPER_EFFECT));
		
		jazzyListView.setFastScrollEnabled(true);

//		jazzyGridView.setTransitionEffect(new CardsEffect(
//				CardsEffect.GROW_EFFECT));
//		jazzyGridView.setFastScrollEnabled(true);

		ArrayList<Address> listOfRecordings = new ArrayList<Address>();

		for (int i = 0; i < 1200; i++) {
			listOfRecordings
					.add(new Address("Home", "Lorelle, Wakad,",
							"Pimpri-Chinchwad", " Pune, Maharashtra , India",
							"411057"));
		}
//
//		arrayAdapter = new AddressListArrayAdapter(getActivity(),
//				R.layout.row_swipe_grid_item, listOfRecordings);
//		
		
//		arrayAdapter1 = new HomeGridListArrayAdapter(getActivity(),
//				R.layout.row_swipe_grid_item, listOfRecordings);

//		jazzyListView.setAdapter(arrayAdapter);
//		
		// FloatingActionButton fab = (FloatingActionButton) rootView
		// .findViewById(R.id.fab);
		//
		// fab.attachToListView(jazzyListView, new ScrollDirectionListener() {
		// @Override
		// public void onScrollDown() {
		// Log.d("ListViewFragment", "onScrollDown()");
		// }
		//
		// @Override
		// public void onScrollUp() {
		// Log.d("ListViewFragment", "onScrollUp()");
		// }
		// }, new AbsListView.OnScrollListener() {
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Log.d("ListViewFragment", "onScrollStateChanged()");
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		// Log.d("ListViewFragment", "onScroll()");
		// }
		// });
		//
		// fab.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// if (!isGridMode) {
		//
		// swipeLayoutList.setRefreshing(false);
		// swipeLayoutList.setVisibility(View.GONE);
		// swipeLayoutGrid.setVisibility(View.VISIBLE);
		//
		// jazzyGridView.setVisibility(View.VISIBLE);
		// jazzyListView.setVisibility(View.GONE);
		// jazzyGridView.setAdapter(arrayAdapter1);
		//
		// isGridMode = true;
		// } else {
		//
		// swipeLayoutGrid.setRefreshing(false);
		// swipeLayoutGrid.setVisibility(View.GONE);
		// swipeLayoutList.setVisibility(View.VISIBLE);
		//
		// jazzyGridView.setVisibility(View.GONE);
		// jazzyListView.setVisibility(View.VISIBLE);
		// jazzyListView.setAdapter(arrayAdapter);
		// isGridMode = false;
		// }
		// UtilFunctions.vibrate(getActivity());
		//
		// }
		// });

		swipeLayoutList = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container_list);
		swipeLayoutList.setOnRefreshListener(HistoryFragment.this);
		swipeLayoutList.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		swipeLayoutGrid = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container_grid);
		swipeLayoutGrid.setOnRefreshListener(HistoryFragment.this);
		swipeLayoutGrid.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

}
