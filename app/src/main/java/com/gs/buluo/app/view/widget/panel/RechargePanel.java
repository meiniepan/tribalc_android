package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.LiteBankCardListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.WxPayResponse;
import com.gs.buluo.app.eventbus.TopupEvent;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.WXPayUtils;
import com.gs.buluo.app.view.widget.LoadingDialog;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Response;
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
    @Bind(R.id.recharge_pay_wechat)
    RadioButton rbWeChat;
    @Bind(R.id.card_list)
    ListView cardList;
    //    @Bind(R.id.recharge_pay_ali)
//    RadioButton rbAli;
    @Bind(R.id.recharge_input)
    EditText mInput;
    RadioButton oldView;

    private OrderBean.PayChannel payMethod;
    private String prepayid;
    private LiteBankCardListAdapter adapter;
    private String orderId;

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
                                if (response.data.size() > 0) {
                                    if (oldView != null) oldView.setChecked(false);
                                    final RadioButton radioButton = (RadioButton) adapter.getView(i, view, cardList).findViewById(R.id.recharge_pay_order);
                                    radioButton.setChecked(true);
                                    oldView = radioButton;
                                    if (i == 0) {
                                        orderId = "weChart";
                                    } else {
                                        orderId = response.data.get(i-1).id;
                                    }
                                    Toast.makeText(mContext, orderId, Toast.LENGTH_SHORT).show();
                                }
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
        rbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //rbAli.setChecked(false);
                    rbWeChat.setChecked(true);
                    payMethod = OrderBean.PayChannel.WEICHAT;
                }
            }
        });
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
                LoadingDialog.getInstance().show(mContext, "充值中", true);
                if (payMethod == OrderBean.PayChannel.WEICHAT) {
                    doRecharge(inputNum);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void rechargeSuccess(TopupEvent event) {
        new MoneyModel().getWXRechargeResult(prepayid, new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                ToastUtils.ToastMessage(mContext, "充值成功");
                dismiss();
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(mContext, R.string.recharge_fail);
                dismiss();
            }
        });
    }

    private void doRecharge(String num) {
        new MoneyModel().topUpInWx(num, new TribeCallback<WxPayResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<WxPayResponse>> response) {
                prepayid = response.body().data.prepayid;
                WXPayUtils.getInstance().doPay(response.body().data);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<WxPayResponse> body) {
                LoadingDialog.getInstance().dismissDialog();
                ToastUtils.ToastMessage(mContext, R.string.connect_fail);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
