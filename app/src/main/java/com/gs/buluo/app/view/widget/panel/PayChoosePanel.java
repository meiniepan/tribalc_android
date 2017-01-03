package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.utils.DensityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2017/1/3.
 */

public class PayChoosePanel extends Dialog{
    Context mContext;
    @Bind(R.id.new_order_pay_balance)
    CheckBox rbBalance;
    @Bind(R.id.new_order_pay_wechat)
    CheckBox rbWeChat;
    @Bind(R.id.new_order_pay_ali)
    CheckBox rbAli;
    private OrderBean.PayChannel payMethod = OrderBean.PayChannel.BALANCE;
    private onChooseFinish onChooseFinish;

    public PayChoosePanel(Context context, onChooseFinish onChooseFinish) {
        super(context, R.style.pay_dialog);
        mContext=context;
        this.onChooseFinish=onChooseFinish;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pay_choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext,450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

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

        rootView.findViewById(R.id.pay_choose_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.pay_choose_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseFinish.onChoose(payMethod);
                dismiss();
            }
        });
    }

    public interface onChooseFinish {
        void onChoose(OrderBean.PayChannel payChannel);
    }
}
