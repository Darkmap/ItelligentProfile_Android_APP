package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import com.daocaowu.itelligentprofile.bean.Task;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class TaskListAdapter extends BaseAdapter{
	private List<Task> listData;
	private LayoutInflater inflater;
	private TextView tv_taskItem;
	
	public TaskListAdapter(List<Task> list, Context context) {
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
		//TODO  由于还没有做界面，暂时不做，如果想做了，请做完它
		return null;
	}

	/**
     * 添加列表项
     * @param item
     */
    public void addItem(Task item) {
        this.listData.add(item);
    }
    
}
