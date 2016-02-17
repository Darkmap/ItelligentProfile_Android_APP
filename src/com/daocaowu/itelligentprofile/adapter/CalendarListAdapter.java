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
import com.daocaowu.itelligentprofile.bean.Task;

public class CalendarListAdapter extends BaseAdapter{

	private List<Task> listData;
	private LayoutInflater inflater;
	static public int counter = 0;
	
	public CalendarListAdapter(List<Task> list, Context context) {
		setListData(list);
		inflater = LayoutInflater.from(context);
	}
	
	private void setListData(List<Task> list){
		if (list != null) {
			listData = list;
		}else {
			listData = new ArrayList<Task>();
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
		Task myListItem = listData.get(position);
		Holder holder = null;
		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.carlenderitem, null);// ���ò����ļ�
			holder.taskName = (TextView) convertView.findViewById(R.id.calenderitemname);
			holder.profile = (TextView) convertView.findViewById(R.id.profile);
			holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.endTime = (TextView) convertView.findViewById(R.id.endTime);
			convertView.setTag(holder);// ������²�����view��������tag
		} else {
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}

		if (myListItem != null) {
			// ����Ӧ�Ŀؼ���ֵ
			holder.profile.setText(myListItem.getProfileName());
			holder.taskName.setText(myListItem.getTaskName());
			holder.startTime.setText(myListItem.getStartTime());
			holder.endTime.setText(myListItem.getEndTime());
		}
		return convertView;
	}

	
	final class Holder {
		public TextView taskName,startTime, endTime,profile;
	}
	
	
	/**
     * ����б���
     * @param item
     */
    public void addItem(Task item) {
        this.listData.add(item);
    }
    
	
}

