package com.gs.buluo.app.view.widget;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

import com.gs.buluo.app.R;

public class CustomDialog extends Dialog{

	private ProgressDialog mDialog;
	private static CustomDialog instance = null;

	public CustomDialog(Context context, int themeResId) {
		super(context, themeResId);
	}


	public static synchronized CustomDialog getInstance(Context context) {
		if (instance == null) {
			instance = new CustomDialog(context, R.style.CustomProgressDialog);
			instance.setContentView(R.layout.customprogressdialog);
			instance.getWindow().getAttributes().gravity = Gravity.CENTER;
		}
		return instance;
	}
	
	public void show(String message,boolean cancleable){
		instance.setTitle(message);
		instance.setCancelable(cancleable);
		instance.show();
	}
	public void show(Context context,int message,boolean cancleable){
//		mDialog =  ProgressDialog.show(context, null, context.getResources().getString(message));
//		mDialog=new ProgressDialog(context);
//		return customProgressDialog;
		mDialog.setCancelable(cancleable);
		mDialog.show();
	}

	public void dismissDialog() {
			if (instance != null && instance.isShowing()) {
				instance.dismiss();
			}
	}
}
