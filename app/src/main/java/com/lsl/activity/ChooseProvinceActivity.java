package com.lsl.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsl.Contacts;
import com.lsl.R;
import com.lsl.adapter.CityAdapter;
import com.lsl.adapter.DistrictSelectAdapter;
import com.lsl.adapter.ProvinceAdapter;
import com.lsl.model.CityModel;
import com.lsl.model.DistrictModel;
import com.lsl.model.ProvinceModel;
import com.lsl.utils.CityXmlParserHandler;
import com.lsl.utils.DistrictXmlParserHandler;
import com.lsl.utils.XmlParserHandler;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by DragonWang on 2016/3/14.
 */
public class ChooseProvinceActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView allProvincesList;
    private TextView back;
    private TextView title;
    private List<DistrictModel> listDistrict ;
    private List<CityModel> listCity ;
    private List<ProvinceModel> listProvince ;
    private ProvinceAdapter Provinceadapter;
    private String province="";
    private String city="";
    private String district="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseprovinceactivity);
        initView();
        getProvinceList();
    }

    private void getProvinceList() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                AssetManager asset=getAssets();
                try {
                    InputStream input=asset.open("province.xml");
                    SAXParserFactory spf=SAXParserFactory.newInstance();
                    SAXParser parser=spf.newSAXParser();
                    XmlParserHandler handler=new XmlParserHandler();
                    parser.parse(input,handler);
                    input.close();
                    listProvince.clear();
                    listProvince.addAll(handler.getDataList());
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
    private void getCityList(final String provinceName) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                AssetManager asset=getAssets();
                try {
                    InputStream input=asset.open("province.xml");
                    SAXParserFactory spf=SAXParserFactory.newInstance();
                    SAXParser parser=spf.newSAXParser();
                    CityXmlParserHandler handler=new CityXmlParserHandler(provinceName);
                    parser.parse(input,handler);
                    input.close();
                    listCity.clear();
                    listCity.addAll(handler.getCityModels());
                    myHandler.sendEmptyMessage(Contacts.CODE_REQUEST) ;
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
    private void getDistrictList(final String cityName) {
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
                    listDistrict.clear();
                    listDistrict.addAll(handler.getDistrictModels());
                    myHandler.sendEmptyMessage(Contacts.CODE_QUEST) ;
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
    private int Flag=0;
    private Handler myHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Contacts.CODE_RESULT:
                    Provinceadapter = new ProvinceAdapter(ChooseProvinceActivity.this, listProvince) ;
                    allProvincesList.setAdapter(Provinceadapter);
                    Flag=1;
                    setProvinceListener();

                    break;
                case Contacts.CODE_REQUEST:

                    CityAdapter adapter=new CityAdapter(ChooseProvinceActivity.this,listCity);
                    allProvincesList.setAdapter(adapter);
                    Flag=2;
                    setProvinceListener();
                    break;
                case Contacts.CODE_QUEST:
                    DistrictSelectAdapter adapterDistrict=new DistrictSelectAdapter(ChooseProvinceActivity.this,listDistrict);
                    allProvincesList.setAdapter(adapterDistrict);
                    Flag=3;
                    setProvinceListener();
                    break;
                default:
                    break;
            }
        }
    } ;
    private  void setProvinceListener(){
        allProvincesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Flag==1){
                    province=listProvince.get(position).getName();
                    getCityList(province);
                }else if (Flag==2){
                    city=listCity.get(position).getName();
                    getDistrictList(city);
                }else if (Flag==3){
                    district=listDistrict.get(position).getName();
                    Toast.makeText(ChooseProvinceActivity.this,"省份=="+province+"**城市=="+city+"**区县=="+district,Toast.LENGTH_SHORT).show();
                    getProvinceList();
                }

            }
        });
    }
    private void initView() {
        allProvincesList= (ListView) findViewById(R.id.allProvincesList);
        back= (TextView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title= (TextView) findViewById(R.id.title);
        title.setText("省份选择");
        listDistrict=new ArrayList<>();
        listCity=new ArrayList<>();
        listProvince=new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                back();
                break;
        }
    }
    private void back(){
        if (Flag==1){
            finish();
        }else if(Flag==2){
            getProvinceList();
        }else if (Flag==3){
            getCityList(province);
        }else {}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                back();
                break;

            default:
                break;
        }
        return false ;
    }
}
