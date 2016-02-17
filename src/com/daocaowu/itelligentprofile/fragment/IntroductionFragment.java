package com.daocaowu.itelligentprofile.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.activity.WebViewActivity;

public class IntroductionFragment extends Fragment implements OnClickListener{

	private static final String TAG = "GPSMapFragment";

	MainPad mainPadActivity = null;
	View contentView = null;
	Button btn_introduction = null;
	Button btn_help = null;
	Button btn_contact_we = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainPadActivity = (MainPad)getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.inroduction_layout, container, false);
		btn_introduction = (Button) contentView
				.findViewById(R.id.btn_inroduction);
		// btn_help = (Button)contentView.findViewById(R.id.btn_help);
		btn_contact_we = (Button) contentView
				.findViewById(R.id.btn_contact_we);
		btn_introduction.setOnClickListener(this);
		// btn_help.setOnClickListener(this);
		btn_contact_we.setOnClickListener(this);

		return contentView;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(MainPad.instance, WebViewActivity.class);
		switch (v.getId()) {
		case R.id.btn_inroduction:
			intent.putExtra("extraUrl", "file:///android_asset/HTML/introduction.html");
			startActivity(intent);
			break;
//		case R.id.btn_help:
//			intent.putExtra("extraUrl", "file:///android_asset/HTML/1.html");
//			startActivity(intent);
//			break;
		case R.id.btn_contact_we:
			Uri uri = Uri.parse("mailto:"+"229153581@qq.com");
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			startActivity(it);
			break;
		default:
			intent = null;
			break;
		}
		
	}

}
