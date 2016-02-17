package com.daocaowu.itelligentprofile.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.daocaowu.itelligentprofile.action.AbstractAction;
import com.daocaowu.itelligentprofile.adapter.NoteListAdapter;
import com.daocaowu.itelligentprofile.bean.Note;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.NoteService;


public class NoteListActivity extends BaseActivity {

	ListView mListView = null;
	NoteListAdapter mNoteListAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//TODO ����д�ü���
		// setContentView(R.layout.activity_main);
		// mListView = (ListView) findViewById(R.id.listview);
		// mNoteListAdapter = new NoteListAdapter(this,
		// R.layout.activity_note);
		// mListView.setAdapter(mNoteListAdapter);
	}
	
	
	private class NoteAction extends AbstractAction{

		private AbstractLocalService localService;
		private Note note;
		
		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * ����ʼǵ����ݿ���
		 */
		public void saveNote(){
			localService = new NoteService();
			localService.add(note);
		}
		
		/**
		 * �����ݿ��л�ȡ�����ʼ���Ϣ
		 * @param objectId noteID
		 * @return �����ʼ�ʵ��
		 */
		public Note checkSingleNote(int objectId){
			return (Note) localService.check(objectId);
		}
		
		/**
		 * �����ݿ��л�ȡ�ʼ��б�����
		 * @return
		 */
		public List<Note> checkNotes(){
			return localService.check();
		}
		
		/**
		 * ɾ�����ݿ��е���Note������
		 * @param objectId
		 */
		public void deleteNote(int objectId){
			localService = new NoteService();
			localService.delete(objectId);
		}
		
		/**
		 * ����Ҫ������ʵ��
		 * @param note
		 */
		public void setNote(Note note) {
			this.note = note;
		}
		
	}
	
}