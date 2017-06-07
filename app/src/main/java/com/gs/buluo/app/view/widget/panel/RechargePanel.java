package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import com.baofoo.sdk.device.BaofooDeviceFingerPrint;
import com.baofoo.sdk.device.environment.Environment;
import com.baofoo.sdk.device.interfaces.ResultInterfaces;
import com.gs.buluo.app.BuildConfig;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.LiteBankCardListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankOrderResponse;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PaySessionResponse;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.eventbus.TopupEvent;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.AddBankCardActivity;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
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

    @Bind(R.id.recharge_add_card)
    View addGroup;
    private RadioButton oldView;

    private OrderBean.PayChannel payMethod;
    private String prepayid;
    private LiteBankCardListAdapter adapter;
    private BankCard mBankCard;
    private int last_item = -1;
    private BaofooDeviceFingerPrint baofooDeviceFingerPrint;

    public RechargePanel(Context context) {
        super(context, R.style.my_dialog);
        mContext = context;
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
        getBankCards();

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

        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), AddBankCardActivity.class));
                dismiss();
            }
        });
    }

    private void getBankCards() {
        LoadingDialog.getInstance().show(mContext,mContext.getString(R.string.loading),true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(final BaseResponse<List<BankCard>> response) {
                        final List<BankCard> data = response.data;
                        if (data.size() == 0) {
                            addGroup.setVisibility(View.VISIBLE);
                            return;
                        }
//                        int intValue = SharePreferenceManager.getInstance(getContext()).getIntValue(Constant.LAST_ITEM);
//                        intValue = intValue==-1? 0:intValue;
//                        mBankCard = data.get(intValue);

                        adapter.setData(data);
                        cardList.setAdapter(adapter);
                        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (last_item != -1 && last_item != i) oldView.setChecked(false);
                                final RadioButton radioButton = (RadioButton) view.findViewById(R.id.recharge_pay_order);
                                radioButton.setChecked(true);
                                oldView = radioButton;
                                last_item = i;
                                SharePreferenceManager.getInstance(getContext()).setValue(Constant.LAST_ITEM, last_item);
                                mBankCard = data.get(i);
                            }
                        });
                    }

                });
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
//                if (payMethod == OrderBean.PayChannel.WEICHAT) {
//                    beginDoRecharge(inputNum);
//                }
                beginDoRecharge(inputNum);
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

    private void beginDoRecharge(final String num) {
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getPrepareOrderInfo(TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<PaySessionResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                        LoadingDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onNext(BaseResponse<PaySessionResponse> response) {
                        doNextPrepare(response.data.result, num);
                    }
                });
    }

    private void doNextPrepare(final PaySessionResponse.PaySessionResult data, final String num) {
        if (BuildConfig.API_SERVER_URL.contains("dev")) {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), data.sessionId, Environment.TEST_DEVICE_SERVER);
        } else {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), data.sessionId, Environment.PRODUCT_DEVICE_SERVER);
        }
        baofooDeviceFingerPrint.execute();
        baofooDeviceFingerPrint.onRespResult(new ResultInterfaces() {
            @Override
            public void respSuccess(String s) {
                doFinalPrepare(num, data.paymentId);
                if (baofooDeviceFingerPrint != null) {
                    baofooDeviceFingerPrint.releaseResource();//释放资源；
                }
            }

            @Override
            public void respError(String s) {
                Log.e("baofoo", "respError: " + s);
                ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                LoadingDialog.getInstance().dismissDialog();
                if (baofooDeviceFingerPrint != null) {
                    baofooDeviceFingerPrint.releaseResource();//释放资源；
                }
            }
        });
    }

    private void doFinalPrepare(final String num, String paymentId) {
        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.bankCardId = mBankCard.id;
        prepareOrderRequest.totalFee = num;
        prepareOrderRequest.paymentId = paymentId;
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareOrder(TribeApplication.getInstance().getUserInfo().getId(), prepareOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankOrderResponse>>(false) {
                    @Override
                    public void onNext(BaseResponse<BankOrderResponse> response) {
                        new BfRechargeVerifyCodePanel(mContext, mBankCard, response.data.result, num, RechargePanel.this).show();
                    }
                });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
