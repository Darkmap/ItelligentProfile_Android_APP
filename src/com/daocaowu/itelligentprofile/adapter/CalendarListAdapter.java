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
		if (convertView == null) {// 这样做可以使view循环利用，而不会有多少个item就产生多少个view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.carlenderitem, null);// 引用布局文件
			holder.taskName = (TextView) convertView.findViewById(R.id.calenderitemname);
			holder.profile = (TextView) convertView.findViewById(R.id.profile);
			holder.startTime = (TextView) convertView.findViewById(R.id.startTime);
			holder.endTime = (TextView) convertView.findViewById(R.id.endTime);
			convertView.setTag(holder);// 如果是新产生的view，则设置tag
		} else {
			holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
		}

		if (myListItem != null) {
			// 对相应的控件赋值
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
     * 添加列表项
     * @param item
     */
    public void addItem(Task item) {
        this.listData.add(item);
    }
    
	
}

