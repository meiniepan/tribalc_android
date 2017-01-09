package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.utils.ToastUtils;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/23.
 */
public class AddBankCardActivity extends BaseActivity {
    @Bind(R.id.card_add_bank_name)
    TextView etBankName;
    @Bind(R.id.card_add_bank_num)
    EditText etNum;
    @Bind(R.id.card_add_name)
    EditText etName;
    @Bind(R.id.card_add_phone)
    EditText etPhone;
    @Bind(R.id.card_add_verify)
    EditText etVerify;

    @Bind(R.id.card_send_verify)
    TextView sendVerify;
    Context mContext;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext=this;
        findViewById(R.id.card_bind_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.card_add_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddCard();
            }
        });
        findViewById(R.id.card_add_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext,BankPickActivity.class), Constant.ForIntent.REQUEST_CODE);
            }
        });
        sendVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerifyCode(etPhone.getText().toString().trim());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            String name = data.getStringExtra(Constant.ForIntent.FLAG);
            etBankName.setText(name);
        }
    }

    private void sendVerifyCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.ToastMessage(this, R.string.phone_not_empty);
            return;
        }
        new MainModel().doVerify(phone, new Callback<BaseCodeResponse<CodeResponse>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<CodeResponse>> call, Response<BaseCodeResponse<CodeResponse>> response) {
                dealWithIdentify(response.body().code);
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<CodeResponse>> call, Throwable t) {
                ToastUtils.ToastMessage(AddBankCardActivity.this, R.string.connect_fail);
            }
        });
    }


    private void dealWithIdentify(int code) {
        switch (code) {
            case 202:
                sendVerify.setText("60s");
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        sendVerify.setClickable(false);
                        sendVerify.setText(millisUntilFinished / 1000 + "秒");
                    }
                    @Override
                    public void onFinish() {
                        sendVerify.setText("获取验证码");
                        sendVerify.setClickable(true);
                    }
                }.start();
                break;
            case 400:
                ToastUtils.ToastMessage(this, getString(R.string.wrong_number));
                break;
        }
    }

    private void doAddCard() {
        String vCode = etVerify.getText().toString().trim();
        BankCard card = new BankCard();
        card.bankCardNum = etNum.getText().toString().trim();
        card.bankName = etBankName.getText().toString().trim();
        card.userName = etName.getText().toString().trim();
        card.phone = etPhone.getText().toString().trim();
        MoneyModel moneyModel = new MoneyModel();
        moneyModel.addBankCard(TribeApplication.getInstance().getUserInfo().getId(), vCode, card, new Callback<BaseCodeResponse<CodeResponse>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<CodeResponse>> call, Response<BaseCodeResponse<CodeResponse>> response) {
                if (response.body() != null && response.body().code == 201&&response.body() != null && response.body().code == 201) {
                    startActivity(new Intent(AddBankCardActivity.this, BankCardActivity.class));
                    finish();
                } else if (response.body().code == 401) {
                    ToastUtils.ToastMessage(AddBankCardActivity.this, R.string.wrong_verify);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<CodeResponse>> call, Throwable t) {
                ToastUtils.ToastMessage(AddBankCardActivity.this, R.string.connect_fail);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_bank_card;
    }
}
