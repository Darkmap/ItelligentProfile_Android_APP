package com.daocaowu.itelligentprofile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.GPSMapActivity;
import com.daocaowu.itelligentprofile.activity.LoadOfflineMapActivity;
import com.daocaowu.itelligentprofile.activity.LoadOfflineMapActivity.MYMKOfflineMapListener;


public class OffMapCityAdapter extends BaseAdapter{

	
	private List<MKOLSearchRecord> listData;
	private LayoutInflater inflater;
	
	private MKOfflineMap mMKOfflineMap;  
//	private TextView tv_MKOLSearchRecordItem;
	
//	private static Holder tempHolder;
	private void initMKOfflineMap() {
		mMKOfflineMap = LoadOfflineMapActivity.mMKOfflineMap;
//		mMKOfflineMap = new MKOfflineMap(); 
//		mMKOfflineMap.init(GPSMapActivity.mStaticMapController, new MYMKOfflineMapListener());
	}
	public OffMapCityAdapter() {
		initMKOfflineMap();
		// TODO Auto-generated constructor stub
	}
	public OffMapCityAdapter(List<MKOLSearchRecord> list, Context context) {
		setListData(list);
		initMKOfflineMap();
		inflater = LayoutInflater.from(context);
	}
	public void setListData(List<MKOLSearchRecord> list){
		if (list != null) {
			listData = list;
		}else {
			listData = new ArrayList<MKOLSearchRecord>();
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

	static int isLoading = 0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//TODO
//		final int positionOperate  = position;
		Holder holder = null;
		
		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.city_item, null);// ���ò����ļ�
//			mConvertView = convertView;
			holder.cityNameTV = (TextView) convertView.findViewById(R.id.cityNameTV);
			holder.cityDataSizeTV = (TextView) convertView.findViewById(R.id.cityDataSizeTV);
			holder.hasNewTV = (TextView) convertView.findViewById(R.id.hasNewTV);
			holder.loadTV = (TextView) convertView.findViewById(R.id.loadTV);

//			holder.cityRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.cityRelativeLayout);
//			holder.cityRelativeLayout.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Log.v("cityName"+listData.get(usePosition).cityName, "cityID"+listData.get(usePosition).cityID);
//					Log.v("Start","  `");
//					if(GPSMapActivity.isOneLoading==0)
//					{
//						Log.v("Start","````");
//						GPSMapActivity.mMKOfflineMap.start(listData.get(usePosition).cityID);
//						GPSMapActivity.isOneLoading =1;
////						TextView hasNewTV = (TextView) mConvertView.findViewById(R.id.hasNewTV);
////						hasNewTV.setText("����ing");
//					}
//					else if(GPSMapActivity.isOneLoading ==1&&GPSMapActivity.nowCityId==listData.get(usePosition).cityID)
//					{
//						Log.v("pause","```");
//						GPSMapActivity.mMKOfflineMap.pause(listData.get(usePosition).cityID);
////						holder.hasNewTV.setText("��ͣing"+myListItem.cityID+" "+GPSMapActivity.nowCityId);
////						TextView hasNewTV = (TextView) mConvertView.findViewById(R.id.hasNewTV);
////						hasNewTV.setText("��ͣing"+myListItem.cityID+" "+GPSMapActivity.nowCityId);
//						GPSMapActivity.isOneLoading = 0;
//					}
//				}
//			});
			
				
//		TODO		holder.hasNewTV.setText(myListItem.);
			
			convertView.setTag(holder);// ������²�����view��������tag
		} 
		else
		{
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}
		MKOLSearchRecord myListItem = listData.get(position);
		if (myListItem != null) {
			// ����Ӧ�Ŀؼ���ֵ
			holder.cityNameTV.setText(myListItem.cityName);
			holder.cityDataSizeTV.setText((int)(myListItem.size/10000)/100.0+"MB");
			MKOLUpdateElement updateElement = mMKOfflineMap.getUpdateInfo(myListItem.cityID);
			if ( updateElement!= null)
			{
				switch (updateElement.status) {
				case MKOLUpdateElement.SUSPENDED:
					holder.loadTV.setText("����ͣ");
					break;
				case MKOLUpdateElement.FINISHED:
					holder.loadTV.setText("�����");
					break;
				case MKOLUpdateElement.DOWNLOADING:
					if (updateElement.ratio > 0)
						holder.loadTV.setText("������" + updateElement.ratio + "%");
					else
						holder.loadTV.setText("��������");
					break;
				case MKOLUpdateElement.WAITING:
					if (updateElement.ratio > 0)
						holder.loadTV.setText("������" + updateElement.ratio + "%");
					else
					    holder.loadTV.setText("�ȴ���");
					break;
				case MKOLUpdateElement.eOLDSIOError:
					holder.loadTV.setText("��д�쳣");
					break;
				case MKOLUpdateElement.eOLDSMd5Error:
					holder.loadTV.setText("Ч��ʧ��");
					break;
				case MKOLUpdateElement.eOLDSMissData:
					holder.loadTV.setText("���ݶ�ʧ");
					break;
				case MKOLUpdateElement.eOLDSWifiError:
					holder.loadTV.setText("�����쳣");
					break;
				case MKOLUpdateElement.eOLDSNetError:
					holder.loadTV.setText("wifi�����쳣");
					break;
				default:
					holder.loadTV.setText("error");
					break;
				}
//				if(updateElement.status == MKOLUpdateElement.DOWNLOADING ||updateElement.status ==  MKOLUpdateElement.WAITING)
//					if(updateElement.ratio>0)
//						holder.loadTV.setText("������"+updateElement.ratio+"%");
			}
			else {
				holder.loadTV.setText("��");
			}
			
			if(updateElement!=null&&updateElement.update)
				holder.hasNewTV.setText("�и��£�");
			else
				holder.hasNewTV.setText("");
		}

		
		return convertView;
	}
	
	
	final class Holder {
		public TextView cityNameTV,cityDataSizeTV, hasNewTV,loadTV;
		public ImageButton downloadButton;
		public RelativeLayout cityRelativeLayout;
	}
	
	/**
     * ����б���
     * @param item
     */
    public void addItem(MKOLSearchRecord item) {
        this.listData.add(item);
    }
    
	
    
}