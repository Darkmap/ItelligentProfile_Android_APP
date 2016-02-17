package com.daocaowu.itelligentprofile.activity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.adapter.OffMapCityAdapter;
import com.daocaowu.itelligentprofile.adapter.UpdateInfoCityAdapter;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;
import com.daocaowu.itelligentprofile.utils.SystemUtil;

//TODO ��⵱ǰ����״̬
public class LoadOfflineMapActivity extends Activity {

	public static MKOfflineMap mMKOfflineMap = null;
	public MYMKOfflineMapListener myMKOfflineMapListener = null;
	private final static String TAG = "MainActivty";
	EditText searchCityEdit ;//������
	TextView loadInfoTextView;//������Ϣ��ʾ
	RelativeLayout citySelectLayout ;//�л���
	TextView citySelectTextView ;//�л���Ϣ��ʾ
	ListView cityList_view;//�����б�
	OffMapCityAdapter offlineMapCityadapter;//���ߵ�ͼ����
	UpdateInfoCityAdapter updateCityadapter;//
	ArrayList<MKOLSearchRecord> offlineCityList,searchOfflineCityList ;//���ߵ�ͼ �б�
	ArrayList<MKOLUpdateElement> updateCityList,searchAllUpdateCityList ;//�����غۼ��ĵ�ͼ�б�
	
	//��¼��ǰ��listview״̬��
	private final int LOOKloadLIST= 1;//�����б�״̬
	private final int LOOKallLIST= 0;//�����б�״̬
	private int listViewStatus = LOOKallLIST;
	private boolean isLoading = false;
	
	private ArrayList<MKOLSearchRecord> offlineProvinceList;//ʡ���б�
	
	PendingIntent m_restartIntent;
	String RESTART_INTENT_KEY = "restart";
	int CRASHED_CODE = -1;
	private UncaughtExceptionHandler m_handler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Log.d(TAG, "uncaught exception is catched!");
			AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
					m_restartIntent);
			System.exit(2);
		}
	};
	
	private void setUnCatchException(){
		Intent intent = getIntent();
		int code = intent.getIntExtra(RESTART_INTENT_KEY, 0);
		if (CRASHED_CODE == code) {
			/** You can do something here. */
			Log.d(TAG, "So sorry that the application crashed.");
		}
		intent.putExtra(RESTART_INTENT_KEY, CRASHED_CODE);
		m_restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Thread.setDefaultUncaughtExceptionHandler(m_handler);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_offline_map);
		// ����activityʱ���Զ��������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setUnCatchException();
		//��������Ļ�ȡ
        searchCityEdit = (EditText)findViewById(R.id.searchCityEdit);
		loadInfoTextView = (TextView)findViewById(R.id.loadInfoTextView);
		citySelectLayout = (RelativeLayout)findViewById(R.id.citySelectLayout);
		citySelectTextView = (TextView)findViewById(R.id.citySelectTextView);
		cityList_view = (ListView) findViewById(R.id.cityList_view);
        
		initMKOfflineMap();
        //��ȡ֧�����ߵ�ͼ�����б�
		offlineCityList = getOfflineCityList();
        if (offlineCityList == null) {
			offlineCityList = new ArrayList<MKOLSearchRecord>();
		}
        if (offlineMapCityadapter == null) {
        	//��ʼ�� ���ߵ�ͼ ������        cityList_view�󶨸�������
            offlineMapCityadapter = new OffMapCityAdapter(offlineCityList, LoadOfflineMapActivity.this);
    		cityList_view.setAdapter(offlineMapCityadapter);
		}else {
			offlineMapCityadapter.notifyDataSetChanged();
		}
        
        
        //��ȡ���������ߵ�ͼ������Ϣ
		updateCityList = getUpdateCityList();
        if (updateCityList == null) {
        	updateCityList = new ArrayList<MKOLUpdateElement>();
        }
        if (updateCityadapter == null) {
        	//��ʼ�� ���ߵ�ͼ������Ϣ ������ 
            updateCityadapter = new UpdateInfoCityAdapter(updateCityList, LoadOfflineMapActivity.this);
		}else {
			updateCityadapter.notifyDataSetChanged();
		}
        
        //��ȡ��ʡ�����ߵ�ͼ
        offlineProvinceList = getOfflineProvinceList();
        
      //����searchCityEdit�ı仯  �б仯��ʱʱ����
		searchCityEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(listViewStatus == LOOKallLIST)
					searchOfflineCity(s.toString());
				else
					searchUpdateInfoCity(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		//����citySelectLayout�����¾ͽ��г����б��л�
		citySelectLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listViewStatus == LOOKallLIST)//�������ʾ���г���״̬
				{
					citySelectTextView.setText("�鿴���г����б�");
					listViewStatus = LOOKloadLIST;
					cityList_view.setAdapter(updateCityadapter);
					refUpdateCityList();
					searchUpdateInfoCity(searchCityEdit.getText().toString());
				}
				else
				{
					citySelectTextView.setText("�鿴�����б�");
					listViewStatus = LOOKallLIST;
					cityList_view.setAdapter(offlineMapCityadapter);
					searchOfflineCity(searchCityEdit.getText().toString());
				}
			}
		});
		
		//����cityList_view�Ƿ���item��click
		        //����
//		cityList_view.setOnItemLongClickListener(new OnItemLongClickListener() {
//			
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				TextView hasNewTV=(TextView) view.findViewById(R.id.hasNewTV);
//				if(listViewStatus == LOOKloadLIST)//��������ߵ�ͼ������Ϣ�б�
//				{
//					MKOLUpdateElement myListItem = (MKOLUpdateElement) updateCityadapter.getItem(position);
//					removeLoadMap(myListItem.cityID);
////					loadInfoTV.setText("�Ѿ�ɾ�� "+myListItem.cityName);
//					updateCityList.remove(position);
////					searchCityEdit.setText("");
//					refUpdateCityList();
//					Log.v("�Ѿ�ɾ��","```");
//					updateCityadapter.notifyDataSetChanged();
//				}
//				return true;		
//			}
//		});
		       //������
		cityList_view.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stu
				openDialog(position);
			}
		});
	}
	/**
	 *  ��ʼ�� MKOfflineMap
	 */
	private void initMKOfflineMap() {
		if (myMKOfflineMapListener == null) {
			myMKOfflineMapListener = new MYMKOfflineMapListener();
		}
		if (mMKOfflineMap == null) {
			mMKOfflineMap = new MKOfflineMap();
//			mMKOfflineMap.init(GPSMapActivity.mMapController,
//					myMKOfflineMapListener);
		}
		mMKOfflineMap.init(GPSMapActivity.mMapController,
				myMKOfflineMapListener);
	}

	/**
	 * �������ߵ�ͼ���У�����ʡ�ݺ�ȫ�����б�
	 */
	public ArrayList<MKOLSearchRecord> getOfflineCityAndProvinceList() {
		ArrayList<MKOLSearchRecord> list = new ArrayList<MKOLSearchRecord>();
		ArrayList<MKOLSearchRecord> ocl = mMKOfflineMap.getOfflineCityList();
		if(ocl==null)
			return list;
		for (MKOLSearchRecord r : mMKOfflineMap.getOfflineCityList())
		{
			list.add(r);
			if (r.cityType == 1)
				for (MKOLSearchRecord o : r.childCities)
				{
					list.add(o);
					Log.v(o.cityName, o.cityID+" ");
				}
		}
		return list;
	}
	/**
	 * �������ߵ�ͼ��ֻ��ʡ�ݣ��б�
	 */
	public ArrayList<MKOLSearchRecord> getOfflineProvinceList() {
		ArrayList<MKOLSearchRecord> list = new ArrayList<MKOLSearchRecord>();
		ArrayList<MKOLSearchRecord> ocl = mMKOfflineMap.getOfflineCityList();
		if(ocl==null)
			return list;
		for (MKOLSearchRecord r : mMKOfflineMap.getOfflineCityList())
		{
			if (r.cityType == 1)
				list.add(r);
		}
		return list;
	}
	
	/**
	 * �������ߵ�ͼ���У�û�а���ʡ�ݣ���ȫ�����б�
	 */
	public ArrayList<MKOLSearchRecord> getOfflineCityList() {
		ArrayList<MKOLSearchRecord> list = new ArrayList<MKOLSearchRecord>();
		ArrayList<MKOLSearchRecord> ocl = mMKOfflineMap.getOfflineCityList();
		if(ocl==null)
			return list;
		for (MKOLSearchRecord r : ocl)
		{
			if (r.cityType == 1)
				for (MKOLSearchRecord o : r.childCities)
				{
					list.add(o);
//					Log.v(o.cityName, o.cityID+" ");
				}
			else
				list.add(r);
		}
		Log.v("OfflineCityList", "���м������");
		return list;
	}
	
	/**
	 * ���ظ��������ߵ�ͼ������Ϣ
	 */
	public ArrayList<MKOLUpdateElement> getUpdateCityList()
	{
		ArrayList<MKOLUpdateElement> mmm = mMKOfflineMap.getAllUpdateInfo();
		if(mmm==null)
			return new ArrayList<MKOLUpdateElement>();
		for (MKOLUpdateElement m: mmm)
		{
			if(m.size==0&&m.status!=MKOLUpdateElement.DOWNLOADING&&m.status!=MKOLUpdateElement.WAITING)  
				//�����СΪ0 ���߲������ػ��ߵȴ�����״̬    ��ɾ����
				mMKOfflineMap.remove(m.cityID);
		}
		return mMKOfflineMap.getAllUpdateInfo();
	}
	
	/**
	 * �������ߵ�ͼ����
	 */
	public void searchOfflineCity(String city) {
		if(city.equals(""))
		{
			offlineMapCityadapter.setListData(offlineCityList);
		}
		else
		{
			if (searchOfflineCityList == null) {
				searchOfflineCityList = new ArrayList<MKOLSearchRecord>();
			}else {
				searchOfflineCityList.clear();
			}
			if(city.length()<4&&city.length()>1)
			{
				for (MKOLSearchRecord p: offlineProvinceList)
				{
					if( p.cityName.startsWith(city) )
					{
						for (MKOLSearchRecord c: p.childCities)
							searchOfflineCityList.add(c);
						break;//�й�����ʡ�ݵ�ǰ2���ֶ�����ͬ
					}	
				}
			}
			for (MKOLSearchRecord r : offlineCityList) {
				if(r.cityName.startsWith(city))
					searchOfflineCityList.add(r);
			}
			offlineMapCityadapter.setListData(searchOfflineCityList);
		}
		offlineMapCityadapter.notifyDataSetChanged();//ˢ��offlineMapCityadapter������
	}
	
	/**
	 * �������ߵ�ͼ����List
	 */
	public void setOfflineCityListData() {
		offlineMapCityadapter.setListData(offlineCityList);
	}
	public void setOfflineCityListData(ArrayList<MKOLSearchRecord> CityList) {
		offlineMapCityadapter.setListData(CityList);
	}
	/**
	 * �����Ѿ����ص�ͼ����
	 */
	public void searchUpdateInfoCity(String city) {
		if(city.equals(""))
		{
			updateCityadapter.setListData(updateCityList);
		}
		else
		{
			if (searchAllUpdateCityList == null) {
				searchAllUpdateCityList = new ArrayList<MKOLUpdateElement>();
			}else {
				searchAllUpdateCityList.clear();
			}
			if(city.length()<4&&city.length()>1)
			{
				MKOLUpdateElement upEle;
				for (MKOLSearchRecord p: offlineProvinceList)
				{
					if( p.cityName.startsWith(city) )
					{
						for (MKOLSearchRecord c: p.childCities)
						{
							upEle=mMKOfflineMap.getUpdateInfo(c.cityID);
							if(upEle !=null)
								searchAllUpdateCityList.add(upEle);
						}
						break;//�й�����ʡ�ݵ�ǰ2���ֶ�����ͬ
					}	
				}
			}
			for (MKOLUpdateElement r : updateCityList) {
				if(r.cityName.startsWith(city))
					searchAllUpdateCityList.add(r);
			}
			updateCityadapter.setListData(searchAllUpdateCityList);
		}
		updateCityadapter.notifyDataSetChanged();//ˢ��updateCityadapter������
	}
	
	/**
	 * ˢ���Ѿ����ص�ͼ����
	 */
	public void refUpdateCityList() {
		updateCityList = getUpdateCityList();
		updateCityadapter.setListData(updateCityList);
	}
	/**
	 * �����Ѿ����ص�ͼ����List
	 */
	public void setUpdateInfoCityListData() {
		updateCityadapter.setListData(getUpdateCityList());
	}
	public void setUpdateInfoCityListData(ArrayList<MKOLUpdateElement> CityList) {
		updateCityadapter.setListData(CityList);
	}
	
	/**
	 * ��ʼ�������ߵ�ͼ
	 */
	public void startLoadMap(int cityID) {
		mMKOfflineMap.start(cityID);
	}
	/**
	 * ��ͣ�������ߵ�ͼ
	 */
	public void pauseLoadMap(int cityID) {
		mMKOfflineMap.pause(cityID);
	}
	/**
	 * ɾ�����غõ����ߵ�ͼ
	 */
	public void removeLoadMap(int cityID) {
		mMKOfflineMap.remove(cityID);
	}
	/**
	 * �������غõ����ߵ�ͼ  ���ص������
	 */
	public int scanLoadMap() {
		//true: ����ʧ��ʱ��ɾ�����ߵ�ͼ���ݡ� false: ����ʧ��ʱ�������ߵ�ͼ���ݡ�
		return mMKOfflineMap.scan(false);
	}
	

//	private boolean isLoading = false;//���浱ǰ״̬
	/**
	 * ���ߵ�ͼmMKOfflineMap�ļ�����
	 */
	public class MYMKOfflineMapListener implements MKOfflineMapListener {

		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				isLoading = true;
				Log.v("TYPE_DOWNLOAD_UPDATE", "������");
				// Log.d("OfflineDemo",
				// String.format("cityid:%d update",state));
				MKOLUpdateElement update = mMKOfflineMap.getUpdateInfo(state);
				if(update == null)
					update = new MKOLUpdateElement();
//				Log.v(""+update.status," ```");
				if (update.ratio == 0) {
					loadInfoTextView.setText(String.format("%s : �ȴ���", update.cityName) );
				} else {
					loadInfoTextView.setText(String.format("%s : %d%%", update.cityName, update.ratio));
//					isOneLoading = 1;
				}
//				if(update.ratio==100)
//					isLoading = false;
//				else
//					isLoading = true;
//				nowCityId = update.cityID;
//				Log.v("isOneLoading", ""+isOneLoading);
//				Log.v("update.ratio", ""+update.ratio);
//				Log.v("update.status", ""+update.status);
				if(update.ratio >= 100)
				{
//					isOneLoading = 0;
					loadInfoTextView.setText(String.format("%s : %d%%"+" �Ѿ��������",
							update.cityName, update.ratio));
					isLoading = false;
					Log.v("TYPE_DOWNLOAD_UPDATE", "�������");
//					Log.v("update", ""+mMKOfflineMap.getUpdateInfo(state).update);
					scanLoadMap();
				}
				//�����ݽ���ˢ��
				if(listViewStatus == LOOKallLIST)
				{
					searchOfflineCity(searchCityEdit.getText().toString());
				}
				else
				{
					refUpdateCityList();
					searchUpdateInfoCity(searchCityEdit.getText().toString());			
				}
				switch (update.status) {//�쳣��ɾ����
				case MKOLUpdateElement.eOLDSIOError:
					loadInfoTextView.setText(update.cityName + " : ��д�쳣"+"�Ѿ�ɾ��");
					removeLoadMap(update.cityID);
					break;
				case MKOLUpdateElement.eOLDSMd5Error:
					loadInfoTextView.setText(update.cityName + " : Ч��ʧ��"+"�Ѿ�ɾ��");
					removeLoadMap(update.cityID);
					break;
				case MKOLUpdateElement.eOLDSMissData:
					loadInfoTextView.setText(update.cityName + " : ���ݶ�ʧ"+"�Ѿ�ɾ��");
					removeLoadMap(update.cityID);
					break;
				case MKOLUpdateElement.eOLDSWifiError:
					loadInfoTextView.setText(update.cityName + " : �����쳣");
					break;
				case MKOLUpdateElement.eOLDSNetError:
					loadInfoTextView.setText(update.cityName + " : wifi�����쳣");
					break;
				default:
					break;
				}
//				Log.v("update", ""+mMKOfflineMap.getUpdateInfo(state).update);
			}
			break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				loadInfoTextView.setText("�Ѿ���ɰ�װ");
				Log.v("TYPE_NEW_OFFLINE","�°�װ%d�����ߵ�ͼ"+state);
//				loadInfoTextView.setText(String.format("�°�װ%d�����ߵ�ͼ", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				MKOLUpdateElement e = mMKOfflineMap.getUpdateInfo(state);
				if (e != null) {
					loadInfoTextView.setText(String.format("%s �����ߵ�ͼ����", e.cityName));
					Log.v("TYPE_VER_UPDATE",e.cityName+"�����ߵ�ͼ����");
				}
				break;
			}
		}
	}
	
	/**
	 * ���listview �򿪶Ի���
	 * @param position
	 */
	public void openDialog(int position) {
		AlertDialog.Builder builder = new Builder(LoadOfflineMapActivity.this);
		String message1="",message2="",message3="",queren="ȷ��";
		final MKOLUpdateElement mMKOLUpdateElement;
		final MKOLSearchRecord mMKOLSearchRecord ;
		if(SwitchProfileUtil.getWlanStatus(this)==0)
			message2 = "δ����WLAN";
		if(SystemUtil.isConnectInternet(this)==false)
			message2 = "δ��������";
		if(listViewStatus == LOOKallLIST)
		{
			mMKOLSearchRecord = (MKOLSearchRecord) offlineMapCityadapter.getItem(position);
			Log.v("cityName"+mMKOLSearchRecord.cityName, "cityID"+mMKOLSearchRecord.cityID);
			mMKOLUpdateElement = mMKOfflineMap.getUpdateInfo(mMKOLSearchRecord.cityID);
			Log.v("MKOLUpdateElement",mMKOLUpdateElement+"");
			loadInfoTextView.setText(mMKOLSearchRecord.cityName+"");
			
			
			if(mMKOLUpdateElement == null)
				message1 = "ȷ��������";
			else
				switch(mMKOLUpdateElement.status)
				{
				case MKOLUpdateElement.SUSPENDED:
					message1 = "ȷ������������";
					break;
				case MKOLUpdateElement.DOWNLOADING:
					message1 = "ȷ����ͣ��";
					break;
				case MKOLUpdateElement.WAITING:
					message1 = "ȷ����ͣ��";
					break;
				case MKOLUpdateElement.FINISHED:
					message1 = "�Ѿ���� ��������";
					break;
				default:
					message1 = "error";
					break;
				
				}
		}
		else
		{
			message1 = "ȷ��ɾ����";
			mMKOLUpdateElement = (MKOLUpdateElement) updateCityadapter.getItem(position);
			mMKOLSearchRecord = null;
			switch(mMKOLUpdateElement.status)
			{
			case MKOLUpdateElement.SUSPENDED:
				message1 = "ɾ��or������";
				message3 = "����";
				queren="ɾ��";
				break;
			case MKOLUpdateElement.DOWNLOADING:
				message1 = "ɾ��or��ͣ��";
				message3 = "��ͣ";
				queren="ɾ��";
				break;
			case MKOLUpdateElement.WAITING:
				message1 = "ɾ��or��ͣ��";
				message3 = "��ͣ";
				queren="ɾ��";
				break;
			}
		}
		
		if(message3.length()>0)
			builder.setNeutralButton(message3, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(mMKOLUpdateElement.status)
					{
					case MKOLUpdateElement.SUSPENDED:
						mMKOfflineMap.start(mMKOLUpdateElement.cityID);
						break;
					case MKOLUpdateElement.DOWNLOADING:
						loadInfoTextView.setText(loadInfoTextView.getText() + " ����ͣ");
						mMKOfflineMap.pause(mMKOLUpdateElement.cityID);
						break;
					case MKOLUpdateElement.WAITING:
						loadInfoTextView.setText(loadInfoTextView.getText() + " ����ͣ");
						mMKOfflineMap.pause(mMKOLUpdateElement.cityID);
						break;
					}
					offlineMapCityadapter.notifyDataSetChanged();
					refUpdateCityList();
					updateCityadapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			});
		
		builder.setMessage(message1);
		if(message2.length()>0)
		{
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle(message2);
		}
		builder.setPositiveButton(queren, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(listViewStatus == LOOKallLIST)//��������ߵ�ͼ�б�
				{
					
					if(mMKOLUpdateElement == null || mMKOLUpdateElement.status == MKOLUpdateElement.SUSPENDED)//��δ���ع�  �Ϳ�ʼ����
					{
						Log.v("status","null or SUSPENDED");
						mMKOfflineMap.start(mMKOLSearchRecord.cityID);
					}
					else if(mMKOLUpdateElement.status == MKOLUpdateElement.DOWNLOADING)//������
					{
						loadInfoTextView.setText(loadInfoTextView.getText() + " ����ͣ");
						Log.v("status","DOWNLOADING");
						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
					}
					else if(mMKOLUpdateElement.status == MKOLUpdateElement.WAITING)//�ȴ���
					{
						loadInfoTextView.setText(loadInfoTextView.getText() + " ����ͣ");
						Log.v("status","WAITING");
						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
					}
					else if(mMKOLUpdateElement.status == MKOLUpdateElement.FINISHED)//�������
					{
						loadInfoTextView.setText(loadInfoTextView.getText() + " �����");
						Log.v("status","FINISHED");
					}
					else
					{
						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
						Log.v("status",""+mMKOLUpdateElement.status);
//						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
					}
				}
				else//��������ߵ�ͼ�б�
				{
					removeLoadMap(mMKOLUpdateElement.cityID);
					loadInfoTextView.setText(mMKOLUpdateElement.cityName + " : ��ɾ��");
//					updateCityList.remove(position);
//					searchCityEdit.setText("");
					Log.v("��ɾ��","```");
				}
				offlineMapCityadapter.notifyDataSetChanged();
				refUpdateCityList();
				updateCityadapter.notifyDataSetChanged();
				dialog.dismiss();
//				LoadOfflineMapActivity.this.finish();
			}
		});

		builder.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
	
	/**
	 * ��ط��ؼ�
	 * @param position
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(isLoading){
				AlertDialog.Builder build=new AlertDialog.Builder(this);
				build.setTitle("�˳�����ͣ����")
				        .setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("ȷ��Ҫ�˳���")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								finish();
							}
						})
						.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						})
						.show();
			}
			else
				finish();
			break;
		default:
			break;
		}
		return false;
		//return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
//		mOffline.pause(cityid);
		// mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mMKOfflineMap != null) {
			mMKOfflineMap.destroy();
		}
//		mMapView.destroy();
//		DemoApplication app = (DemoApplication) this.getApplication();
//		if (app.mBMapManager != null) {
//			app.mBMapManager.destroy();
//			app.mBMapManager = null;
//		}
		super.onDestroy();
	}
	
	

}
