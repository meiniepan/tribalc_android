package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.panel.VerifyCodePanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    Context mContext;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = this;
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
                startActivityForResult(new Intent(mContext, BankPickActivity.class), Constant.ForIntent.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra(Constant.ForIntent.FLAG);
            etBankName.setText(name);
        }
    }



    private void doAddCard() {
        final BankCard card = new BankCard();
        card.bankCardNum = etNum.getText().toString().trim();
        card.bankName = etBankName.getText().toString().trim();
        card.userName = etName.getText().toString().trim();
        card.phone = etPhone.getText().toString().trim();
        card.bankAddress = "asdusahdkjashdk";
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareAddBankCard(TribeApplication.getInstance().getUserInfo().getId(), card).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankCard>>() {
                    @Override
                    public void onNext(BaseResponse<BankCard> bankCardBaseResponse) {
                        BankCard data = bankCardBaseResponse.data;

                        VerifyCodePanel verifyPanel = new VerifyCodePanel(mContext, data.id,card.phone);
                        verifyPanel.show();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_bank_card;
    }
}
