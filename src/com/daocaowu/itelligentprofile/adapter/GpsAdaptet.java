package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.bean.Location;

public class GpsAdaptet extends BaseAdapter{

	private List<Location> listData;
	private LayoutInflater inflater;
	static public int counter = 0;
	
	public GpsAdaptet(List<Location> list, Context context) {
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
		Location myListItem = listData.get(position);
		Holder holder = new Holder();
		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.gpsitem, null);// ���ò����ļ�
			holder.name = (TextView) convertView.findViewById(R.id.gpsname);
			holder.lalo = (TextView) convertView.findViewById(R.id.latiandlongi);
			holder.profile = (TextView) convertView.findViewById(R.id.radius);
			convertView.setTag(holder);// ������²�����view��������tag
		} else {
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}

		if (myListItem != null) {
			// ����Ӧ�Ŀؼ���ֵ
			holder.name.setText(myListItem.getLocationName()+"("+myListItem.getAddress()+")");
			holder.lalo.setText(myListItem.getLatitude()+"||"+myListItem.getLongitude());
			holder.profile.setText(myListItem.getProfileName());
		}
		return convertView;
	}

	
	final class Holder {
		public TextView name,lalo,profile;
	}
	
	
	/**
     * ����б���
     * @param item
     */
    public void addItem(Location item) {
        this.listData.add(item);
    }

}
