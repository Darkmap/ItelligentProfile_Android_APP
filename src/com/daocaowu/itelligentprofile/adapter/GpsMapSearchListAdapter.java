package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.MKPoiInfo;
import com.daocaowu.itelligentprofile.R;



public class GpsMapSearchListAdapter extends BaseAdapter{

	
	private List<MKPoiInfo> listData;
	private LayoutInflater inflater;
	 

	public GpsMapSearchListAdapter() {
		// TODO Auto-generated constructor stub
	}
	public GpsMapSearchListAdapter(List<MKPoiInfo> list, Context context) {
		setListData(list);
		inflater = LayoutInflater.from(context);
	}
	public void setListData(List<MKPoiInfo> list){
		if (list != null) {
			listData = list;
		}else {
			listData = new ArrayList<MKPoiInfo>();
		}
	}
	
	public void setNull() {
		setListData(null);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		if(listData == null) return null;
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static int isLoading = 0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder = null;
		
		if (convertView == null) {// 这样做可以使view循环利用，而不会有多少个item就产生多少个view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.gpsmapsearch_item, null);// 引用布局文件
			holder.poinameTV = (TextView) convertView.findViewById(R.id.poinameTV);
			holder.poicityaddressTV = (TextView) convertView.findViewById(R.id.poicityaddressTV);
			
			convertView.setTag(holder);// 如果是新产生的view，则设置tag
		} 
		else
		{
			holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
		}
		MKPoiInfo myPoiListItem = listData.get(position);
		if (myPoiListItem != null) {
			// 对相应的控件赋值
			holder.poinameTV.setText(myPoiListItem.name);
			holder.poicityaddressTV.setText(myPoiListItem.city+" "+myPoiListItem.address);
		}
		return convertView;
	}
	
	final class Holder {
		public TextView poinameTV,poicityaddressTV;
		public RelativeLayout searchRelativeLayout;
	}
	
	/**
     * 添加列表项
     * @param item
     */
    public void addItem(MKPoiInfo item) {
        this.listData.add(item);
    }
    
	
    
}