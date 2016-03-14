package com.lsl.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lsl.R;
import com.lsl.model.CoverCityBean;


public class CitySelectAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<CoverCityBean> datas;

	public CitySelectAdapter(Context context, List<CoverCityBean> datas) {
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {

		final CoverCityBean person = datas.get(position);
		Holder hd;
		if (v == null) {
			hd = new Holder();
			v = inflater.inflate(
					R.layout.activity_custom_title_listview_section_item, null);
			hd.textView = (TextView) v.findViewById(R.id.example_text_view);
			v.setTag(hd);
		} else {
			hd = (Holder) v.getTag();
		}
		hd.textView.setText(person.getCityname());
		return v;
	}

	class Holder {
		TextView textView;
	}
}
