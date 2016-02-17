package com.daocaowu.itelligentprofile.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.adapter.WifiAdapter;
import com.daocaowu.itelligentprofile.bean.WifiLocation;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class WifiProfileActivity extends Activity {
	private Button scan;
	private Button getScanResult;
	private TextView scanResult;

	private String mScanResult;
	private WifiAdmin mWifiAdmin;
	
	private ListView wifilist;
	private WifiAdapter wifiadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_profile);
		
		mWifiAdmin = new WifiAdmin(WifiProfileActivity.this);
		init();
		
	}
	
	public void init() {
		scan = (Button) findViewById(R.id.scan);
		getScanResult = (Button) findViewById(R.id.getScanResult);
		
		scan.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWifiAdmin.scan();
			}
		});
		getScanResult.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mScanResult = mWifiAdmin.getScanResult();
				scanResult.setText(mScanResult);
			}
		});
		
		initadapter();
		
	}

	private void initadapter() {
		
		WifiLocation a= new WifiLocation();
		
		a.setBssid("CMCC123");
		a.setProfileName("����ģʽ");
		a.setWifiLocationName("����");
		
		DataApplication.wifilist.add(a);
		
		wifilist = (ListView) findViewById(R.id.wifisetlist);
		wifiadapter = new WifiAdapter(DataApplication.wifilist,WifiProfileActivity.this);
		wifilist.setAdapter(wifiadapter);
		Log.v("initadapter", "ִ����initadapter,��"+DataApplication.wifilist.size()+"��");
	}

}
class WifiAdmin {
	private final static String TAG = "WifiAdmin";
	private StringBuffer mStringBuffer = new StringBuffer();
	private List<ScanResult> listResult;
	private ScanResult mScanResult;
	// ����WifiManager����
	private WifiManager mWifiManager;
	// ����WifiInfo����
	private WifiInfo mWifiInfo;
	// ���������б�
	private List<WifiConfiguration> mWifiConfiguration;
	// ����һ��WifiLock
	WifiLock mWifiLock;

	/**
	 * ���췽��
	 */
	public WifiAdmin(Context context) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * ��Wifi����
	 */
	public void openNetCard() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * �ر�Wifi����
	 */
	public void closeNetCard() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**
	 * ��鵱ǰWifi����״̬
	 */
	public void checkNetCardState() {
		if (mWifiManager.getWifiState() == 0) {
			Log.i(TAG, "�������ڹر�");
		} else if (mWifiManager.getWifiState() == 1) {
			Log.i(TAG, "�����Ѿ��ر�");
		} else if (mWifiManager.getWifiState() == 2) {
			Log.i(TAG, "�������ڴ�");
		} else if (mWifiManager.getWifiState() == 3) {
			Log.i(TAG, "�����Ѿ���");
		} else {
			Log.i(TAG, "---_---��......û�л�ȡ��״̬---_---");
		}
	}

	/**
	 * ɨ���ܱ�����
	 */
	public void scan() {
		mWifiManager.startScan();
		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			Log.i(TAG, "��ǰ��������������磬��鿴ɨ����");
		} else {
			Log.i(TAG, "��ǰ����û����������");
		}
	}

	/**
	 * �õ�ɨ����
	 */
	public String getScanResult() {
		
		Time t=new Time();
		t.setToNow(); // ȡ��ϵͳʱ�䡣
		String timeNow =t.year+"."+t.month+"."+t.monthDay+"."+t.hour+":"+t.minute+":"+t.second;

		// ÿ�ε��ɨ��֮ǰ�����һ�ε�ɨ����
		if (mStringBuffer != null) {
			mStringBuffer = new StringBuffer();
		}
		// ��ʼɨ������
		scan();
//		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			for (int i = 0; i < listResult.size(); i++) {
				mScanResult = listResult.get(i);
				mStringBuffer = mStringBuffer.append("NO.").append(i + 1)
						.append(" :").append(mScanResult.SSID).append("->")
						.append(mScanResult.BSSID).append("->")
						.append(mScanResult.capabilities).append("->")
						.append(mScanResult.frequency).append("->")
						.append(mScanResult.level).append("->")
						.append(mScanResult.describeContents()).append("->")
						.append(timeNow).append("\n\n");
			}
		}
		Log.i(TAG, mStringBuffer.toString());
		return mStringBuffer.toString();
	}

	/**
	 * ����ָ������
	 */
	public void connect() {
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * �Ͽ���ǰ���ӵ�����
	 */
	public void disconnectWifi() {
		int netId = getNetworkId();
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
		mWifiInfo = null;
	}

	/**
	 * ��鵱ǰ����״̬
	 * 
	 * @return String
	 */
	public void checkNetWorkState() {
		if (mWifiInfo != null) {
			Log.i(TAG, "������������");
		} else {
			Log.i(TAG, "�����ѶϿ�");
		}
	}

	/**
	 * �õ����ӵ�ID
	 */
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * �õ�IP��ַ
	 */
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// ����WifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// ����WifiLock
	public void releaseWifiLock() {
		// �ж�ʱ������
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// ����һ��WifiLock
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// �õ����úõ�����
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	// ָ�����úõ������������
	public void connectConfiguration(int index) {
		// �����������úõ�������������
		if (index >= mWifiConfiguration.size()) {
			return;
		}
		// �������úõ�ָ��ID������
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
	}

	// �õ�MAC��ַ
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// �õ�������BSSID
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// �õ�WifiInfo��������Ϣ��
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	// ���һ�����粢����
	public int addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(mWifiConfiguration.get(3));
		mWifiManager.enableNetwork(wcgID, true);
		return wcgID;
	}
	
	
}
