package com.lsl.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsl.Contacts;
import com.lsl.MainActivity;
import com.lsl.R;
import com.lsl.adapter.CitySelectAdapter;
import com.lsl.model.CoverCityBean;


public class ChooseCityActivity extends AppCompatActivity implements  OnClickListener{
	private TextView common_title_back;
	private ListView provinceListView;
	private CitySelectAdapter myAdapter;
	private List<CoverCityBean> data ;
	private View headView;
	private String provinceName = "";
	private String cityName = "";
	private String selectedName = "";
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosecityactivity);
		common_title_back= (TextView) findViewById(R.id.common_title_back);
		common_title_back.setOnClickListener(this);
		provinceListView= (ListView) findViewById(R.id.AllProvincesList);

		initHeadView();
		//加载城市数据
		initData();
		myAdapter=new CitySelectAdapter(ChooseCityActivity.this,data);
		provinceListView.setAdapter(myAdapter);
		provinceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					return;
				}
				cityName = data.get(position - 1).getCityname();
				Intent intent = new Intent(ChooseCityActivity.this, ChooseDistrictActiviity.class);
				intent.putExtra("cityName", cityName);
				startActivityForResult(intent, Contacts.CODE_REQUEST);
			}
		});
	}
	private void initHeadView() {
		TextView tv;
		headView = LayoutInflater.from(this).inflate(
				R.layout.nowlocationcitytitle, null);
		tv = (TextView) headView.findViewById(R.id.nowCity);
		tv.setText("定位信息（当前所在城市，But没有加定位功能）");
		provinceListView.addHeaderView(headView);
	}
	/**
	 * 覆盖城市，可以从服务器返回，但是现在为了方便写成本地数据
	 */
	private void initData() {
		data=new ArrayList<>();
		CoverCityBean bean1=new CoverCityBean();
		bean1.setCityname("北京");
		CoverCityBean bean2=new CoverCityBean();
		bean2.setCityname("上海");
		CoverCityBean bean3=new CoverCityBean();
		bean3.setCityname("广州");
		CoverCityBean bean4=new CoverCityBean();
		bean4.setCityname("深圳");
		CoverCityBean bean5=new CoverCityBean();
		bean5.setCityname("郑州");
		data.add(bean1);
		data.add(bean2);
		data.add(bean3);
		data.add(bean4);
		data.add(bean5);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.common_title_back:
				finish();
				break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, resultCode, data);
		switch (resultCode) {
			case Contacts.CODE_RESULT:
				if (data.getBooleanExtra("flag", false)) {
					selectedName = data.getStringExtra("districtName");
//					state = data.getIntExtra("state", -1) ;
//					resultCityName = data.getStringExtra("city") ;
//					setResult();
					Toast.makeText(ChooseCityActivity.this,"城市"+cityName+"地区"+selectedName,Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
		}
	}
}
