package com.daocaowu.itelligentprofile.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.daocaowu.itelligentprofile.action.AbstractAction;
import com.daocaowu.itelligentprofile.adapter.LocationListAdapter;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.LocationService;

public class LocationListActivity extends BaseActivity {

	ListView mListView = null;
	LocationListAdapter mLocationListAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//TODO ����д�ü���
		// setContentView(R.layout.activity_main);
		// mListView = (ListView) findViewById(R.id.listview);
		// mLocationListAdapter = new LocationListAdapter(this,
		// R.layout.activity_location);
		// mListView.setAdapter(mLocationListAdapter);
	}
	
	private class LocationAction extends AbstractAction{

		private AbstractLocalService localService;
		private Location location;
		
		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * ����λ�õ����ݿ���
		 */
		public void saveLocation(){
			localService = new LocationService();
			localService.add(location);
		}
		
		/**
		 * �����ݿ��л�ȡ����λ����Ϣ
		 * @param objectId locationID
		 * @return ����λ��ʵ��
		 */
		public Location checkSingleLocation(int objectId){
			return (Location) localService.check(objectId);
		}
		
		/**
		 * �����ݿ��л�ȡλ���б�����
		 * @return
		 */
		public List<Location> checkLocations(){
			return localService.check();
		}
		
		/**
		 * ɾ�����ݿ��е���Location������
		 * @param objectId
		 */
		public void deleteLocation(int objectId){
			localService = new LocationService();
			localService.delete(objectId);
		}
		
		/**
		 * ����Ҫ������ʵ��
		 * @param location
		 */
		public void setLocation(Location location) {
			this.location = location;
		}
		
	}
	
}