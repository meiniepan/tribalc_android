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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.LiteBankCardListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.view.activity.AddBankCardActivity;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/1/3.
 */

public class PayChoosePanel extends Dialog {
    Context mContext;
    @Bind(R.id.new_order_pay_balance)
    RadioButton rbBalance;
    @Bind(R.id.ll_balance)
    LinearLayout llBalance;
    @Bind(R.id.new_order_pay_wechat)
    CheckBox rbWeChat;
    @Bind(R.id.new_order_pay_ali)
    CheckBox rbAli;
    @Bind(R.id.card_list)
    ListView cardList;
    @Bind(R.id.card_list_layout)
    StatusLayout mStatusLayout
            ;

    @Bind(R.id.ll_add__bank_card)
    LinearLayout addBankCard;
    private String payMethod = OrderBean.PayChannel.BALANCE.toString();
    private onChooseFinish onChooseFinish;
    private RadioButton oldView;
    private LiteBankCardListAdapter adapter;
    private BankCard mBankCard;

    public PayChoosePanel(Context context, onChooseFinish onChooseFinish) {
        super(context, R.style.pay_dialog);
        mContext = context;
        this.onChooseFinish = onChooseFinish;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pay_choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext, 450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

//        int intValue = SharePreferenceManager.getInstance(getContext()).getIntValue(Constant.LAST_ITEM);
//        if (intValue == -1) {
            rbBalance.setChecked(true);
            payMethod = OrderBean.PayChannel.BALANCE.toString();
//        }

        adapter = new LiteBankCardListAdapter(mContext);
//        adapter.setPos(intValue);
        mStatusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(final BaseResponse<List<BankCard>> response) {
                        final List<BankCard> data = response.data;
                        adapter.setData(data);
                        cardList.setAdapter(adapter);
                        if (data.size() > 0){mStatusLayout.showContentView();}else{
                            mStatusLayout.showEmptyView(mContext.getString(R.string.nothing));
                        }

                        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                adapter.setPos(i);
//                                SharePreferenceManager.getInstance(getContext()).setValue(Constant.LAST_ITEM, i);
                                rbBalance.setChecked(false);
                                mBankCard = data.get(i);
                                payMethod = mBankCard.bankName + "储蓄卡" + "(" + mBankCard.bankCardNum.substring(mBankCard.bankCardNum.length() - 4, mBankCard.bankCardNum.length()) + ")";
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mStatusLayout.showErrorView(mContext.getString(R.string.net_error));
                    }
                });


        llBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharePreferenceManager.getInstance(getContext()).setValue(Constant.LAST_ITEM, -1);
                rbBalance.setChecked(true);
                payMethod = OrderBean.PayChannel.BALANCE.toString();
                adapter.setPos(-1);
            }
        });
        rbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbBalance.setChecked(false);
                    rbAli.setChecked(true);
                    rbWeChat.setChecked(false);
                    payMethod = OrderBean.PayChannel.ALIPAY.toString();
                }
            }
        });
        rbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbBalance.setChecked(false);
                    rbAli.setChecked(false);
                    rbWeChat.setChecked(true);
                    payMethod = OrderBean.PayChannel.WEICHAT.toString();
                }
            }
        });

        addBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, AddBankCardActivity.class));
                dismiss();
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
                onChooseFinish.onChoose(payMethod, mBankCard);
                dismiss();
            }
        });
    }


    public interface onChooseFinish {
        void onChoose(String payChannel, BankCard bankCard);
    }
}
