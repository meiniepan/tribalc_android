package com.gs.buluo.app.view.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.http.GET;

/**
 * Created by hjn on 2016/12/7.
 */
public class PayPanel extends Dialog{
    private final OnPayPanelDismissListener onDismissListener;
    private Context mContext;
    @Bind(R.id.pay_way)
    TextView tvWay;
    @Bind(R.id.pay_money)
    TextView total;

    public PayPanel(Context context,OnPayPanelDismissListener onDismissListener) {
        super(context ,R.style.my_dialog);
        mContext=context;
        this.onDismissListener=onDismissListener;
        initView();
    }

    public void setData(String way,String price){
        tvWay.setText(way);
        total.setText(price);
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pay_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext,400);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        findViewById(R.id.pay_ask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        findViewById(R.id.pay_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.pay_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                payMoney();
                showPasswordPanel();
            }
        });
    }

    private void payMoney() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(getContext(), null);
        // 将该app注册到微信
        msgApi.registerApp(Constant.WX_ID);
        PayReq request = new PayReq();
        request.appId = Constant.WX_ID;
        request.partnerId = Constant.WX_SHOP_ID;
        request.prepayId= "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr= CommonUtils.getRandomString(32);
        request.timeStamp= SystemClock.currentThreadTimeMillis()/1000+"";
        request.sign= Constant.WX_SIGN;
        msgApi.sendReq(request);
    }

    private void showPasswordPanel() {
        PasswordPanel passwordPanel=new PasswordPanel(getContext());
        passwordPanel.show();

    }

    public interface OnPayPanelDismissListener {
        void onPayPanelDismiss();
    }

    @Override
    public void dismiss() {
        onDismissListener.onPayPanelDismiss();
        super.dismiss();
    }
}
