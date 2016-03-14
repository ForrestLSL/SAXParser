package com.lsl.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lsl.Contacts;
import com.lsl.R;
import com.lsl.adapter.DistrictSelectAdapter;
import com.lsl.model.DistrictModel;
import com.lsl.utils.DistrictXmlParserHandler;

import org.xml.sax.SAXException;


public class ChooseDistrictActiviity extends AppCompatActivity implements OnClickListener{
	private View headView ;
	private TextView common_title_back;
	private ListView provinceListView;
	private DistrictSelectAdapter adapter ;
	private List<DistrictModel> list ;
	private String cityName = "" ;
	private String districtName = "" ;
	private Intent intent ;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 	 	setContentView(R.layout.choosecityactivity);
		intent=getIntent();
		cityName = intent.getStringExtra("cityName") ;
		headView = LayoutInflater.from(this).inflate(R.layout.activity_custom_title_listview_section_item, null) ;
		common_title_back = (TextView) findViewById(R.id.common_title_back);
		common_title_back.setOnClickListener(this);
		provinceListView = (ListView) findViewById(R.id.AllProvincesList);
		initHeadView();
		list = new ArrayList<DistrictModel>() ;
		parser();
		provinceListView.setAdapter(adapter) ;
		provinceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				districtName = list.get(position-1).getName() ;
				setresult(true,1) ;
			}
		}) ;
	}

	private void parser() {
		new Thread(){
			@Override
			public void run() {
				super.run();
				AssetManager asset=getAssets();
				try {
					InputStream input=asset.open("province.xml");
					//创建一个解析XML的工厂对象
					SAXParserFactory spf=SAXParserFactory.newInstance();
					SAXParser parser=spf.newSAXParser();
					DistrictXmlParserHandler handler=new DistrictXmlParserHandler(cityName);
					parser.parse(input,handler);
					input.close();
					list.addAll(handler.getDistrictModels());
					myHandler.sendEmptyMessage(Contacts.CODE_RESULT) ;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	private Handler myHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case Contacts.CODE_RESULT:
					adapter = new DistrictSelectAdapter(ChooseDistrictActiviity.this, list) ;
					provinceListView.setAdapter(adapter) ;
					break;

				default:
					break;
			}
		}
	} ;

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.common_title_back:
				setresult(false,-1) ;
				finish();
				break;
		}

	}
	private void initHeadView() {
		TextView tv = (TextView)headView.findViewById(R.id.example_text_view) ;
		tv.setText("全"+cityName) ;
		headView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setresult(true,0) ;
			}
		}) ;
		provinceListView.addHeaderView(headView) ;
	}
	private void setresult(boolean flag,int state)
	{
		intent.putExtra("flag", flag) ;
		intent.putExtra("city", cityName) ;
		intent.putExtra("state", state) ;
		intent.putExtra("districtName", districtName) ;
		setResult(Contacts.CODE_RESULT, intent) ;
		finish() ;
	}
}
