package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.UpdateEvent;
import com.gs.buluo.common.widget.CustomAlertDialog;
import com.tencent.tmselfupdatesdk.ITMSelfUpdateListener;
import com.tencent.tmselfupdatesdk.TMSelfUpdateManager;
import com.tencent.tmselfupdatesdk.model.TMSelfUpdateUpdateInfo;

import org.xutils.common.Callback;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hjn on 2017/3/1.
 */

public class UpdatePanel extends Dialog {

    private ViewStub viewStub;
    private ProgressBar progressBar;
    private TextView progress;
    private View progressView;
    private View rootView;
    private Callback.Cancelable cancelable;
    private ListView listView;
    private boolean supported;
    private Button positiveBt;
    private String lastVersion;

    public UpdatePanel(Context context, UpdateEvent data) {
        super(context, R.style.sheet_dialog);
        initView();
        initData(data);
        initDownload();
    }

    private TMSelfUpdateManager selfUpdateManager = TMSelfUpdateManager.getInstance();

    private void initDownload() {
        try {
            Context context = TribeApplication.getInstance().getApplicationContext();//application的context
            String channelId = "1003001";
            ITMSelfUpdateListener updateListener = new ITMSelfUpdateListener() {
                @Override
                public void onDownloadAppStateChanged(int state, int errorCode, String errorMsg) {
                    Log.e("Update", "onDownloadAppStateChanged: " + errorMsg);
                }

                @Override
                public void onDownloadAppProgressChanged(long l, long l1) {
                    Log.e("Update", "onDownloadAppProgressChanged: " + l +"......."+l1);
                    int pro = Integer.parseInt(l * 100 / l1 + "");
                    progress.setText(pro + "%");
                    progressBar.setMax(100);
                    progressBar.setProgress(pro);
                }

                @Override
                public void onUpdateInfoReceived(TMSelfUpdateUpdateInfo tmSelfUpdateUpdateInfo) {
                    Log.e("Update", "onDownloadAppStateChanged: " + tmSelfUpdateUpdateInfo);
                }
            };
            selfUpdateManager.init(context, channelId, updateListener, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData(final UpdateEvent data) {
        this.supported = data.supported;
        this.lastVersion = data.lastVersion;
        if (data.releaseNote == null || data.releaseNote.size() == 0) {  //505返回码
            data.releaseNote = new ArrayList<>();
            data.releaseNote.add("发现重大更新");
            data.releaseNote.add("如果取消更新，您将无法继续使用");
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return data.releaseNote.size();
            }

            @Override
            public Object getItem(int position) {
                return data.releaseNote.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.update_item, parent, false);
                }
                TextView item = (TextView) convertView.findViewById(R.id.update_item);
                item.setText(getItem(position).toString());
                return convertView;
            }
        });
    }

    private void initView() {
        rootView = View.inflate(getContext(), R.layout.update_board, null);
        setContentView(rootView);
        viewStub = (ViewStub) rootView.findViewById(R.id.update_progress_stub);
        positiveBt = (Button) rootView.findViewById(R.id.update_dialog_yes);
        positiveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetStatus();
            }
        });
        listView = (ListView) rootView.findViewById(R.id.message_content_root);
    }


    private void checkNetStatus() {
        if (!CommonUtils.isConnectedWifi(getContext())) {
            new CustomAlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage("检测到您当前并非Wi-Fi环境,是否仍要更新?")
                    .setPositiveButton("下载", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownload();
                        }
                    })
                    .setNegativeButton("取消", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelUpdate();
                        }
                    }).create().show();
        } else {
            startDownload();
        }
    }

    private void stopDownload() {
        if (cancelable != null) {
            cancelable.cancel();
        }
        progressView.setVisibility(View.GONE);
    }

    private void startDownload() {
        selfUpdateManager.startSelfUpdate(false);
//        if (progressView == null) {
//            inflateProgress();
//        } else {
//            progressView.setVisibility(View.VISIBLE);
//        }
        ToastUtils.ToastMessage(getContext(),"开始后台下载....");
        dismiss();
//        positiveBt.setText("后台下载");
//        positiveBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }

    public void inflateProgress() {
        progressView = viewStub.inflate();
        progressBar = (ProgressBar) progressView.findViewById(R.id.download_dialog_progress);
        progress = (TextView) progressView.findViewById(R.id.download_dialog_count);
    }

    public void cancelUpdate() {
        SharePreferenceManager.getInstance(getContext()).setValue(Constant.CANCEL_UPDATE_VERSION, lastVersion);
        if (supported) {
            dismiss();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selfUpdateManager.destroy();
        SharePreferenceManager.getInstance(getContext()).setValue(Constant.CANCEL_UPDATE_VERSION, lastVersion);
    }
}
