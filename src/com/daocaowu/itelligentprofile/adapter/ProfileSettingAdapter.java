package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.bean.GridItem;

public class ProfileSettingAdapter extends BaseAdapter {

	private Context context;
	private List<GridItem> myList;
	private LayoutInflater layoutInflater;
	
	public ProfileSettingAdapter(Context context, List<GridItem> myList){
		this.context = context;
		setListData(myList);
		layoutInflater = LayoutInflater.from(context);
		
		Log.v("传入List大小", String.valueOf(myList.size()));
		Log.v("myList大小", String.valueOf(this.myList.size()));
	}
	
	private void setListData(List<GridItem> list){
		if(list != null){
			myList = list;
		}else{
			myList = new ArrayList<GridItem>();
		}
	}
	
	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		if(myList == null) return null;
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridItem myListItem = myList.get(position);
		Holder holder = new Holder();
		if (convertView == null) {// 这样做可以使view循环利用，而不会有多少个item就产生多少个view
			holder = new Holder();
			convertView = layoutInflater.inflate(R.layout.griditem, null);// 引用布局文件
			holder.name = (TextView) convertView.findViewById(R.id.gridname);
			holder.image = (ImageView) convertView.findViewById(R.id.gridimage);
			convertView.setTag(holder);// 如果是新产生的view，则设置tag
		} else {
			holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
		}

		if (myListItem != null) {
			// 对相应的控件赋值
			holder.name.setText(myListItem.getItemname());
//			Log.v("名称", myListItem.getItemname());
			holder.image.setImageDrawable(context.getResources().getDrawable(
					myListItem.getImageID()));
//			Log.v("图片", String.valueOf(myListItem.getImageID()));
		}
		return convertView;
	}
	
	final class Holder {
		public TextView name;
		public ImageView image;
	}
	
	public void addItem(GridItem item) {
        this.myList.add(item);
    }

}
