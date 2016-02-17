package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.bean.WifiLocation;

public class WifiLocationListAdapter extends BaseAdapter{

	public WifiLocationListAdapter() {
		// TODO Auto-generated constructor stub
	}private List<WifiLocation> listData;
	private LayoutInflater inflater;
	private TextView tv_WifiLocationItem;
	
	public WifiLocationListAdapter(List<WifiLocation> list, Context context) {
		setListData(list);
		inflater = LayoutInflater.from(context);
	}
	
	private void setListData(List<WifiLocation> list){
		if (list != null) {
			listData = list;
		}else {
			listData = new ArrayList<WifiLocation>();
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
    public void addItem(WifiLocation item) {
        this.listData.add(item);
    }
    
}
