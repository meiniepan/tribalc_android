package com.gs.buluo.app.view.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.WalletResponse;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.NewOrderActivity;
import com.gs.buluo.app.view.activity.OrderActivity;
import com.gs.buluo.app.view.activity.OrderDetailActivity;
import com.gs.buluo.app.view.activity.UpdateWalletPwdActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by hjn on 2016/12/7.
 */
public class PayPanel extends Dialog  {
    private final OnPayPanelDismissListener onDismissListener;
    private Context mContext;
    @Bind(R.id.pay_way)
    TextView tvWay;
    @Bind(R.id.pay_money)
    TextView total;

    private String payWay;
    private String orderId;

    public PayPanel(Context context,OnPayPanelDismissListener onDismissListener) {
        super(context ,R.style.my_dialog);
        mContext=context;
        this.onDismissListener=onDismissListener;
        initView();
    }

    public void setData(String way, String price,String orderId){
        payWay=way;
        tvWay.setText(way);
        total.setText(price);
        this.orderId=orderId;
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
                if (TextUtils.equals(payWay,getContext().getString(R.string.pay_balance)))
                    getWalletInfo();
                else
                    dismiss();
            }
        });
    }

    public void getWalletInfo() {
        new MoneyModel().getWelletInfo(TribeApplication.getInstance().getUserInfo().getId(), new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.body()!=null&&response.body().code== ResponseCode.GET_SUCCESS){
                    if (response.body().data.password==null){
                        showAlert();
                    }else {
                        showPasswordPanel(response.body().data.password);
                    }
                }else {
                    ToastUtils.ToastMessage(getContext(),R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                ToastUtils.ToastMessage(getContext(),R.string.connect_fail);
            }
        });
    }

    private void showAlert() {
        new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("您还没有设置支付密码，请先去设置密码")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContext().startActivity(new Intent(getContext(),UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton("取消",null);
    }

    private void payMoney() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(getContext(), null);
        // 将该app注册到微信
        msgApi.registerApp(Constant.Base.WX_ID);
        PayReq request = new PayReq();
        request.appId = Constant.Base.WX_ID;
        request.partnerId = Constant.Base.WX_SHOP_ID;
        request.prepayId= "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr= CommonUtils.getRandomString(32);
        request.timeStamp= SystemClock.currentThreadTimeMillis()/1000+"";
        request.sign= Constant.Base.WX_SIGN;
        msgApi.sendReq(request);
    }

    private void showPasswordPanel(String password) {
        PasswordPanel passwordPanel=new PasswordPanel(getContext(),password,orderId);
        if (!passwordPanel.isShowing())
            passwordPanel.show();
        dismiss();
    }


    public interface OnPayPanelDismissListener {
        void onPayPanelDismiss();
    }

    @Override
    public void dismiss() {
        if (onDismissListener!=null){
            onDismissListener.onPayPanelDismiss();
        }
        super.dismiss();
    }
}
