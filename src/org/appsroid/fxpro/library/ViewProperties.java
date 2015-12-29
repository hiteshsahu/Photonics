package org.appsroid.fxpro.library;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class ViewProperties {

	@SuppressLint("NewApi")
	public static void alpha(View view, float value) {
		if (Build.VERSION.SDK_INT < 11) {
			final AlphaAnimation animation = new AlphaAnimation(value, value);
			animation.setDuration(1);
			animation.setFillAfter(true);
			view.startAnimation(animation);
		} else {
			view.setAlpha(value);
		}
	}

}
