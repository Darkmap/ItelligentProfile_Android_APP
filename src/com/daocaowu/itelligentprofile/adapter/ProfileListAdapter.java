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
		if (convertView == null) {// 这样做可以使view循环利用，而不会有多少个item就产生多少个view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.profilegriditem, null);// 引用布局文件
			holder.profileName = (TextView) convertView.findViewById(R.id.gridname);
			convertView.setTag(holder);// 如果是新产生的view，则设置tag
		} else {
			holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
		}

		if (myListItem != null) {
			// 对相应的控件赋值
			if(myListItem.getIsdefault()==1)holder.profileName.setText(myListItem.getProfileName()+"(默认)");
			else holder.profileName.setText(myListItem.getProfileName());
		}
		return convertView;
	}

	
	final class Holder {
		public TextView profileName;
	}
	
	
	/**
     * 添加列表项
     * @param item
     */
    public void addItem(Profile item) {
        this.listData.add(item);
    }
    
	
}
