package com.gs.buluo.app.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.gs.buluo.app.bean.RequestBodyBean.WithdrawRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.panel.PasswordPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.CustomAlertDialog;

import java.math.BigDecimal;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by hjn on 2017/5/4.
 */

public class CashActivity extends BaseActivity {
    @Bind(R.id.card_icon)
    ImageView ivIcon;
    @Bind(R.id.card_name)
    TextView tvName;
    @Bind(R.id.card_end_number)
    TextView tvEnd;
    @Bind(R.id.card_withdraw_amount)
    EditText etWithdraw;
    @Bind(R.id.card_offer_money)
    TextView tvAmount;
    @Bind(R.id.withdraw_finish)
    Button btWithdraw;
    @Bind(R.id.cash_poundage)
    TextView tvPoundage;

    private String pwd;
    private String chooseCardId;
    private float availableAccount;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        float amount = Float.parseFloat(intent.getStringExtra(Constant.WALLET_AMOUNT));
        pwd = intent.getStringExtra(Constant.WALLET_PWD);
        float poundage = intent.getFloatExtra(Constant.POUNDAGE, 0);
        tvPoundage.setText(poundage + "");

        BigDecimal amountDecimal=new BigDecimal(amount+"");
        BigDecimal poundageDecimal=new BigDecimal(poundage+"");

        float floatValue = amountDecimal.subtract(poundageDecimal).floatValue();
        availableAccount =   floatValue > 0 ? floatValue : 0;
        tvAmount.setText(availableAccount + "");

        findViewById(R.id.card_withdraw_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(chooseCardId)) {
                    ToastUtils.ToastMessage(getCtx(), getString(R.string.please_choose_card));
                    return;
                }
                doWithDraw(availableAccount);
            }
        });
        findViewById(R.id.card_choose_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), BankCardActivity.class);
                intent.putExtra(Constant.CASH_FLAG, true);
                startActivityForResult(intent, 201);
            }
        });

        etWithdraw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btWithdraw.setEnabled(true);
                } else {
                    btWithdraw.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_cash;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK) {
            findViewById(R.id.card_choose_area).setVisibility(View.GONE);
            BankCard card = data.getParcelableExtra(Constant.BANK_CARD);
            chooseCardId = card.id;
            int resId = 0;
            try {
                resId = (Integer) R.mipmap.class.getField("bank_logo_" + card.bankCode.toLowerCase()).get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (resId != 0)
                ivIcon.setImageResource(resId);
            else
                ivIcon.setImageResource(R.mipmap.default_pic);

            tvName.setText(card.bankName);
            tvEnd.setText(card.bankCardNum.substring(card.bankCardNum.length() - 4, card.bankCardNum.length()));
        }
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getCtx()).setTitle("提示").setMessage("您还没有设置支付密码，请先去设置密码")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCtx().startActivity(new Intent(getCtx(), UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    public void clickWithdraw(View view) {// click button to withdraw
        doWithDraw(Float.parseFloat(etWithdraw.getText().toString().trim()));
    }

    public void doWithDraw(final float number) {
        if (TextUtils.isEmpty(pwd)) {
            showAlert();
            return;
        }
        if (chooseCardId == null) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.choose_bank_card));
            return;
        }
        if (number > availableAccount) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.withdraw_too_much));
            return;
        }
        PasswordPanel passwordPanel = new PasswordPanel(this, pwd, new PasswordPanel.OnPasswordPanelDismissListener() {
            @Override
            public void onPasswordPanelDismiss(boolean successful) {
                if (successful) {
                    finishWithDraw(number);
                }
            }
        });
        passwordPanel.show();
    }

    private void finishWithDraw(float number) {
        if (TextUtils.isEmpty(chooseCardId)) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.please_choose_card));
            return;
        }
        WithdrawRequestBody body = new WithdrawRequestBody();
        body.amount = number;
        body.bankCardId = chooseCardId;

        TribeRetrofit.getInstance().createApi(MoneyApis.class).withdrawCash(TribeApplication.getInstance().getUserInfo().getId(), body)
                .subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> codeResponseBaseResponse) {
                        ToastUtils.ToastMessage(getCtx(), getString(R.string.withdraw_success));
                        startActivity(new Intent(getCtx(), WalletActivity.class));
                    }
                });
    }
}
