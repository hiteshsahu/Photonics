package com.serveroverload.cube.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.serveroverload.customview.TextAwesome;
import com.serveroverload.yago.R;

// TODO: Auto-generated Javadoc
/**
 * The Class DrawerListArrayAdapter.
 */
public class DrawerListArrayAdapter extends ArrayAdapter<String> {

	/** The context. */
	private final Context context;

	/** The values. */
	private final String[] values;

	/** The default tone. */
	private int defaultTone = Color.parseColor("#bf360c");

	/** The tone. */
	private float tone = 1.0f;

	/** The tones. */
	private ArrayList<Integer> tones = new ArrayList<Integer>();
	private ArrayList<Integer> icons = new ArrayList<Integer>();

	private ViewHolder holder;

	private void getTone() {

		tones.add(Color.parseColor("#B39DDB"));
		tones.add(Color.parseColor("#9575CD"));
		tones.add(Color.parseColor("#7E57C2"));
		tones.add(Color.parseColor("#673AB7"));
		tones.add(Color.parseColor("#5E35B1"));
		tones.add(Color.parseColor("#512DA8"));
		tones.add(Color.parseColor("#4A148C"));

	}

	private void getIcons() {

		icons.add(R.string.fa_image);
		icons.add(R.string.fa_camera);
		icons.add(R.string.fa_wrench);
		icons.add(R.string.fa_edit);
		icons.add(R.string.fa_map_marker);
		icons.add(R.string.fa_gears);
		icons.add(R.string.fa_clipboard);

	}

	/**
	 * Instantiates a new drawer list array adapter.
	 *
	 * @param context
	 *            the context
	 * @param values
	 *            the values
	 */
	public DrawerListArrayAdapter(Context context, String[] values) {
		super(context, -1, values);
		this.context = context;
		this.values = values;

		getTone();
		getIcons();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflator.inflate(R.layout.drawer_list_item, parent, false);
			holder = new ViewHolder();
			holder.title = ((TextView) rowView.findViewById(android.R.id.title));
			holder.icon = (TextAwesome) rowView.findViewById(R.id.icon);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		// LayoutInflater inflater = (LayoutInflater)
		// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View rowView = inflater.inflate(R.layout.drawer_list_item, parent,
		// false);

		rowView.setBackgroundColor(tones.get(position));
		holder.title.setText(values[position]);
		holder.icon.setText(icons.get(position));

		return rowView;
	}

	class ViewHolder {
		TextView title;
		TextAwesome icon;
	}
}
