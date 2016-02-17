package com.daocaowu.itelligentprofile.service;

import java.util.List;

import android.content.ContentValues;

import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.DBHelper.TYPE;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.LocationColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.ProfileColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.TaskColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.WifiLocationColumns;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class ProfileService extends AbstractLocalService{

	@Override
	public int add(Object object) {
		if (object != null) {
			if (1==((Profile)object).getIsdefault()) {
				if (DataApplication.defaultTask == null) {
					TaskService.initDefaultTask();
				}
				DataApplication.defaultTask.setProfileId(((Profile)object).getProfileId());
				DataApplication.defaultTask.setProfileName(((Profile)object).getProfileName());
			}
		}
		
		return DBManager.insertProfileIntoDB((Profile)object);
	}

	@Override
	public void delete(int objectId) {
		ContentValues updateCV = new ContentValues();
		updateCV.put(TaskColumns.PROFILE_ID, -1);
		updateCV.put(ProfileColumns.NAME, "");
		DBManager.update(TYPE.TASK, updateCV, TaskColumns.PROFILE_ID+"=?", new String[]{String.valueOf(objectId)});
		DBManager.update(TYPE.WIFILOCATION, updateCV, WifiLocationColumns.PROFILE_ID+"=?", new String[]{String.valueOf(objectId)});
		DBManager.update(TYPE.LOCATION, updateCV, LocationColumns.PROFILE_ID+"=?", new String[]{String.valueOf(objectId)});
		
		
		DBManager.delete(TYPE.PROFILE, ProfileColumns.ID + "=?",
				new String[] { String.valueOf(objectId) });

	}

	@Override
	public Object check(int objectId) {
		return DBManager.getProfile(TYPE.PROFILE, null, ProfileColumns.ID
				+ "=? AND "+ProfileColumns.TYPE+"=?", new String[] { String.valueOf(objectId),"0" });

	}

	@Override
	public List check() {
		return DBManager.getProfiles(TYPE.PROFILE, null, ProfileColumns.TYPE+"=0", null);
		
	}

	@Override
	public void update(Object object) {
		DBManager.insertProfileIntoDB((Profile) object);

	}
	
	public static Profile getPowerSavingProfile(){
		return DBManager.getProfile(TYPE.PROFILE, null, ProfileColumns.TYPE+"=3 AND "+ProfileColumns.IS_DEFAULT+"!=1", null);
	}
	
	/**
	 * 从数据库获得默认情景模式
	 */
	public static Profile getDefaultProfile() {
		Profile profile = DBManager.getProfile(TYPE.PROFILE, null,
				ProfileColumns.IS_DEFAULT + "=?",
				new String[] { String.valueOf(1) });
		if (profile == null) {
			profile = DBManager.getProfile(TYPE.PROFILE, null, null, null);
		}
		return profile;
	}

	/**
	 * 从数据库中获得某个任务的情景模式
	 * @param currentTime
	 * @return
	 */
	public static Profile getProfileByTask(Task task) {
		Profile profile = null;
		if (task != null) {
			profile = DBManager.getProfile(TYPE.PROFILE, null, ProfileColumns.ID+"=?", new String[]{String.valueOf(task.getProfileId())});
		}
		return profile;
	}
	
	/**
	 * 获得情景模式,如果传入的task为defaultTask,返回的是defaultProfile
	 * @param task
	 * @return
	 */
	public static Profile getProfile(Task task){
		if (task.getTaskId() == 0) {
			return getDefaultProfile();
		}else {
			return getProfileByTask(task);
		}
	}

}
