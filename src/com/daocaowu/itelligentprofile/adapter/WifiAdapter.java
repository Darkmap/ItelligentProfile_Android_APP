package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.bean.WifiLocation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WifiAdapter extends BaseAdapter {

	private List<WifiLocation> listData;
	private LayoutInflater inflater;
	static public int counter = 0;
	
	
	public WifiAdapter(List<WifiLocation> list, Context context) {
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
		WifiLocation myListItem = listData.get(position);
		Holder holder = new Holder();
		if (convertView == null) {// 这样做可以使view循环利用，而不会有多少个item就产生多少个view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.wifiitem, null);// 引用布局文件
			holder.wifiname = (TextView) convertView.findViewById(R.id.wifiname);
			holder.profilename = (TextView) convertView.findViewById(R.id.profilename);
			holder.bssid = (TextView) convertView.findViewById(R.id.bssid);
			convertView.setTag(holder);// 如果是新产生的view，则设置tag
		} else {
			holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
		}

		Log.v("adapter", "判断之前");
		if (myListItem != null) {
			// 对相应的控件赋值
			holder.wifiname.setText(myListItem.getSsid());
			Log.v("adapter", myListItem.getSsid());
			holder.profilename.setText(myListItem.getProfileName());
			holder.bssid.setText("优先级:"+myListItem.getPriorityLevel()+"");
		}
		return convertView;
	}

	
	final class Holder {
		public TextView wifiname,bssid,profilename;
	}
	
	
	/**
     * 添加列表项
     * @param item
     */
    public void addItem(WifiLocation item) {
        this.listData.add(item);
    }

}
