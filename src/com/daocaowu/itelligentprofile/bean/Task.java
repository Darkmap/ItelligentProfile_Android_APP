package com.daocaowu.itelligentprofile.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Task implements Parcelable{

	private int taskId;             //任务Id
	private String taskName;        //任务名称
	private String title;           //任务标题
	private String startTime;       //开始时间
	private String endTime;         //结束时间
	private long remindTime;         //提醒时间
	private String durationTime;    //持续时间
	private String content;         //任务内容
	private String settingType;     //设置类型??
	private int userId;             //用户Id
	private int profileId;          //情景模式Id
	private String profileName;     //情景模式名称
	private int dayofWeek;          //一周的哪一天
	private int noteId;             //笔记Id
	private int enable = 1;

	
	public boolean duplicate(Task task){
		if(task.getDayofWeek()==this.getDayofWeek()){
			if(task.getStartTime().compareTo(this.getStartTime())<=0){
				if(task.getEndTime().compareTo(this.getStartTime())>=0)
					return true;
			}
			else if(task.getStartTime().compareTo(this.getStartTime())>0){
				if(task.getStartTime().compareTo(this.getEndTime())<=0)
					return true;
			}
		}
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	/**自动生成的：构造、get set函数        **/
	public Task() {
		super();
	}
	public Task(Task t) {
		super();
		this.taskId = t.getTaskId();
		this.taskName = t.getTaskName();
		this.title = t.getTitle();
		this.startTime = t.getStartTime();
		this.endTime = t.getEndTime();
		this.remindTime = t.getRemindTime();
		this.durationTime = t.getDurationTime();
		this.content = t.getContent();
		this.settingType = t.getSettingType();
		this.userId = t.getUserId();
		this.profileId = t.getProfileId();
		this.profileName = t.getProfileName();
		this.dayofWeek = t.getDayofWeek();
		this.noteId = t.getNoteId();
		this.enable = t.enable;
	}
	
	
	
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
		@Override
		public Task createFromParcel(Parcel p) {
			return new Task(p);
		}

		@Override
		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
    
    
	public Task(Parcel t){
		this.taskId = t.readInt();
		this.taskName = t.readString();
		this.title = t.readString();
		this.startTime = t.readString();
		this.endTime = t.readString();
		this.remindTime = t.readLong();
		this.durationTime = t.readString();
		this.content = t.readString();
		this.settingType = t.readString();
		this.userId = t.readInt();
		this.profileId = t.readInt();
		this.profileName = t.readString();
		this.dayofWeek = t.readInt();
		this.noteId = t.readInt();
		this.enable = t.readInt();
	}
    
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeInt(taskId);
		p.writeString(taskName);
		p.writeString(title);
		p.writeString(startTime);
		p.writeString(endTime);
		p.writeLong(remindTime);
		p.writeString(durationTime);
		p.writeString(content);
		p.writeString(settingType);
		p.writeInt(userId);
		p.writeInt(profileId);
		p.writeString(profileName);
		p.writeInt(dayofWeek);
		p.writeInt(noteId);
		p.writeInt(enable);
	}
	
//	public void writeToParcel(Parcel p){
//		p.writeInt(taskId);
//		p.writeString(taskName);
//		p.writeString(title);
//		p.writeString(startTime);
//		p.writeString(endTime);
//		p.writeLong(remindTime);
//		p.writeString(durationTime);
//		p.writeString(content);
//		p.writeString(settingType);
//		p.writeInt(userId);
//		p.writeInt(profileId);
//		p.writeString(profileName);
//		p.writeInt(dayofWeek);
//		p.writeInt(noteId);
//		p.writeInt(enable);
//	}
	
	
	public int getDayofWeek() {
		return dayofWeek;
	}
	public void setDayofWeek(int dayofWeek) {
		this.dayofWeek = dayofWeek;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public long getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(long remindTime) {
		this.remindTime = remindTime;
	}
	public String getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(String durationTime) {
		this.durationTime = durationTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSettingType() {
		return settingType;
	}
	public void setSettingType(String settingType) {
		this.settingType = settingType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}

}
