package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.CreditRepayAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.panel.NewPayPanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditRepaymentActivity extends BaseActivity {
    @Bind(R.id.credit_repay_card_list)
    ListView listView;
    private CreditRepayAdapter adapter;

    @Bind(R.id.credit_pay_balance)
    RadioButton radioButton;
    @Bind(R.id.ll_add__bank_card)
    View addView;
    @Bind(R.id.credit_repay_should)
    TextView tvShouldRepay;

    @Bind(R.id.credit_repay_input)
    EditText evRepay;
    BankCard mBankCard;

    private PayChannel payChannel = PayChannel.BALANCE;

    ArrayList<BankCard> list = new ArrayList<>();
    private String creditBillId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String shouldRepay = getIntent().getStringExtra(Constant.CREDIT_BALANCE);
        tvShouldRepay.setText(shouldRepay);
        adapter = new CreditRepayAdapter(getCtx(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                radioButton.setChecked(false);
                mBankCard = list.get(position);
                payChannel = PayChannel.BF_BANKCARD;
                adapter.setPos(position);
            }
        });

        findViewById(R.id.ll_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payChannel = PayChannel.BALANCE;
                radioButton.setChecked(true);
            }
        });
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(), AddBankCardActivity.class));
            }
        });

        TribeRetrofit.getInstance().createApi(MoneyApis.class).getCurrentCreditBill(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CreditBill>>() {
                    @Override
                    public void onNext(BaseResponse<CreditBill> creditBillBaseResponse) {
                        creditBillId = creditBillBaseResponse.data.id;
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(BaseResponse<List<BankCard>> listBaseResponse) {
                        list.clear();
                        list.addAll(listBaseResponse.data);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit_repayment;
    }

    public void repayMoney(View view) {
        if (evRepay.length()==0){
            ToastUtils.ToastMessage(getCtx(),"请输入还款金额");
            return;
        }
        NewPayPanel payPanel = new NewPayPanel(this);
        payPanel.setData(evRepay.getText().toString().trim(),creditBillId,"credit");
        payPanel.show();
    }
}
