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
		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.wifiitem, null);// ���ò����ļ�
			holder.wifiname = (TextView) convertView.findViewById(R.id.wifiname);
			holder.profilename = (TextView) convertView.findViewById(R.id.profilename);
			holder.bssid = (TextView) convertView.findViewById(R.id.bssid);
			convertView.setTag(holder);// ������²�����view��������tag
		} else {
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}

		Log.v("adapter", "�ж�֮ǰ");
		if (myListItem != null) {
			// ����Ӧ�Ŀؼ���ֵ
			holder.wifiname.setText(myListItem.getSsid());
			Log.v("adapter", myListItem.getSsid());
			holder.profilename.setText(myListItem.getProfileName());
			holder.bssid.setText("���ȼ�:"+myListItem.getPriorityLevel()+"");
		}
		return convertView;
	}

	
	final class Holder {
		public TextView wifiname,bssid,profilename;
	}
	
	
	/**
     * ����б���
     * @param item
     */
    public void addItem(WifiLocation item) {
        this.listData.add(item);
    }

}
