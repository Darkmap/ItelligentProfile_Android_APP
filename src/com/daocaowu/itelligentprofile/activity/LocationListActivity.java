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
		//TODO 界面写好即可
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
		 * 保存位置到数据库中
		 */
		public void saveLocation(){
			localService = new LocationService();
			localService.add(location);
		}
		
		/**
		 * 从数据库中获取单个位置信息
		 * @param objectId locationID
		 * @return 单个位置实体
		 */
		public Location checkSingleLocation(int objectId){
			return (Location) localService.check(objectId);
		}
		
		/**
		 * 从数据库中获取位置列表数据
		 * @return
		 */
		public List<Location> checkLocations(){
			return localService.check();
		}
		
		/**
		 * 删除数据库中单个Location的数据
		 * @param objectId
		 */
		public void deleteLocation(int objectId){
			localService = new LocationService();
			localService.delete(objectId);
		}
		
		/**
		 * 设置要操作的实体
		 * @param location
		 */
		public void setLocation(Location location) {
			this.location = location;
		}
		
	}
	
}