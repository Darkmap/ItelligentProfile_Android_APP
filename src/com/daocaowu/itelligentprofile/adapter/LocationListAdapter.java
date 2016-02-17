package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.bean.Location;

public class LocationListAdapter extends BaseAdapter{

	public LocationListAdapter() {
		// TODO Auto-generated constructor stub
	}private List<Location> listData;
	private LayoutInflater inflater;
	private TextView tv_LocationItem;
	
	public LocationListAdapter(List<Location> list, Context context) {
		setListData(list);
		inflater = LayoutInflater.from(context);
	}
	
	private void setListData(List<Location> list){
		if (list != null) {
			listData = list;
		}else {
			listData = new ArrayList<Location>();
		}
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//TODO  由于还没有做界面，暂时不做，如果想做了，请做完它
		return null;
	}


	/**
     * 添加列表项
     * @param item
     */
    public void addItem(Location item) {
        this.listData.add(item);
    }
    
}
