package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.LiteBankCardListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankCardBindTypeEnum;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.view.activity.AddBankCardActivity;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/3.
 */

public class PayChoosePanel extends Dialog implements View.OnClickListener {
    Context mCtx;
    @Bind(R.id.new_order_pay_balance)
    RadioButton rbBalance;
    @Bind(R.id.new_order_pay_wx)
    RadioButton rbWeChat;
    @Bind(R.id.card_list)
    ListView cardList;
    @Bind(R.id.pay_choose_available_balance)
    TextView tvBalance;

    @Bind(R.id.ll_add__bank_card)
    LinearLayout addBankCard;

    private PayChannel payMethod = PayChannel.BALANCE;
    private onChooseFinish onChooseFinish;
    private LiteBankCardListAdapter adapter;
    private BankCard mBankCard;
    private String bankName;

    public PayChoosePanel(Context context, double availableBalance, onChooseFinish onChooseFinish) {
        super(context, R.style.pay_dialog);
        mCtx = context;
        this.onChooseFinish = onChooseFinish;
        initView();
        initData();
        if (availableBalance == -1) {//充值
            findViewById(R.id.ll_balance).setVisibility(View.GONE);
        } else {
            tvBalance.setText(availableBalance + "");
        }
    }

    private void initView() {
        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.pay_choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mCtx, 400);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        rootView.findViewById(R.id.ll_balance).setOnClickListener(this);
        rootView.findViewById(R.id.ll_wx).setOnClickListener(this);
        rootView.findViewById(R.id.ll_add__bank_card).setOnClickListener(this);
        rootView.findViewById(R.id.pay_choose_close).setOnClickListener(this);
    }

    private void initData() {
        adapter = new LiteBankCardListAdapter(mCtx);
        LoadingDialog.getInstance().show(mCtx, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(final BaseResponse<List<BankCard>> response) {
                        setBankList(response);
                    }
                });
    }


    private void setBankList(BaseResponse<List<BankCard>> response) {
        final List<BankCard> data = response.data;
        if (data == null || data.size() == 0) {
            addBankCard.setVisibility(View.VISIBLE);
            return;
        }
        adapter.setData(data);
        cardList.setAdapter(adapter);
        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!(data.get(i).bindType == BankCardBindTypeEnum.NORMAL)) return;
                adapter.setPos(i);
                rbBalance.setChecked(false);
                rbWeChat.setChecked(false);
                mBankCard = data.get(i);
                bankName = mBankCard.bankName + "储蓄卡" + "(" + mBankCard.bankCardNum.substring(mBankCard.bankCardNum.length() - 4, mBankCard.bankCardNum.length()) + ")";
                payMethod = PayChannel.BF_BANKCARD;
                onChooseFinish.onChoose(payMethod, mBankCard, bankName);
                dismiss();
            }
        });
        CommonUtils.setListViewHeightBasedOnChildren(cardList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_balance:
                rbBalance.setChecked(true);
                rbWeChat.setChecked(false);
                payMethod = PayChannel.BALANCE;
                adapter.setPos(-1);
                onChooseFinish.onChoose(payMethod, null, bankName);
                dismiss();
                break;
            case R.id.ll_wx:
                rbBalance.setChecked(false);
                rbWeChat.setChecked(true);
                payMethod = PayChannel.WECHAT;
                adapter.setPos(-1);
                onChooseFinish.onChoose(payMethod, null, bankName);
                dismiss();
                break;
            case R.id.ll_add__bank_card:
                mCtx.startActivity(new Intent(mCtx, AddBankCardActivity.class));
                dismiss();
                break;
            case R.id.pay_choose_close:
                dismiss();
                break;
        }
    }


    public interface onChooseFinish {
        void onChoose(PayChannel payChannel, BankCard bankCard, String bankName);
    }
}
