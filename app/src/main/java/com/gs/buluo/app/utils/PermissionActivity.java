package com.gs.buluo.app.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.BaseActivity;
import com.gs.buluo.common.widget.CustomAlertDialog;

/**
 * Created by Solang on 2017/5/11.
 */

public abstract class PermissionActivity extends BaseActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private String permisName;

    /**
     * 授权成功后的业务
     */
    protected abstract void onAllGranted();

    /**
     * 第一个值为权限的名字，剩余值为所需权限
     * @return
     */
    protected abstract String[] getPermissions();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermis();
    }


    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return 0;
    }

    private void checkPermis() {
        String[] tmp = getPermissions();
        String[] permissionRequests = new String[tmp.length-1];
        for (int k = 0;k<tmp.length;k++){
            if (k>0){
                permissionRequests[k-1] = tmp[k];
            }
        }
        permisName = tmp[0];


        boolean isAllGranted = checkPermissionAllGranted(
                permissionRequests
        );
        // 如果权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            onAllGranted();
            return;
        }

        /**
         * 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                permissionRequests,
                MY_PERMISSION_REQUEST_CODE
        );
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }
    private int i = 0;
    /**
     *  申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                onAllGranted();
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                if (i == 0) {
                    openAppDetails();
                }
                i++;
            }
        }
    }

    private void openAppDetails() {
        CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-" + getResources().getString(R.string.app_name) + "-权限中开启" + permisName +"权限,以正常使用此功能");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }
}
