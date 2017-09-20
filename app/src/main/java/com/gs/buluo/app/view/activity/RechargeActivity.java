package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.RequestBodyBean.WxPayRequest;
import com.gs.buluo.app.utils.BFUtil;
import com.gs.buluo.app.utils.WXUtils;
import com.gs.buluo.app.view.widget.panel.PayChoosePanel;
import com.gs.buluo.common.utils.ToastUtils;

import butterknife.Bind;

/**
 * Created by hjn on 2017/9/19.
 */

public class RechargeActivity extends BaseActivity implements PayChoosePanel.onChooseFinish, BFUtil.OnBFPayStatusListener {
    @Bind(R.id.recharge_pay_icon)
    ImageView rechargePayIcon;
    @Bind(R.id.recharge_pay_method)
    TextView rechargePayMethod;
    @Bind(R.id.recharge_pay_method_note)
    TextView rechargePayMethodNote;
    @Bind(R.id.recharge_input)
    EditText rechargeInput;
    @Bind(R.id.bt_recharge)
    Button btFinish;
    private PayChoosePanel choosePanel;
    private String targetId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.white);
        targetId = getIntent().getStringExtra(Constant.TARGET_ID);
        if (TextUtils.isEmpty(targetId)) {
            targetId = TribeApplication.getInstance().getUserInfo().getId();
        }
        findViewById(R.id.recharge_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePanel();
            }
        });
        setInputDecimal();
        choosePanel = new PayChoosePanel(this, -1, this);
    }

    private void showChoosePanel() {
        choosePanel.show();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_recharge;
    }


    private static final int DECIMAL_DIGITS = 2;//小数的位数
    private void setInputDecimal() {
        rechargeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
                        rechargeInput.setText(s);
                        rechargeInput.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    rechargeInput.setText(s);
                    rechargeInput.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        rechargeInput.setText(s.subSequence(0, 1));
                        rechargeInput.setSelection(1);
                        return;
                    }
                }
                if (s.toString().length() <= 0 || Float.parseFloat(s.toString()) <= 0) {
                    btFinish.setEnabled(false);
                } else {
                    btFinish.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private PayChannel payChannel;
    private BankCard mBankCard;

    @Override
    public void onChoose(PayChannel payChannel, BankCard bankCard, String bankName) {
        mBankCard = bankCard;
        this.payChannel = payChannel;
        switch (payChannel) {
            case WEICHAT:
                rechargePayMethod.setText(R.string.pay_wechat);
                rechargePayIcon.setImageResource(R.mipmap.pay_wechat);
                rechargePayMethodNote.setVisibility(View.GONE);
                break;
            case BF_BANKCARD:
                rechargePayMethod.setText(bankName);
                rechargePayIcon.setImageResource(bankCard.bankIcon);
                rechargePayMethodNote.setVisibility(View.VISIBLE);
                rechargePayMethodNote.setText("单笔交易限额" + bankCard.maxPaymentAmount + "元");
                break;
        }
    }

    public void doRecharge(View view) {
        switch (payChannel) {
            case WEICHAT:
                WxPayRequest request = new WxPayRequest();
                request.targetId = targetId;
                request.totalFee = rechargeInput.getText().toString().trim();
//                request.paymentId=    ???
                WXUtils.getInstance().payInWechat(request);
                break;
            case BF_BANKCARD:
                OrderPayment data = new OrderPayment();
                data.totalAmount = rechargeInput.getText().toString().trim();
                data.ownerAccountId = targetId;
                new BFUtil().doBFPay(this, data, mBankCard, this);
                break;
        }
    }

    @Override
    public void onBFSuccess() {
        ToastUtils.ToastMessage(getCtx(), R.string.recharge_success);
        finish();
    }
}
