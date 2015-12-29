package com.serveroverload.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.serveroverload.yago.R;

public class SplashActivity extends Activity {

	private Animation animation;
	private ImageView logo;
	private TextView title_txt;
	private TextView title2_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		logo = (ImageView) findViewById(R.id.logo_img);
		title_txt = (TextView) findViewById(R.id.track_txt);
		title2_txt = (TextView) findViewById(R.id.pro_txt);

		if (savedInstanceState == null) {
			flyIn();
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				endSplash();
			}
		}, 3000);
	}

	private void flyIn() {
		animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
		logo.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.app_name_animation);
		title_txt.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.pro_animation);
		title2_txt.startAnimation(animation);
	}

	private void endSplash() {
		animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation_back);
		logo.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.app_name_animation_back);
		title_txt.startAnimation(animation);

		animation = AnimationUtils.loadAnimation(this, R.anim.pro_animation_back);
		title2_txt.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent intent = new Intent(getApplicationContext(), SampleActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationStart(Animation arg0) {
			}
		});

	}

	@Override
	public void onBackPressed() {
		// Do nothing
	}

}
