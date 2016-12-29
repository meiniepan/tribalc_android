package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.OrderBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/12/29.
 */
public class RechargePanel extends Dialog implements View.OnClickListener {
    Context mContext;
    @Bind(R.id.recharge_integer)
    TextView mInterger;
    @Bind(R.id.recharge_float)
    TextView mFloat;
    @Bind(R.id.recharge_pay_wechat)
    RadioButton rbWeChat;
    @Bind(R.id.recharge_pay_ali)
    RadioButton rbAli;
    private OrderBean.PayChannel payMethod;

    public RechargePanel(Context context) {
        super(context, R.style.my_dialog);
        mContext=context;
        initView();
    }
    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recharge_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        findViewById(R.id.recharge_back).setOnClickListener(this);
        findViewById(R.id.recharge_finish).setOnClickListener(this);

        rbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbAli.setChecked(true);
                    rbWeChat.setChecked(false);
                    payMethod= OrderBean.PayChannel.ALIPAY;
                }
            }
        });
        rbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbAli.setChecked(false);
                    rbWeChat.setChecked(true);
                    payMethod=OrderBean.PayChannel.WEICHAT;
                }
            }
        });
    }

    public void setData(String price){
        String[] arrs=price.split(".");
        if (arrs.length>1){
            mInterger.setText(arrs[0]);
            mFloat.setText(arrs[1]);
        }else {
            mInterger.setText(price);
            mFloat.setText("00");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recharge_back:
                dismiss();
                break;
            case R.id.recharge_finish:
                dismiss();
                break;
        }
    }
}
