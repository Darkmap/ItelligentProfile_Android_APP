package com.daocaowu.itelligentprofile.activity;

import android.app.Activity;
import android.os.Bundle;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.utils.ToastFactory;

public class MyAlarm extends Activity {

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
         setContentView(R.layout.activity_main);
         
         ToastFactory.showToast(this, "la la la la lalala");
         finish();
    }  
    
}
