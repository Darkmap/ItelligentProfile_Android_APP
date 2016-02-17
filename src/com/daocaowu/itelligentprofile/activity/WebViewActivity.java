package com.daocaowu.itelligentprofile.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.daocaowu.itelligentprofile.R;

public class WebViewActivity extends BaseActivity {

	/** Called when the activity is first created. */
    private WebView webview;
    static final String mimeType = "text/html";  
    static final String encoding = "utf-8"; 
    //在进来Activity前应该给这个Url赋值
    private String url = "http://www.baidu.com/";
    
@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

		loadAssetHtml();
		
//		webview.loadDataWithBaseURL(url, data, mimeType, encoding, historyUrl);
		// 设置web视图客户端
	}


	public void loadAssetHtml() {

		webview = (WebView) findViewById(R.id.web_view);
		// 设置WebView属性，能够执行JavaScript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setDefaultTextEncodingName(encoding);
		webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		webview.setHorizontalScrollBarEnabled(false);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.setInitialScale(70);
		webview.setHorizontalScrollbarOverlay(true);

		webview.setWebViewClient(new MyWebViewClient());
		
		webview.loadUrl("file:///android_asset/HTML/gogo.html");
		String extraUrl = ""; 
		Bundle extras = getIntent().getExtras();  
		if (extras != null) {  
			extraUrl = extras.getString("extraUrl");  
		}
		
		if (extraUrl != null && !"".equals(extraUrl)) {
			url = extraUrl;
			webview.loadUrl(url);
//			webview.loadUrl("file:///android_asset/HTML/tel.html");
			Log.v("url", url);
		} else {
			Toast.makeText(WebViewActivity.this, "无法获取链接", Toast.LENGTH_LONG);
		}
			
	}


	// 设置回退
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// web视图客户端
	public class MyWebViewClient extends WebViewClient {
		/**
		* Show in webview not system webview.
		*/
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

}
