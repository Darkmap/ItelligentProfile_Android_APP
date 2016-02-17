package com.daocaowu.itelligentprofile.bean;

public class Note {

	private int noteId;             //笔记Id
	private String noteName;        //笔记名称
	private String content;         //笔记内容
	private String record;          //录音路径
	private String recordLength;    //录音长度
	private String recordName;      //录音名称
	private String date;            //笔记日期
	private boolean isComMeg = true; //是否是的
	private int enable = 1; //有效的
	private int  isRecord = 0;
	
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public String getNoteName() {
		return noteName;
	}
	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	public String getRecordName() {
		return recordName;
	}
	
	public String getRecordLength() {
		return recordLength;
	}
	public void setRecordLength(String recordLength) {
		this.recordLength = recordLength;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
    	isComMeg = isComMsg;
    }
    
    public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public int getNoteType() {
		return isRecord;
	}
	public void setNoteType(int isRecord) {
		this.isRecord = isRecord;
	}
	
}
