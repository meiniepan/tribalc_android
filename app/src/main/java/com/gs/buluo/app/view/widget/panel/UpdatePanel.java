package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by hjn on 2017/3/1.
 */

public class UpdatePanel extends Dialog implements View.OnClickListener {

    private ViewStub viewStub;
    private ProgressBar progressBar;
    private TextView progress;
    private View progressView;
    private View rootView;
    private Callback.Cancelable cancelable;

    public UpdatePanel(Context context) {
        super(context, R.style.sheet_dialog);
        initView();
    }

    private void initView() {
        rootView = View.inflate(getContext(), R.layout.update_board, null);
        setContentView(rootView);
        viewStub = (ViewStub) rootView.findViewById(R.id.update_progress_stub);
        rootView.findViewById(R.id.update_dialog_yes).setOnClickListener(this);
        rootView.findViewById(R.id.update_dialog_no).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_dialog_yes:
                if (progressView == null) {
                    inflateProgress();
                    startDownload();
                } else {
                    progressView.setVisibility(View.VISIBLE);
                    startDownload();
                }
                break;
            case R.id.update_dialog_no:
                SharePreferenceManager.getInstance(getContext()).setValue(Constant.UPDATE_TIME, System.currentTimeMillis());
                dismiss();
                break;
            case R.id.download_dialog_close:
                stopDownload();
                break;
        }
    }

    private void stopDownload() {
        if (cancelable != null) {
            cancelable.cancel();
        }
        progressView.setVisibility(View.GONE);
    }


    private void startDownload() {
        File file = new File(Constant.DIR_PATH + "tribalc.apk");
        if (file.exists()) {
            file.delete();
        }
        RequestParams params = new RequestParams(Constant.APK_URL);
        params.setAutoResume(true);//断点下载
        params.setSaveFilePath(Constant.DIR_PATH);
        params.setAutoRename(true);
        params.addHeader("Accept-Encoding", "identity");
        cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                int pro = Integer.parseInt(current * 100 / total + "");
                progress.setText(pro + "%");
                progressBar.setMax(100);
                progressBar.setProgress(pro);
            }

            @Override
            public void onSuccess(File result) {
                dismiss();
                result.renameTo(new File(Constant.DIR_PATH + "tribalc.apk"));
                startInstall(getContext(), Constant.DIR_PATH + "tribalc.apk");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.ToastMessage(getContext(), "下载失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void inflateProgress() {
        progressView = viewStub.inflate();
        progressBar = (ProgressBar) progressView.findViewById(R.id.download_dialog_progress);
        progress = (TextView) progressView.findViewById(R.id.download_dialog_count);
        progressView.findViewById(R.id.download_dialog_close).setOnClickListener(this);
    }

    private void startInstall(Context context, String filePath) {
        File apkfile = new File(filePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    @Override
    public void onBackPressed() {

    }
}
