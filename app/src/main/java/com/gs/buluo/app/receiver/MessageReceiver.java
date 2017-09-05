package com.gs.buluo.app.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.HomeMessageEnum;
import com.gs.buluo.app.bean.PushMessageBean;
import com.gs.buluo.app.view.activity.OrderDetailActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver {
    private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
    public static final String LogTag = "TPushReceiver";

    private void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    //反注册的回调
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
//        if (context == null) {
//            return;
//        }
//        String text = "";
//        if (errorCode == XGPushBaseReceiver.SUCCESS) {
//            text = "反注册成功";
//        } else {
//            text = "反注册失败" + errorCode;
//        }
//        Log.d(LogTag, text);
//        show(context, text);

    }

    //设置tag的回调
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
//        if (context == null) {
//            return;
//        }
//        String text = "";
//        if (errorCode == XGPushBaseReceiver.SUCCESS) {
//            text = "\"" + tagName + "\"设置成功";
//        } else {
//            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
//        }
//        Log.d(LogTag, text);
//        show(context, text);

    }

    //删除tag的回调
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        PushMessageBean messageBean = JSON.parseObject(message.getCustomContent(), PushMessageBean.class);
        PushMessageBean.PushMessageBody messageBody = messageBean.message;
        if (messageBody.messageBodyType == HomeMessageEnum.ORDER_DELIVERY) {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.ORDER_ID, messageBody.referenceId);
            context.startActivity(intent);
        }
//        else if (messageBody.messageBodyType == HomeMessageEnum.TENANT_WITHDRAW) {
//            Intent intent = new Intent(context, BillDetailActivity.class);
//            intent.putExtra(Constant.BILL_ID, messageBody.referenceId);
//            context.startActivity(intent);
//        }
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }

    //注册的回调
    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {

    }

    // 消息透传的回调
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        TribeApplication.getInstance().getXgMessage();
        // 获取自定义key-value
//                    String customContent = message.getCustomContent();
//                    if (customContent != null && customContent.length() != 0) {
//                        try {
//                            JSONObject obj = new JSONObject(customContent);
//                            // key1为前台配置的key
//
//                            if (!obj.isNull("key")) {
//                                String value = obj.getString("key");
//                    Log.e(LogTag, "get custom value:" + value);
//                }
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
