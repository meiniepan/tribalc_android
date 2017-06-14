package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.StoreDesc;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.widget.panel.Pay2mPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/6/8.
 */

public class Pay2MerchantActivity extends BaseActivity {
    @Bind(R.id.merchant_icon)
    SimpleDraweeView icon;
    @Bind(R.id.tv_pay2m_merchant_name)
    TextView tvMerchantName;
    @Bind(R.id.et_pay2m_money)
    EditText etMoney;
    @Bind(R.id.btn_pay2m_pay)
    Button btnPay;
    private Context mContext;
    private String result;
    private String name;
    private static final int DECIMAL_DIGITS = 2;//小数的位数

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = Pay2MerchantActivity.this;
        setInputDecimal();
        result = getIntent().getStringExtra("result");
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreDesc(result, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreDesc>>() {
                    @Override
                    public void onNext(BaseResponse<StoreDesc> response) {
                        FresoUtils.loadImage(response.data.logo, icon);
                        name = response.data.name;
                        tvMerchantName.setText(response.data.name);
                    }

                    @Override
                    public void onError(Throwable e) {
                        etMoney.setEnabled(false);
                        LoadingDialog.getInstance().dismissDialog();
                        ToastUtils.ToastMessage(getCtx(),R.string.merchant_error);
                    }
                });
        findViewById(R.id.pay2m_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Pay2mPanel payBoard = new Pay2mPanel(mContext, null);
                    payBoard.setData(etMoney.getText().toString(), result, name);
                    payBoard.show();
            }
        });
    }

    private void setInputDecimal() {
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
                        etMoney.setText(s);
                        etMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etMoney.setText(s);
                    etMoney.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etMoney.setText(s.subSequence(0, 1));
                        etMoney.setSelection(1);
                        return;
                    }
                }
                if (s.toString().length() <= 0 || Float.parseFloat(s.toString()) <= 0) {
                    btnPay.setEnabled(false);
                }else btnPay.setEnabled(true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.pay2merchant;
    }
}
