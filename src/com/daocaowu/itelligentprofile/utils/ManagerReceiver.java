package com.daocaowu.itelligentprofile.utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ManagerReceiver extends BroadcastReceiver {
	private ReceiverInterface mInterface;

	public ManagerReceiver() {
	}

	public ManagerReceiver(ReceiverInterface pInterface) {
		this.mInterface = pInterface;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (mInterface != null) {
			mInterface.refresh(intent);
		}
	}

	public interface ReceiverInterface {
		public void refresh(Intent intent);
	}

}
