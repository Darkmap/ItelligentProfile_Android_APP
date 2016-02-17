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
		
		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.gpsmapsearch_item, null);// ���ò����ļ�
			holder.poinameTV = (TextView) convertView.findViewById(R.id.poinameTV);
			holder.poicityaddressTV = (TextView) convertView.findViewById(R.id.poicityaddressTV);
			
			convertView.setTag(holder);// ������²�����view��������tag
		} 
		else
		{
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}
		MKPoiInfo myPoiListItem = listData.get(position);
		if (myPoiListItem != null) {
			// ����Ӧ�Ŀؼ���ֵ
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
     * ����б���
     * @param item
     */
    public void addItem(MKPoiInfo item) {
        this.listData.add(item);
    }
    
	
    
}