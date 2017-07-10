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
import com.gs.buluo.app.bean.RequestBodyBean.RentProtocolWithholdInfo;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/06/28.
 */
public class AddRentWithholdActivity extends BaseActivity {
    @Bind(R.id.tv_withhold_card_add_bank_name)
    TextView tvBankName;
    @Bind(R.id.et_withhold_card_add_bank_num)
    EditText etNum;
    @Bind(R.id.et_withhold_add_name)
    EditText etName;
    @Bind(R.id.et_withhold_card_add_phone)
    EditText etPhone;
    @Bind(R.id.et_withhold_add_id)
    EditText etIdNo;
    private String bankCode;
    private String protocolId;
    Context mContext;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = this;
        protocolId = getIntent().getStringExtra("protocolId");
        if(getIntent().getIntExtra("from",0) == 2){
            setDataFromOld();
        }
        findViewById(R.id.btn_withhold_card_add_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddCard();
            }
        });
        findViewById(R.id.ll_withhold_card_add_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BankPickActivity.class);
                intent.putExtra(Constant.BANK_TYPE, 1);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE);
            }
        });
    }

    private void setDataFromOld() {
        bankCode = getIntent().getStringExtra("bankCode");
        etNum.setText(getIntent().getStringExtra("bankCardNum"));
        tvBankName.setText(getIntent().getStringExtra("bankName"));
        etName.setText(getIntent().getStringExtra("userName"));
        etPhone.setText(getIntent().getStringExtra("phone"));
        etIdNo.setText(getIntent().getStringExtra("idNo"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String name = ((BankCard) data.getExtras().get(Constant.ForIntent.FLAG)).bankName;
            tvBankName.setText(name);
            bankCode = ((BankCard) data.getExtras().get(Constant.ForIntent.FLAG)).bankCode;
        }
    }

    private void doAddCard() {
        final RentProtocolWithholdInfo withholdInfo = new RentProtocolWithholdInfo();
        withholdInfo.bankCardNum = etNum.getText().toString().trim();
        withholdInfo.bankName = tvBankName.getText().toString().trim();
        withholdInfo.userName = etName.getText().toString().trim();
        withholdInfo.phone = etPhone.getText().toString().trim();
        withholdInfo.idNo = etIdNo.getText().toString().trim();
        withholdInfo.bankCode = bankCode;
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).
                updateWithholdInfo(protocolId, withholdInfo, TribeApplication.getInstance().getUserInfo().getId()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                               @Override
                               public void onNext(BaseResponse bankCardBaseResponse) {
                                   ToastUtils.ToastMessage(mContext,R.string.success);
                                   finish();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                                   finish();
                               }
                           }
                );
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_withhold;
    }
}