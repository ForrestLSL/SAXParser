package com.lsl.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lsl.R;
import com.lsl.model.CityModel;
import com.lsl.model.DistrictModel;
import com.lsl.model.ProvinceModel;


public class DistrictSelectAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<DistrictModel> datas;
	public DistrictSelectAdapter(Context context, List<DistrictModel> datas) {
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}


	@Override
	public int getCount() {
		return datas!=null?datas.size():0;
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

		final DistrictModel person = datas.get(position);
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
		hd.textView.setText(person.getName());
		return v;
	}

	class Holder {
		TextView textView;
	}
}