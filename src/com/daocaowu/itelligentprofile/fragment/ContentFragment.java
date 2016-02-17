package com.daocaowu.itelligentprofile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.R;

public class ContentFragment extends Fragment {

	private String TAG = "ContentFragment";
	
	String text = null;

	public ContentFragment() {
	}

	public ContentFragment(String text) {
		Log.e(TAG, text);
		this.text = text;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		Log.e(TAG, "onCreate:" + text);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView:" + text);
		// inflater the layout
		View view = inflater.inflate(R.layout.fragment_text, null);
		TextView textView = (TextView) view.findViewById(R.id.textView);
		if (!TextUtils.isEmpty(text)) {
			textView.setText(text);
		}
		return view;
	}

	public String getText() {
		return text;
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy:" + text);
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.e(TAG, "onDetach:" + text);
		super.onDetach();
	}

	@Override
	public void onPause() {
		Log.e(TAG, "onPause:" + text);
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.e(TAG, "onResume:" + text);
		super.onResume();
	}

	@Override
	public void onStart() {
		Log.e(TAG, "onStart:" + text);
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.e(TAG, "onStop:" + text);
		super.onStop();
	}

}
