package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.app.view.widget.panel.PayPanel;
import com.gs.buluo.app.view.widget.panel.RechargePanel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyPayActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.property_pay_price)
    TextView tvPrice;
    private String id;

    @Bind(R.id.new_order_pay_balance)
    CheckBox rbBalance;
    @Bind(R.id.new_order_pay_wechat)
    CheckBox rbWeChat;
    @Bind(R.id.new_order_pay_ali)
    CheckBox rbAli;
    private OrderBean.PayChannel payMethod;
    private String price;

    Context mContext;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext=this;
        findViewById(R.id.choose_pay_back).setOnClickListener(this);
        Intent intent = getIntent();
        price = intent.getStringExtra(Constant.PRICE);
        id = intent.getStringExtra(Constant.GOODS_ID);
        tvPrice.setText(price);
        tvPrice.clearFocus();

        rbBalance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbBalance.setChecked(true);
                    rbAli.setChecked(false);
                    rbWeChat.setChecked(false);
                    payMethod= OrderBean.PayChannel.BALANCE;
                }
            }
        });
        rbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbBalance.setChecked(false);
                    rbAli.setChecked(true);
                    rbWeChat.setChecked(false);
                    payMethod=OrderBean.PayChannel.ALIPAY;
                }
            }
        });
        rbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbBalance.setChecked(false);
                    rbAli.setChecked(false);
                    rbWeChat.setChecked(true);
                    payMethod=OrderBean.PayChannel.WEICHAT;
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_pay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_pay_back:
                finish();
                break;
            case R.id.choose_pay_pay:
                List<String > list=new ArrayList<>();
                list.add(id);
                if (payMethod== OrderBean.PayChannel.BALANCE){
                    getWalletInfo();
                }
                new MoneyModel().createPayment(list, "BALANCE","maintain", new TribeCallback<OrderPayment>() {
                    @Override
                    public void onSuccess(Response<BaseCodeResponse<OrderPayment>> response) {
                        
                    }

                    @Override
                    public void onFail(int responseCode, BaseCodeResponse<OrderPayment> body) {

                    }
                });
                break;
        }
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
                        }
                    }
                }else {
                    ToastUtils.ToastMessage(mContext,R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<WalletAccount>> call, Throwable t) {
                ToastUtils.ToastMessage(mContext,R.string.connect_fail);
            }
        });
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(mContext).setTitle("提示").setMessage("您还没有设置支付密码，请先去设置密码")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       mContext.startActivity(new Intent(mContext,UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton("取消",null).create().show();
    }

    private void showNotEnough(final String balance) {
        new CustomAlertDialog.Builder(mContext).setTitle("提示").setMessage("您账户余额不足，请先去充值")
                .setPositiveButton("去充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RechargePanel panel =new RechargePanel(mContext);
                        panel.setData(balance);
                        panel.show();
                    }
                }).setNegativeButton("取消",null).create().show();
    }
    
}
