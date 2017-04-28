package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    CheckBox rbBalance;
    @Bind(R.id.new_order_pay_wechat)
    CheckBox rbWeChat;
    @Bind(R.id.new_order_pay_ali)
    CheckBox rbAli;
    @Bind(R.id.card_list)
    ListView cardList;

    @Bind(R.id.ll_add__bank_card)
    LinearLayout addBankCard;
    private OrderBean.PayChannel payMethod = OrderBean.PayChannel.BALANCE;
    private onChooseFinish onChooseFinish;
    private RadioButton oldView;
    private LiteBankCardListAdapter adapter;
    private BankCard mBankCard;
    private SharedPreferences sharedPreferences;
    private int last_item = -1;

    public PayChoosePanel(Context context, onChooseFinish onChooseFinish) {
        super(context, R.style.pay_dialog);
        mContext = context;
        this.onChooseFinish = onChooseFinish;
        sharedPreferences = mContext.getSharedPreferences("last_item1", Context.MODE_PRIVATE);
        setSpEditor(-1);
        initView();
    }

    private void setSpEditor(int i) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("last_item1", i);
        editor.commit();
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

        adapter = new LiteBankCardListAdapter(mContext);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(final BaseResponse<List<BankCard>> response) {
                        adapter.setData(response.data);
                        cardList.setAdapter(adapter);
                        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                payMethod = OrderBean.PayChannel.BF_BANKCARD;
//                                if (last_item != -1 && last_item != i) oldView.setChecked(false);
                                setAllOrderFalse();
                                final RadioButton radioButton = (RadioButton) view.findViewById(R.id.recharge_pay_order);
                                radioButton.setChecked(true);
                                oldView = radioButton;
                                last_item = i;
                                setSpEditor(last_item);
                                mBankCard = response.data.get(i);
                            }
                        });
                    }
                });


        rbBalance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setSpEditor(-1);
                    setAllOrderFalse();
                    rbBalance.setChecked(true);
                    payMethod = OrderBean.PayChannel.BALANCE;
                }
            }
        });
        rbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbBalance.setChecked(false);
                    rbAli.setChecked(true);
                    rbWeChat.setChecked(false);
                    payMethod = OrderBean.PayChannel.ALIPAY;
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
                    payMethod = OrderBean.PayChannel.WEICHAT;
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

    private void setAllOrderFalse() {
        rbBalance.setChecked(false);
        rbAli.setChecked(false);
        rbWeChat.setChecked(false);
        if (last_item != -1) oldView.setChecked(false);
    }

    public interface onChooseFinish {
        void onChoose(OrderBean.PayChannel payChannel, BankCard bankCard);
    }
}
