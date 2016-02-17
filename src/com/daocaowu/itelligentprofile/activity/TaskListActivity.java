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
		//TODO 界面写好即可
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
		 * 保存任务到数据库中
		 */
		public void saveTask(){
			localService = new TaskService(TaskListActivity.this);
			localService.add(task);
		}
		
		/**
		 * 从数据库中获取单个任务信息
		 * @param objectId taskID
		 * @return 单个任务实体
		 */
		public Task checkSingleTask(int objectId){
			return (Task) localService.check(objectId);
		}
		
		/**
		 * 从数据库中获取任务列表数据
		 * @return
		 */
		public List<Task> checkTasks(){
			return localService.check();
		}
		
		/**
		 * 删除数据库中单个Task的数据
		 * @param objectId
		 */
		public void deleteTask(int objectId){
			localService = new TaskService(TaskListActivity.this);
			localService.delete(objectId);
		}
		
		/**
		 * 设置要操作的实体
		 * @param task
		 */
		public void setTask(Task task) {
			this.task = task;
		}
		
	}
	
}