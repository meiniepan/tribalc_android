package com.gs.buluo.app.view.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.UpdateWalletPwdActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/7.
 */
public class PayPanel extends Dialog implements PasswordPanel.OnPasswordPanelDismissListener {
    private final OnPayPanelDismissListener onDismissListener;
    private Context mContext;
    @Bind(R.id.pay_way)
    TextView tvWay;
    @Bind(R.id.pay_money)
    TextView tvTotal;

    private OrderBean.PayChannel payWay;
    private List<String> orderId;
    private View rootView;
    private String price;

    public PayPanel(Context context,OnPayPanelDismissListener onDismissListener) {
        super(context ,R.style.my_dialog);
        mContext=context;
        this.onDismissListener=onDismissListener;
        initView();
    }

    public void setData(OrderBean.PayChannel way, String price, List<String> orderId){
        payWay=way;
        tvWay.setText(way.toString());
        this.price = price;
        tvTotal.setText(price);
        this.orderId=orderId;
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.pay_board, null);
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
                if (payWay== OrderBean.PayChannel.BALANCE)
                    getWalletInfo();
                else
                    dismiss();
            }
        });
    }

    public void getWalletInfo() {
        new MoneyModel().getWelletInfo(TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseCodeResponse<WalletAccount>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<WalletAccount>> call, Response<BaseCodeResponse<WalletAccount>> response) {
                if (response.body()!=null&&response.body().data!=null&&response.body().code== ResponseCode.GET_SUCCESS){
                    String password = response.body().data.password;
                    String balance = response.body().data.balance;
                    if (password ==null){
                        showAlert();
                    }else {
                        if (Float.parseFloat(price)> Float.parseFloat(balance)){
                            showNotEnough(balance);
                        }else {
                            showPasswordPanel(password);
                        }
                    }
                }else {
                    ToastUtils.ToastMessage(getContext(),R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<WalletAccount>> call, Throwable t) {
                ToastUtils.ToastMessage(getContext(),R.string.connect_fail);
            }
        });
    }

    private void showNotEnough(final String balance) {
        new CustomAlertDialog.Builder(getContext()).setTitle("提示").setMessage("您账户余额不足，请先去充值")
                .setPositiveButton("去充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RechargePanel panel =new RechargePanel(mContext);
                        panel.setData(balance);
                        panel.show();
                    }
                }).setNegativeButton("取消",null).create().show();
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getContext()).setTitle("提示").setMessage("您还没有设置支付密码，请先去设置密码")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContext().startActivity(new Intent(getContext(),UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton("取消",null).create().show();
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
        PasswordPanel passwordPanel=new PasswordPanel(mContext,password,orderId, payWay,this);
        passwordPanel.show();
        TranslateAnimation animation=new TranslateAnimation(0,-CommonUtils.getScreenWidth(mContext),0,0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.start();
        rootView.startAnimation(animation);
    }

    @Override
    public void onPasswordPanelDismiss() {
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
