package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankCardBindTypeEnum;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.panel.VerifyCodePanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/23.
 */
public class AddBankCardActivity extends BaseActivity {
    @BindView(R.id.card_add_bank_name)
    TextView etBankName;
    @BindView(R.id.card_add_bank_num)
    EditText etNum;
    @BindView(R.id.card_add_name)
    EditText etName;
    @BindView(R.id.card_add_phone)
    EditText etPhone;
    @BindView(R.id.card_add_finish)
    Button btFinish;

    Context mContext;
    private BankCardBindTypeEnum typeEnum;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = this;
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddCard();
            }
        });
        findViewById(R.id.card_add_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BankPickActivity.class);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE);
            }
        });
    }

    private BankCard resultCard;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            resultCard = data.getParcelableExtra(Constant.ForIntent.FLAG);
            etBankName.setText(resultCard.bankName);
            typeEnum = resultCard.bindType;
            if (typeEnum == BankCardBindTypeEnum.WITHDRAW) {
                btFinish.setText(R.string.confirm_bind);
            } else {
                btFinish.setText(R.string.get_verify_code);
            }
        }
    }

    private void doAddCard() {
        if (etNum.length() == 0 || etBankName.length() == 9 || etName.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), R.string.not_complete);
            return;
        }
        final BankCard card = new BankCard();
        card.bankCardNum = etNum.getText().toString().trim();
        card.bankName = etBankName.getText().toString().trim();
        card.userName = etName.getText().toString().trim();
        card.phone = etPhone.getText().toString().trim();
        card.bindType = typeEnum;
        card.personal = true;
        card.maxPaymentAmount = resultCard.maxPaymentAmount;
        card.maxWithdrawAmount = resultCard.maxWithdrawAmount;
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareAddBankCard(TribeApplication.getInstance().getUserInfo().getId(), card).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankCard>>() {
                               @Override
                               public void onNext(BaseResponse<BankCard> bankCardBaseResponse) {
                                   dealWithResult(bankCardBaseResponse);
                               }

                               @Override
                               public void onFail(ApiException e) {
                                   if (!TextUtils.isEmpty(e.getDisplayMessage())) {
                                       ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                                   } else {
                                       dealWithCode(e);
                                   }
                               }
                           }
                );
    }

    private void dealWithResult(BaseResponse<BankCard> bankCardBaseResponse) {
        if (BankCardBindTypeEnum.WITHDRAW == typeEnum) {
            ToastUtils.ToastMessage(getCtx(), R.string.bind_success);
            startActivity(new Intent(getCtx(), BankCardActivity.class));
        } else {
            BankCard data = bankCardBaseResponse.data;
            VerifyCodePanel verifyPanel = new VerifyCodePanel(mContext, data);
            verifyPanel.show();
        }
    }

    public void dealWithCode(ApiException e) {
        switch (e.getCode()) {
            case 400:
                ToastUtils.ToastMessage(getCtx(), R.string.phone_format_error);
                break;
            case 403:
                ToastUtils.ToastMessage(getCtx(), R.string.bankcard_owner_error);
                break;
            case 409:
                ToastUtils.ToastMessage(getCtx(), R.string.bank_card_binded);
                break;
            case 412:
                ToastUtils.ToastMessage(getCtx(), R.string.un_auth);
                break;
            case 424:
                ToastUtils.ToastMessage(getCtx(), R.string.bind_error);
                break;
            case 404:
                ToastUtils.ToastMessage(getCtx(), "银行卡号不正确");
                break;
            default:
                ToastUtils.ToastMessage(getCtx(), R.string.net_error);
                break;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_bank_card;
    }
}
