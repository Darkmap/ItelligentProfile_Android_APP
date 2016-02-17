package com.daocaowu.itelligentprofile.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.daocaowu.itelligentprofile.action.AbstractAction;
import com.daocaowu.itelligentprofile.adapter.TaskListAdapter;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.TaskService;

public class TaskListActivity extends BaseActivity {

	ListView mListView = null;
	TaskListAdapter mTaskListAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//TODO ����д�ü���
		// setContentView(R.layout.activity_main);
		// mListView = (ListView) findViewById(R.id.listview);
		// mTaskListAdapter = new TaskListAdapter(this,
		// R.layout.activity_task);
		// mListView.setAdapter(mTaskListAdapter);
	}
	
	private class TaskAction extends AbstractAction{

		private AbstractLocalService localService;
		private Task task;
		
		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * �����������ݿ���
		 */
		public void saveTask(){
			localService = new TaskService(TaskListActivity.this);
			localService.add(task);
		}
		
		/**
		 * �����ݿ��л�ȡ����������Ϣ
		 * @param objectId taskID
		 * @return ��������ʵ��
		 */
		public Task checkSingleTask(int objectId){
			return (Task) localService.check(objectId);
		}
		
		/**
		 * �����ݿ��л�ȡ�����б�����
		 * @return
		 */
		public List<Task> checkTasks(){
			return localService.check();
		}
		
		/**
		 * ɾ�����ݿ��е���Task������
		 * @param objectId
		 */
		public void deleteTask(int objectId){
			localService = new TaskService(TaskListActivity.this);
			localService.delete(objectId);
		}
		
		/**
		 * ����Ҫ������ʵ��
		 * @param task
		 */
		public void setTask(Task task) {
			this.task = task;
		}
		
	}
	
}