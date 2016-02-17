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
		
		Log.v("����List��С", String.valueOf(myList.size()));
		Log.v("myList��С", String.valueOf(this.myList.size()));
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
		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = layoutInflater.inflate(R.layout.griditem, null);// ���ò����ļ�
			holder.name = (TextView) convertView.findViewById(R.id.gridname);
			holder.image = (ImageView) convertView.findViewById(R.id.gridimage);
			convertView.setTag(holder);// ������²�����view��������tag
		} else {
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}

		if (myListItem != null) {
			// ����Ӧ�Ŀؼ���ֵ
			holder.name.setText(myListItem.getItemname());
//			Log.v("����", myListItem.getItemname());
			holder.image.setImageDrawable(context.getResources().getDrawable(
					myListItem.getImageID()));
//			Log.v("ͼƬ", String.valueOf(myListItem.getImageID()));
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
