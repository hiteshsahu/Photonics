package com.serveroverload.cube.useless;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.serveroverload.customview.WheelView;
import com.serveroverload.yago.R;

public class MainActivity extends Activity implements
		View.OnClickListener {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String[] PLANETS = new String[] { "Mercury", "Venus",
			"Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wheel);

		WheelView wva = (WheelView) findViewById(R.id.main_wv);

		wva.setOffset(1);
		wva.setItems(Arrays.asList(PLANETS));
		wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: "
						+ item);
			}
		});

		findViewById(R.id.main_show_dialog_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_show_dialog_btn:
			View outerView = LayoutInflater.from(this).inflate(
					R.layout.wheel_view, null);
			WheelView wv = (WheelView) outerView
					.findViewById(R.id.wheel_view_wv);
			wv.setOffset(2);
			wv.setItems(Arrays.asList(PLANETS));
			wv.setSeletion(3);
			wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
				@Override
				public void onSelected(int selectedIndex, String item) {
					Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex
							+ ", item: " + item);
				}
			});

			new AlertDialog.Builder(this).setTitle("WheelView in Dialog")
					.setView(outerView).setPositiveButton("OK", null).show();

			break;
		}
	}

}
