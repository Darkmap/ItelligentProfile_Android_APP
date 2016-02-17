package com.daocaowu.itelligentprofile.adapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.bean.Note;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.NoteService;

public class NoteListAdapter extends BaseAdapter {
	protected static final String LOG_TAG = null;
	private MediaPlayer mPlayer;

	public static interface IMsgViewType {
		int TEXT = 0;
		int RECORD = 1;
	}

	private static final String TAG = NoteListAdapter.class.getSimpleName();

	private List<Note> notes;

	private Context ctx;

	private LayoutInflater mInflater;

	public NoteListAdapter(Context context, List<Note> notes) {
		ctx = context;
		this.notes = notes;
		mInflater = LayoutInflater.from(context);
		mPlayer = null;
	}

	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Object getItem(int position) {
		return notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		Note note = notes.get(position);

		if (note.getNoteType() == 1) {
			return IMsgViewType.RECORD;
		} else {
			return IMsgViewType.TEXT;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int deletePosition = position;
		Note note = notes.get(position);
		
		String date = note.getDate();
		int index = date.lastIndexOf('-');
		int leftOrRight = 0;
		if (date.charAt(index + 2) == ' ')
			leftOrRight = (date.charAt(index + 1) - 48) % 2;
		else
			leftOrRight = (date.charAt(index + 2) - 48) % 2;
		
		ViewHolder viewHolder = null;
//		暂时先把这个缓冲给去掉，解决了bug By 哲操
		/**
		 * 按以下的方法，大家再测试一下 By ADao12
		 */
		if (convertView == null) {
			viewHolder = new ViewHolder();
			
			if (note.getNoteType() == 1) {
				if (leftOrRight == 1) {
					convertView = mInflater.inflate(R.layout.record_left, null);
					viewHolder.tvSendTime = (TextView) convertView
							.findViewById(R.id.tv_sendtime3);
					viewHolder.imgRecord = (ImageButton) convertView
							.findViewById(R.id.imageButton1);
					viewHolder.tvRecordLength = (TextView) convertView.findViewById(R.id.tv_recordLength);
					convertView.setTag(R.layout.record_left, viewHolder);
				} else {
					convertView = mInflater
							.inflate(R.layout.record_right, null);
					viewHolder.tvSendTime = (TextView) convertView
							.findViewById(R.id.tv_sendtime4);
					viewHolder.imgRecord = (ImageButton) convertView
							.findViewById(R.id.imageButton2);
					viewHolder.tvRecordLength = (TextView) convertView.findViewById(R.id.tv_recordLength2);
					convertView.setTag(R.layout.record_right, viewHolder);
				}
			} else {
				if (leftOrRight == 1) {
					convertView = mInflater.inflate(
							R.layout.chatting_item_msg_text_left, null);
					viewHolder.tvSendTime = (TextView) convertView
							.findViewById(R.id.tv_sendtime2);
					viewHolder.tvContent = (TextView) convertView
							.findViewById(R.id.tv_chatcontent2);
					convertView.setTag(R.layout.chatting_item_msg_text_left, viewHolder);
				} else {
					convertView = mInflater.inflate(
							R.layout.chatting_item_msg_text_right, null);
					viewHolder.tvSendTime = (TextView) convertView
							.findViewById(R.id.tv_sendtime);
					viewHolder.tvContent = (TextView) convertView
							.findViewById(R.id.tv_chatcontent);
					convertView.setTag(R.layout.chatting_item_msg_text_right, viewHolder);
				}
			}
//			convertView.setTag(viewHolder);
		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
			if (note.getNoteType() == 1) {
				if (leftOrRight == 1) {
					viewHolder = (ViewHolder) convertView.getTag(R.layout.record_left);
				} else {
					viewHolder = (ViewHolder) convertView.getTag(R.layout.record_right);
				}
			} else {
				if (leftOrRight == 1) {
					viewHolder = (ViewHolder) convertView.getTag(R.layout.chatting_item_msg_text_left);
				} else {
					viewHolder = (ViewHolder) convertView.getTag(R.layout.chatting_item_msg_text_right);
				}
			}
		}
		convertView.setClickable(true);
		viewHolder.tvSendTime.setText(note.getDate());

		if (note.getNoteType() == 0) {
			viewHolder.tvContent.setTag(position);
			viewHolder.tvContent.setText(note.getContent());
			viewHolder.tvContent.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if (v.getId() == R.id.tv_chatcontent || v.getId() == R.id.tv_chatcontent2){
						Log.v(TAG, "v.getId():"+v.getId());
						AbstractLocalService noteService = new NoteService();
						noteService.delete(notes.get(deletePosition).getNoteId());
						notes.remove(deletePosition);
						notifyDataSetChanged();
						return true;
					}
					return false;
				}
			});
		} else {
			viewHolder.tvRecordLength.setText(note.getRecordLength());
			viewHolder.imgRecord.setTag(position);
			viewHolder.imgRecord.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ImageButton img = (ImageButton) v;
					if (mPlayer == null)
					{
						mPlayer = new MediaPlayer();
					}
					if (mPlayer.isPlaying() == true) {
						mPlayer.stop();
						mPlayer = null;
					} else {
						if (mPlayer != null)
							mPlayer.release();
						mPlayer = null;
						mPlayer = new MediaPlayer();
						try {
//							Integer curPosition = (Integer) img.getTag();
							Note entity1 = notes.get(deletePosition);
							mPlayer.setDataSource(entity1.getContent()
									.toString());
							mPlayer.prepare();
							mPlayer.start();
						} catch (IOException e) {
							Log.e(LOG_TAG, "播放失败");
						}
					}
				}

			});
			viewHolder.imgRecord.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if (v.getId() == R.id.imageButton1 || v.getId() == R.id.imageButton2) {
						Note tempNote = notes.get(deletePosition);
						//TODO 删除音频
						File file = new File(tempNote.getContent());
						if (file != null && file.exists()) {
							/* 删除文件 */
					         file.delete();
						}
						AbstractLocalService noteService = new NoteService();
						noteService.delete(tempNote.getNoteId());
						notes.remove(deletePosition);
						notifyDataSetChanged();
						return true;
					}
					return false;
				}
			});
		}
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvContent;
		public ImageButton imgRecord;
		public TextView tvRecordLength;
	}

	/**
	 * 添加列表项
	 * 
	 * @param item
	 */
	public void addItem(Note item) {
		this.notes.add(item);
	}

}
