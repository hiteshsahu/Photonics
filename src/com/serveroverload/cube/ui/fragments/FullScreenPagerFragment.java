package com.serveroverload.cube.ui.fragments;

import java.io.File;

import org.appsroid.fxpro.library.Constants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.serveroverload.cube.adapter.ExtendedViewPager;
import com.serveroverload.customview.TouchImageView;
import com.serveroverload.ui.activity.ImageEditorActivity;
import com.serveroverload.util.CamerHandler;
import com.serveroverload.util.UtilFunctions;
import com.serveroverload.util.UtilFunctions.AnimationType;
import com.serveroverload.yago.R;
import com.squareup.picasso.Picasso;

public class FullScreenPagerFragment extends Fragment {

	private static final String PAGER_POSITION = "PagerPosition";
	private static int currentPagePosition = 0;

	/**
	 * Step 1: Download and set up v4 support library:
	 * http://developer.android.com/tools/support-library/setup.html Step 2:
	 * Create ExtendedViewPager wrapper which calls
	 * TouchImageView.canScrollHorizontallyFroyo Step 3: ExtendedViewPager is a
	 * custom view and must be referred to by its full package name in XML Step
	 * 4: Write TouchImageAdapter, located below Step 5. The ViewPager in the
	 * XML should be ExtendedViewPager
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_viewpager_example,
				container, false);
		if (null != getArguments()) {
			currentPagePosition = getArguments().getInt(PAGER_POSITION);
		}

		// setContentView(R.layout.activity_viewpager_example);
		ExtendedViewPager mViewPager = (ExtendedViewPager) rootView
				.findViewById(R.id.view_pager);
		mViewPager.setAdapter(new TouchImageAdapter());

		mViewPager.setCurrentItem(currentPagePosition);

		rootView.findViewById(R.id.edit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								ImageEditorActivity.class);
						intent.putExtra(Constants.EXTRA_KEY_IMAGE_SOURCE, 0);
						intent.setData(Uri.fromFile(CamerHandler
								.GetCamerHandlerInstance().getImageURL()
								.get(currentPagePosition)));
						startActivity(intent);

					}
				});

		rootView.findViewById(R.id.water_mark).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						UtilFunctions.switchFragmentWithAnimation(
								R.id.frag_root,
								WaterMarkFragment.newInstance(CamerHandler
										.GetCamerHandlerInstance()
										.getAllImage().get(currentPagePosition)
										.toString()), getActivity(),
								"WaterMarkFragment", AnimationType.SLIDE_RIGHT);

					}
				});

		rootView.findViewById(R.id.share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						// Bitmap icon = mBitmap;
						// Intent share = new Intent(Intent.ACTION_SEND);
						// share.setType("image/jpeg");
						//
						// ContentValues values = new ContentValues();
						// values.put(Images.Media.TITLE, "title");
						// values.put(Images.Media.MIME_TYPE, "image/jpeg");
						// Uri uri =
						// getActivity().getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
						// values);
						//
						//
						// OutputStream outstream;
						// try {
						// outstream =
						// getActivity().getContentResolver().openOutputStream(uri);
						// icon.compress(Bitmap.CompressFormat.JPEG, 100,
						// outstream);
						// outstream.close();
						// } catch (Exception e) {
						// System.err.println(e.toString());
						// }
						//
						// share.putExtra(Intent.EXTRA_STREAM, uri);
						// startActivity(Intent.createChooser(share,
						// "Share Image"));

					}
				});

		return rootView;
	}

	static class TouchImageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return CamerHandler.GetCamerHandlerInstance().getAllImage().size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			TouchImageView img = new TouchImageView(container.getContext());
			// img.setImageResource(images[position]);

			currentPagePosition = position;

			Picasso.with(container.getContext())
					.load((new File(CamerHandler.GetCamerHandlerInstance()
							.getAllImage().get(position)))).fit().centerCrop()
					.into(img);

			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);

			return img;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	public static Fragment newInstance(int position) {
		Bundle bundle = new Bundle();
		bundle.putInt(PAGER_POSITION, position);
		FullScreenPagerFragment fullScreenPagerFragment = new FullScreenPagerFragment();
		fullScreenPagerFragment.setArguments(bundle);

		return fullScreenPagerFragment;
	}
}
