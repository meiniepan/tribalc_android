package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.LiteBankCardListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankOrderResponse;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.eventbus.TopupEvent;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/29.
 */
public class RechargePanel extends Dialog implements View.OnClickListener {
    Context mContext;
    @Bind(R.id.recharge_integer)
    TextView mInterger;
    @Bind(R.id.recharge_float)
    TextView mFloat;
    //    @Bind(R.id.recharge_pay_wechat)
//    RadioButton rbWeChat;
    @Bind(R.id.card_list)
    ListView cardList;
    //    @Bind(R.id.recharge_pay_ali)
//    RadioButton rbAli;
    @Bind(R.id.recharge_input)
    EditText mInput;
    private RadioButton oldView;

    private OrderBean.PayChannel payMethod;
    private String prepayid;
    private LiteBankCardListAdapter adapter;
    private BankCard mBankCard;
    private SharedPreferences sharedPreferences;
    private int last_item = -1;

    public RechargePanel(Context context) {
        super(context, R.style.my_dialog);
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences("last_item1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("last_item1", -1);
        editor.commit();
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recharge_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        findViewById(R.id.recharge_back).setOnClickListener(this);
        findViewById(R.id.recharge_finish).setOnClickListener(this);

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
//                                int lastSpPosition = sharedPreferences.getInt("last_item1", -1);
//                                if (last_item == -1 && lastSpPosition != -1 && cardList.getFirstVisiblePosition() <= lastSpPosition &&
//                                        lastSpPosition <= cardList.getLastVisiblePosition()) {
//                                    RadioButton lastRadio = (RadioButton) cardList.getChildAt(lastSpPosition).
//                                            findViewById(R.id.recharge_pay_order);
//                                    lastRadio.setChecked(false);
//                                }
                                if (last_item != -1 && last_item != i) oldView.setChecked(false);
                                final RadioButton radioButton = (RadioButton) view.findViewById(R.id.recharge_pay_order);
                                radioButton.setChecked(true);
                                oldView = radioButton;
                                last_item = i;
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("last_item1", last_item);
                                editor.commit();
//                                if (i == 0) {
//                                    orderId = "weChart";
//                                } else {
//                                    if (response.data.size() > 0) {
                                mBankCard = response.data.get(i);
//                                    }

//                                }
                            }
                        });
                    }
                });


//        rbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rbAli.setChecked(true);
//                    rbWeChat.setChecked(false);
//                    payMethod = OrderBean.PayChannel.ALIPAY;
//                }
//            }
//        });
//        rbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    //rbAli.setChecked(false);
//                    rbWeChat.setChecked(true);
//                    payMethod = OrderBean.PayChannel.WEICHAT;
//                }
//            }
//        });
        EventBus.getDefault().register(this);
    }

    public void setData(String price) {
        if (price == null) return;
        String[] arrs = price.split("\\.");
        if (arrs.length > 1) {
            mInterger.setText(arrs[0]);
            mFloat.setText(arrs[1]);
        } else {
            mInterger.setText(price);
            mFloat.setText("00");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_back:
                dismiss();
                break;
            case R.id.recharge_finish:
                String inputNum = mInput.getText().toString().trim();
                if (inputNum.length() <= 0) {
                    ToastUtils.ToastMessage(mContext, "请输入充值金额");
                    return;
                }
                if (mBankCard == null) {
                    ToastUtils.ToastMessage(mContext, "请选择支付方式");
                    return;
                }
//                LoadingDialog.getInstance().show(mContext, "充值中", true);
//                if (payMethod == OrderBean.PayChannel.WEICHAT) {
//                    doRecharge(inputNum);
//                }
                doRecharge(inputNum);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void rechargeSuccess(TopupEvent event) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getTopUpResult(TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(prepayid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        ToastUtils.ToastMessage(mContext, "充值成功");
                        dismiss();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(mContext, R.string.recharge_fail);
                        dismiss();
                    }
                });
    }

    private void doRecharge(String num) {

//        TribeRetrofit.getInstance().createApi(MoneyApis.class).
//                payInWx(TribeApplication.getInstance().getUserInfo().getId(),new ValueRequestBody(num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseResponse<WxPayResponse>>() {
//                    @Override
//                    public void onNext(BaseResponse<WxPayResponse> response) {
//                        prepayid = response.data.prepayid;
//                        WXPayUtils.getInstance().doPay(response.data);
//                    }
//
//                    @Override
//                    public void onFail(ApiException e) {
//                        super.onFail(e);
//                        LoadingDialog.getInstance().dismissDialog();
//                    }
//                });
        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.bankCardId = mBankCard.id;
        prepareOrderRequest.totalFee = num;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareOrder(TribeApplication.getInstance().getUserInfo().getId(), prepareOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankOrderResponse>>() {
                    @Override
                    public void onNext(BaseResponse<BankOrderResponse> response) {
                        new BfRechargeVerifyCodePanel(mContext, response.data.result,RechargePanel.this).show();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                    }
                });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
