package com.lsl.wheelview;


import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.lsl.R;


public class WheelOptions<T> {
	private View view;
	private WheelView wv_option1;
	private WheelView wv_option2;
	private WheelView wv_option3;

	private List<T> mOptions1Items;
	private List<List<T>> mOptions2Items;
	private List<List<List<T>>> mOptions3Items;

	private ArrayWheelAdapter mOptions1Adapter ;
	private ArrayWheelAdapter mOptions2Adapter ;
	private ArrayWheelAdapter mOptions3Adapter ;
	
    private boolean linkage = false;
    private OnItemSelectedListener wheelListener_option1;
    private OnItemSelectedListener wheelListener_option2;

    private int mCurternItem = 0 ;
    
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelOptions(View view) {
		super();
		this.view = view;
		setView(view);
	}

	public void setPicker(ArrayList<T> optionsItems) {
		setPicker(optionsItems, null, null, false);
	}

	public void setPicker(List<T> options1Items,
			List<List<T>> options2Items, boolean linkage) {
		setPicker(options1Items, options2Items, null, linkage);
	}

	public void setPicker(List<T> options1Items,
			List<List<T>> options2Items,
			List<List<List<T>>> options3Items,
			boolean linkage) {
        this.linkage = linkage;
		this.mOptions1Items = options1Items;
		this.mOptions2Items = options2Items;
		this.mOptions3Items = options3Items;
		int len = ArrayWheelAdapter.DEFAULT_LENGTH;
		if (this.mOptions3Items == null)
			len = 8;
		if (this.mOptions2Items == null)
			len = 12;
		
		mOptions1Adapter = new ArrayWheelAdapter(mOptions1Items, len) ;
		mOptions2Adapter = new ArrayWheelAdapter(mOptions2Items.get(0)) ;
		mOptions3Adapter = new ArrayWheelAdapter(mOptions3Items.get(0).get(0)) ;
		
		// 选项1
		wv_option1 = (WheelView) view.findViewById(R.id.options1);
		wv_option1.setAdapter(mOptions1Adapter);// 设置显示数据
		wv_option1.setCurrentItem(0);// 初始化时显示的数据
		// 选项2
		wv_option2 = (WheelView) view.findViewById(R.id.options2);
		if (mOptions2Items != null)
			wv_option2.setAdapter(mOptions2Adapter);// 设置显示数据
		wv_option2.setCurrentItem(wv_option1.getCurrentItem());// 初始化时显示的数据
		// 选项3
		wv_option3 = (WheelView) view.findViewById(R.id.options3);
		if (mOptions3Items != null)
			wv_option3.setAdapter(mOptions3Adapter);// 设置显示数据
		wv_option3.setCurrentItem(wv_option3.getCurrentItem());// 初始化时显示的数据
		int textSize = 18;

		wv_option1.setTextSize(textSize);
		wv_option2.setTextSize(textSize);
		wv_option3.setTextSize(textSize);

		if (this.mOptions2Items == null)
			wv_option2.setVisibility(View.GONE);
		if (this.mOptions3Items == null)
			wv_option3.setVisibility(View.GONE);

		// 联动监听器
	    wheelListener_option1 = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index) {
				mCurternItem = index ;
				mOptions2Adapter = new ArrayWheelAdapter(mOptions2Items.get(index)) ;
				mOptions3Adapter = new ArrayWheelAdapter(mOptions3Items.get(index).get(0)) ;
				wv_option2.setAdapter(mOptions2Adapter);// 设置显示数据
				wv_option3.setAdapter(mOptions3Adapter);// 设置显示数据
				wv_option2.setCurrentItem(0) ;
				wv_option3.setCurrentItem(0) ;
			}
		};
	    wheelListener_option2 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				mOptions3Adapter = new ArrayWheelAdapter(mOptions3Items.get(mCurternItem).get(index)) ;
				wv_option3.setAdapter(mOptions3Adapter);// 设置显示数据
				wv_option3.setCurrentItem(0) ;
			}
		};

//		// 添加联动监听
		if (options2Items != null && linkage)
			wv_option1.setOnItemSelectedListener(wheelListener_option1);
		if (options3Items != null && linkage)
			wv_option2.setOnItemSelectedListener(wheelListener_option2);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		if (label1 != null)
			wv_option1.setLabel(label1);
		if (label2 != null)
			wv_option2.setLabel(label2);
		if (label3 != null)
			wv_option3.setLabel(label3);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wv_option1.setCyclic(cyclic);
		wv_option2.setCyclic(cyclic);
		wv_option3.setCyclic(cyclic);
	}

	/**
	 * 分别设置第一二三级是否循环滚动
	 *
	 * @param cyclic1,cyclic2,cyclic3
	 */
	public void setCyclic(boolean cyclic1,boolean cyclic2,boolean cyclic3) {
        wv_option1.setCyclic(cyclic1);
        wv_option2.setCyclic(cyclic2);
        wv_option3.setCyclic(cyclic3);
	}
    /**
     * 设置第二级是否循环滚动
     *
     * @param cyclic
     */
    public void setOption2Cyclic(boolean cyclic) {
        wv_option2.setCyclic(cyclic);
    }
/**
     * 设置第三级是否循环滚动
     *
     * @param cyclic
     */
    public void setOption3Cyclic(boolean cyclic) {
        wv_option3.setCyclic(cyclic);
    }

	/**
	 * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
	 * 
	 * @return
	 */
	public int[] getCurrentItems() {
		int[] currentItems = new int[3];
		currentItems[0] = wv_option1.getCurrentItem();
		currentItems[1] = wv_option2.getCurrentItem();
		currentItems[2] = wv_option3.getCurrentItem();
		return currentItems;
	}

	public void setCurrentItems(int option1, int option2, int option3) {
        if(linkage){
            itemSelected(option1-1, option2-1, option3-1);
        }
        wv_option1.setCurrentItem(option1-1);
        wv_option2.setCurrentItem(option2-1);
        wv_option3.setCurrentItem(option3-1);
	}

	private void itemSelected(int opt1Select, int opt2Select, int opt3Select) {
		if (mOptions2Items != null) {
			wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items
					.get(opt1Select)));
			wv_option2.setCurrentItem(opt2Select);
		}
		if (mOptions3Items != null) {
			wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items
					.get(opt1Select).get(
							opt2Select)));
			wv_option3.setCurrentItem(opt3Select);
		}
	}


}
