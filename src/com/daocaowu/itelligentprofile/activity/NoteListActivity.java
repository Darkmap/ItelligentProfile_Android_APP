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
		//TODO 界面写好即可
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
		 * 保存笔记到数据库中
		 */
		public void saveNote(){
			localService = new NoteService();
			localService.add(note);
		}
		
		/**
		 * 从数据库中获取单个笔记信息
		 * @param objectId noteID
		 * @return 单个笔记实体
		 */
		public Note checkSingleNote(int objectId){
			return (Note) localService.check(objectId);
		}
		
		/**
		 * 从数据库中获取笔记列表数据
		 * @return
		 */
		public List<Note> checkNotes(){
			return localService.check();
		}
		
		/**
		 * 删除数据库中单个Note的数据
		 * @param objectId
		 */
		public void deleteNote(int objectId){
			localService = new NoteService();
			localService.delete(objectId);
		}
		
		/**
		 * 设置要操作的实体
		 * @param note
		 */
		public void setNote(Note note) {
			this.note = note;
		}
		
	}
	
}