package com.daocaowu.itelligentprofile.bean;

public class Note {

	private int noteId;             //�ʼ�Id
	private String noteName;        //�ʼ�����
	private String content;         //�ʼ�����
	private String record;          //¼��·��
	private String recordLength;    //¼������
	private String recordName;      //¼������
	private String date;            //�ʼ�����
	private boolean isComMeg = true; //�Ƿ��ǵ�
	private int enable = 1; //��Ч��
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
