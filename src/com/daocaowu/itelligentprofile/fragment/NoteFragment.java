package com.daocaowu.itelligentprofile.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.adapter.NoteListAdapter;
import com.daocaowu.itelligentprofile.bean.Note;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.NoteService;
import com.daocaowu.itelligentprofile.utils.ToastFactory;

public class NoteFragment extends Fragment implements OnClickListener {

	MainPad mainPadActivity = null;
	View contentView = null;
	AbstractLocalService noteService = null;
	private Button mBtnSend;
	private Button mBtnRecord;
	private EditText mEditTextContent;
	private ListView mListView;
	private ImageButton imgRecord1;
	private ImageButton imgRecord2;
	private File myPlayFile;
	private NoteListAdapter mAdapter;
	private List<Note> mNoteList;
	private String strTempFile = "kaixuan";
	private File myRecAudioFile;
	private File myRecAudioDir;
	private MediaRecorder mMediaRecorder01;
	private boolean sdCardExit;
	private boolean isStopRecord;
	private boolean isPlaying;
	private int duration;
	private MediaPlayer mp;
//	private ArrayList<String> recordFiles;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainPadActivity = (MainPad) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.layout_main, container,
				false);
		initData();
		initViews();
		return contentView;
	}
	
	private void initData(){
		if (noteService == null) {
			noteService = new NoteService();
		}
		mNoteList = noteService.check();
	}

	private void initViews() {
		// 启动activity时不自动弹出软键盘
		mainPadActivity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mListView = (ListView) contentView.findViewById(R.id.listview);
		mBtnSend = (Button) contentView.findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnRecord = (Button) contentView.findViewById(R.id.btn_record);
		mBtnRecord.setOnClickListener(this);
		isStopRecord = true;

		mNoteList = new ArrayList<Note>();
		// mPlayer = null;

		/* 判断SD Card是否插入 */
		sdCardExit = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		/* 取得SD Card路径做为录音的文件位置 */
		if (sdCardExit){
			myRecAudioDir = Environment.getExternalStorageDirectory();
			String path = myRecAudioDir.getPath()+"/IntelligentProfile/NoteMedia";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			myRecAudioDir = file;
		}
			
		else
			ToastFactory.showToast(mainPadActivity.instance, "请检查 内存卡 是否可用！");
			

		/* 取得SD Card目录里的所有.amr文件 */
//		getRecordFiles();
		mEditTextContent = (EditText) contentView
				.findViewById(R.id.et_sendmessage);
		mAdapter = new NoteListAdapter(MainPad.instance, mNoteList);
		mListView.setAdapter(mAdapter);
		/*mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				()
			}
		});*/
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (noteService == null) {
			noteService = new NoteService();
		}
		if (mNoteList == null) {
			mNoteList = new ArrayList<Note>();
		}else {
			mNoteList.clear();
		}
		List<Note> notes = noteService.check();
		mNoteList.addAll(notes);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 暂时弃用
	 */
	private void getRecordFiles() {
//		recordFiles = new ArrayList<String>();
//		if (sdCardExit) {
//			File files[] = myRecAudioDir.listFiles();
//			if (files != null) {
//
//				for (int i = 0; i < files.length; i++) {
//					if (files[i].getName().indexOf(".") >= 0) {
//						/* 读取.amr文件 */
//						String fileS = files[i].getName().substring(
//								files[i].getName().indexOf("."));
//						if (fileS.toLowerCase().equals(".amr"))
//							recordFiles.add(files[i].getName());
//
//					}
//				}
//			}
//		}
	}

	/* 开启播放录音文件的程序 */
	private void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		startActivity(intent);
	}

	private String getMIMEType(File f) {
		String end = f
				.getName()
				.substring(f.getName().lastIndexOf(".") + 1,
						f.getName().length()).toLowerCase();
		String type = "";
		if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
				|| end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg")) {
			type = "image";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_record:
			if (isStopRecord)
				record();
			else
				stopRecord();
		}
	}
	
	private void record() {
		try {
			if (!sdCardExit) {
				Toast.makeText(MainPad.instance, "请插入SD Card", Toast.LENGTH_LONG)
						.show();
				return;
			}
			mBtnRecord.setTextColor(getResources().getColor(R.color.red));
			mBtnRecord.setText(R.string.stop);  //为什么不可以嗯？
			Calendar c = Calendar.getInstance();
			// 建立录音档
			Toast.makeText(MainPad.instance, "录音中", Toast.LENGTH_LONG).show();
			myRecAudioFile = File.createTempFile(
					strTempFile + c.get(Calendar.YEAR) + c.get(Calendar.MONTH)
							+ c.get(Calendar.DAY_OF_MONTH) + " "
							+ c.get(Calendar.HOUR_OF_DAY)
							+ c.get(Calendar.MINUTE) + c.get(Calendar.SECOND),
					".amr", myRecAudioDir);
			myPlayFile = new File(myRecAudioDir.getAbsolutePath()
	                + File.separator
	                + myRecAudioFile.getName());
			mMediaRecorder01 = new MediaRecorder();
			// 设定录音来源为麦克风
			mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
			mMediaRecorder01
					.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mMediaRecorder01
					.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

			mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());

			mMediaRecorder01.prepare();
			mMediaRecorder01.start();

			mBtnSend.setEnabled(false);
			isStopRecord = false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stopRecord() {
		if (myRecAudioFile != null) {
			// 停止录音
			mMediaRecorder01.stop();
			mMediaRecorder01.release();
			mMediaRecorder01 = null;
			/*myPlayFile = new File(myRecAudioDir.getAbsolutePath()
	                + File.separator
	                + ((CheckedTextView) arg1).getText());*/
			// 将录音文件名给Adapter
			
			MediaPlayer mp = MediaPlayer.create(MainPad.instance, Uri.parse(myPlayFile.toString()));
			int duration = mp.getDuration() / 1000;//即为时长 是ms
			mp.release();
			Note entity = new Note();
			entity.setDate(getDate());
			entity.setContent(myPlayFile.toString());
			entity.setNoteType(1);
			entity.setRecordLength(Integer.toString(duration) + " ' ");
			
			int insertId = noteService.add(entity);
		    entity.setNoteId(insertId);
			mNoteList.add(entity);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mListView.getCount() - 1);
			mBtnRecord.setTextColor(getResources().getColor(R.color.white));
			mBtnRecord.setText(R.string.record);
			mBtnSend.setEnabled(true);
			isStopRecord = true;
		}
	}
	
	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			Note entity = new Note();
			entity.setDate(getDate());
			entity.setContent(contString);
			
		    int insertId = noteService.add(entity);
		    entity.setNoteId(insertId);
		    mNoteList.add(entity);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mListView.getCount() - 1);
		    mEditTextContent.setText("");
		    
		}
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH)+1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));
		StringBuffer sbBuffer = new StringBuffer();
		if (mins.length() == 1) {
			sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":0"
					+ mins);
			return sbBuffer.toString();
		}
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

	
}
