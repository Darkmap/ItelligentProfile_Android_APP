package com.daocaowu.itelligentprofile.service;

import java.util.List;

import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.DBHelper.TYPE;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.NoteColumns;
import com.daocaowu.itelligentprofile.bean.Note;


public class NoteService extends AbstractLocalService{

	@Override
	public int add(Object object) {
		return DBManager.insertNoteIntoDB((Note)object);

	}

	@Override
	public void delete(int objectId) {
		DBManager.delete(TYPE.NOTE, NoteColumns.ID+"=?", new String[]{String.valueOf(objectId)});

	}

	@Override
	public Object check(int objectId) {
		return DBManager.getNote(TYPE.NOTE, null, NoteColumns.ID+"=? AND " + NoteColumns.ENABLE+"=?", new String[]{String.valueOf(objectId), String.valueOf(1)});
		
	}

	@Override
	public List check() {
		return DBManager.getNotes(TYPE.NOTE, null, NoteColumns.ENABLE+"=?", new String[]{String.valueOf(1)});
		
	}

	@Override
	public void update(Object object) {
		DBManager.insertNoteIntoDB((Note) object);

	}
	
}
