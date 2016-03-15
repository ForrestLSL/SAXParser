package com.lsl;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsl.activity.ChooseCityActivity;
import com.lsl.activity.ChooseProvinceActivity;
import com.lsl.model.CityModel;
import com.lsl.model.DistrictModel;
import com.lsl.model.ProvinceModel;
import com.lsl.utils.XmlParserHandler;
import com.lsl.wheelview.OptionsPickerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button province;
    private Button city;
    private Button wheelView;
    private Intent intent;
    private TextView text_area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wheelView= (Button) findViewById(R.id.wheelView);
        province= (Button) findViewById(R.id.province);
        city= (Button) findViewById(R.id.city);
        city.setOnClickListener(this);
        province.setOnClickListener(this);
        wheelView.setOnClickListener(this);
        text_area= (TextView) findViewById(R.id.text_area);
        //初始化WheelView
        initPopUpWindow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.province:
                intent=new Intent(MainActivity.this, ChooseProvinceActivity.class);
                startActivity(intent);
                break;
            case R.id.city:
                intent=new Intent(MainActivity.this, ChooseCityActivity.class);
                startActivity(intent);
                break;
            case R.id.wheelView:
                //显示WheelView
                pvOptions.show() ;
                break;
        }
    }

    //以下部分均为初始化WheelView
    private void initPopUpWindow() {
        pvOptions = new OptionsPickerView(this) ;
        initProvinceDatas();
        // 三级联动效果
        pvOptions.setPicker(mProvinceDatas, mCitisDatasMap, mDistrictDatasMap,
                true);
        // 设置选择的三级单位
        // pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(true, true, true);
        // 设置默认选中的三级项目
        // 监听确定选择按钮
        pvOptions.setSelectOptions(1, 1, 1);
        pvOptions
                .setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int option2,
                                                int options3) {
                        // 返回的分别是三个级别的选中位置
                        mCurrentProviceName = mProvinceDatas.get(options1);
                        mCurrentCityName = mCitisDatasMap.get(options1).get(
                                option2);
                        mCurrentDistrictName = mDistrictDatasMap.get(options1)
                                .get(option2).get(options3);
                        text_area
                                .setText(mCurrentProviceName + " "
                                        + mCurrentCityName + " "
                                        + mCurrentDistrictName);
                    }
                });
    }
    private OptionsPickerView pvOptions;;

    /**
     * 所有省
     */
    protected List<String> mProvinceDatas = new ArrayList<String>();
    /**
     * key - 省 value - 市
     */
    protected List<List<String>> mCitisDatasMap = new ArrayList<List<String>>();
    /**
     * key - 市 values - 区
     */
    protected List<List<List<String>>> mDistrictDatasMap = new ArrayList<List<List<String>>>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            List<ProvinceModel> provinceModels = handler.getDataList();
            // */
            for (int i = 0; i < provinceModels.size(); i++) {
                mProvinceDatas.add(provinceModels.get(i).getName());
                List<CityModel> cityList = new ArrayList<CityModel>();
                List<String> cityStringList = new ArrayList<String>();
                cityList = provinceModels.get(i).getCityList();
                for (int j = 0; j < cityList.size(); j++) {
                    cityStringList.add(cityList.get(j).getName());
                }
                mCitisDatasMap.add(cityStringList);
                List<List<String>> Stringlist = new ArrayList<List<String>>();
                for (int j = 0; j < cityList.size(); j++) {
                    List<DistrictModel> districtList = new ArrayList<DistrictModel>();
                    List<String> districtStringList = new ArrayList<String>();
                    districtList = cityList.get(j).getDistrictList();
                    for (int k = 0; k < districtList.size(); k++) {
                        districtStringList.add(districtList.get(k).getName());
                    }
                    Stringlist.add(districtStringList);
                }
                mDistrictDatasMap.add(Stringlist);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static final int ANIMATION_DURATION = 200;
    private RelativeLayout bestrow_shouhuo;
    private void alphaVisibleAnimation() {
        bestrow_shouhuo.setVisibility(View.VISIBLE);
        ObjectAnimator ob = ObjectAnimator.ofFloat(bestrow_shouhuo, "alpha",
                0f, 1f);
        ob.setDuration(ANIMATION_DURATION);
        ob.start();
    }

    private void alphaGoneAnimation() {
        bestrow_shouhuo.setVisibility(View.VISIBLE);
        ObjectAnimator ob = ObjectAnimator.ofFloat(bestrow_shouhuo, "alpha",
                1f, 0f);
        ob.setDuration(ANIMATION_DURATION);
        ob.start();
    }
}
