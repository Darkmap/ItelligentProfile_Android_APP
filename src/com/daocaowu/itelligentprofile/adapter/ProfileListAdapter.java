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
import com.daocaowu.itelligentprofile.bean.Profile;

public class ProfileListAdapter extends BaseAdapter{

	private List<Profile> listData;
	private LayoutInflater inflater;
	private TextView tv_profileItem;
	
	public ProfileListAdapter(List<Profile> list, Context context) {
		setListData(list);
		inflater = LayoutInflater.from(context);
	}
	
	private void setListData(List<Profile> list){
		if (list != null) {
			listData = list;
		}else {
			listData = new ArrayList<Profile>();
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
		Profile myListItem = listData.get(position);
		Holder holder = new Holder();
		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.profilegriditem, null);// ���ò����ļ�
			holder.profileName = (TextView) convertView.findViewById(R.id.gridname);
			convertView.setTag(holder);// ������²�����view��������tag
		} else {
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}

		if (myListItem != null) {
			// ����Ӧ�Ŀؼ���ֵ
			if(myListItem.getIsdefault()==1)holder.profileName.setText(myListItem.getProfileName()+"(Ĭ��)");
			else holder.profileName.setText(myListItem.getProfileName());
		}
		return convertView;
	}

	
	final class Holder {
		public TextView profileName;
	}
	
	
	/**
     * ����б���
     * @param item
     */
    public void addItem(Profile item) {
        this.listData.add(item);
    }
    
	
}
