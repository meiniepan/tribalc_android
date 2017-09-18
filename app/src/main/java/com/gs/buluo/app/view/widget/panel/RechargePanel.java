package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.gs.buluo.app.bean.BankCardBindTypeEnum;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.eventbus.TopupEvent;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.BFUtil;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.activity.AddBankCardActivity;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/29.
 */
public class RechargePanel extends Dialog implements View.OnClickListener, BFUtil.OnBFPayStatusListener {
    Context mContext;
    @Bind(R.id.recharge_float)
    MoneyTextView mFloat;
    //    @Bind(R.id.recharge_pay_wechat)
    //    RadioButton rbWeChat;
    @Bind(R.id.card_list)
    ListView cardList;
    //    @Bind(R.id.recharge_pay_ali)
    //    RadioButton rbAli;
    @Bind(R.id.recharge_input)
    EditText mInput;
    @Bind(R.id.recharge_finish)
    TextView tvFinish;

    @Bind(R.id.recharge_add_card)
    View addGroup;
    private RadioButton oldView;

    private LiteBankCardListAdapter adapter;
    private BankCard mBankCard;
    private int last_item = -1;
    private static final int DECIMAL_DIGITS = 2;//小数的位数
    private String targetId;

    private String prepayid; // 微信支付相关,暂时不用

    public RechargePanel(Context context, String targetId) {
        super(context, R.style.my_dialog);
        this.targetId = targetId;
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
        tvFinish.setOnClickListener(this);

        adapter = new LiteBankCardListAdapter(mContext);
        getBankCards();
        setInputDecimal();

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
        LoadingDialog.getInstance().show(mContext, mContext.getString(R.string.loading), true);
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
                        adapter.setData(data);
                        cardList.setAdapter(adapter);
                        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (!(data.get(i).bindType == BankCardBindTypeEnum.NORMAL)) return;
                                if (last_item != -1 && last_item != i) oldView.setChecked(false);
                                final RadioButton radioButton = (RadioButton) view.findViewById(R.id.recharge_pay_order);
                                radioButton.setChecked(true);
                                oldView = radioButton;
                                last_item = i;
                                mBankCard = data.get(i);
                            }
                        });
                    }

                });
    }

    public void setData(double amount) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        String format = nf.format(amount);
        mFloat.setMoneyText(format);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_back:
                dismiss();
                break;
            case R.id.recharge_finish:
                String inputNum = mInput.getText().toString().trim();
//                if (inputNum.length() <= 0) {
//                    ToastUtils.ToastMessage(mCtx, "请输入充值金额");
//                    return;
//                }
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
                getTopUpResult(new ValueBody(prepayid))
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
        OrderPayment data = new OrderPayment();
        data.totalAmount = num;
        data.ownerAccountId = targetId;
        new BFUtil().doBFPay(mContext, data, mBankCard, this);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }

    private void setInputDecimal() {
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
                        mInput.setText(s);
                        mInput.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mInput.setText(s);
                    mInput.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mInput.setText(s.subSequence(0, 1));
                        mInput.setSelection(1);
                        return;
                    }
                }
                if (s.toString().length() <= 0 || Float.parseFloat(s.toString()) <= 0) {
                    tvFinish.setEnabled(false);
                } else tvFinish.setEnabled(true);
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
    public void onBFSuccess() {
        ToastUtils.ToastMessage(getContext(), R.string.recharge_success);
        dismiss();
    }
}
