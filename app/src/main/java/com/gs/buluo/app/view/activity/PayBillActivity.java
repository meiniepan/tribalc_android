package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.DiscountListAdapter;
import com.gs.buluo.app.bean.Privilege;
import com.gs.buluo.app.bean.PrivilegeResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.panel.NewPayPanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/17.
 */

public class PayBillActivity extends BaseActivity implements NewPayPanel.OnPayFinishListener {
    @Bind(R.id.pay_account)
    EditText etAccount;
    @Bind(R.id.pay_discount_list)
    ListView listView;
    @Bind(R.id.pay_symbol)
    View vSymbol;
    @Bind(R.id.pay_account_final)
    TextView tvAccoutnFinal;
    @Bind(R.id.pay_store_name)
    TextView tvStoreName;
    @Bind(R.id.pay_button)
    Button mButton;
    private List<Privilege> priData = new ArrayList<>();
    private double curremtTime;
    private DiscountListAdapter adapter;
    private String storeId;
    private BigDecimal totalFee;
    private String storeName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mButton.setEnabled(false);
        CommonUtils.setHint(etAccount, "输入金额", getResources().getDimensionPixelSize(R.dimen.dimens_16sp));
        storeId = getIntent().getStringExtra(Constant.STORE_ID);
        if (storeId == null) return;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        curremtTime = (double) (hour * 60 * 60 + minute * 60);

        findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), PrivilegeActivity.class);
                intent.putExtra(Constant.STORE_NAME, storeName);
                intent.putExtra(Constant.STORE_ID, storeId);
                startActivity(intent);
            }
        });

        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getDiscountInfo(storeId, TribeApplication.getInstance().getUserInfo().getId(), true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<PrivilegeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<PrivilegeResponse> pResponse) {
                        setData(pResponse.data);
                    }
                });

        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    if (adapter != null) adapter.setAmount(0 + "");
                    setActualResult(0);
                    mButton.setEnabled(false);
                    return;
                }
                mButton.setEnabled(true);
                try {
                    setActualResult(Double.parseDouble(s.toString()));
                    if (adapter != null) adapter.setAmount(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtils.ToastMessage(getCtx(), "金额输入不正确");
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pay_bill;
    }

    public void setData(PrivilegeResponse data) {
        storeName = data.storeName;
        tvStoreName.setText(storeName);
        if (data.privileges != null && data.privileges.size() != 0) {
            priData = data.privileges;
            ArrayList<Privilege> result = new ArrayList<>();
            for (Privilege privilege : data.privileges) {                  //删除不在时间段内的优惠信息
                if ((curremtTime > privilege.activityTime.get(0) && curremtTime < privilege.activityTime.get(1)) ||
                        ((privilege.activityTime.get(0) > privilege.activityTime.get(1)) &&
                                (curremtTime > privilege.activityTime.get(0) || curremtTime < privilege.activityTime.get(1)))) {
                    result.add(privilege);
                }
            }
            if (result.size() == 0) return;
            Collections.sort(result, new Comparator<Privilege>() {
                @Override
                public int compare(Privilege o1, Privilege o2) {
                    return o1.condition.subtract(o2.condition).intValue();
                }
            });
            adapter = new DiscountListAdapter(this, result);
            listView.setAdapter(adapter);
        }
    }

    public void setActualResult(double account) {
        totalFee = new BigDecimal(account);
        BigDecimal previous = totalFee;
        BigDecimal temp = totalFee;
        for (Privilege privilege : priData) {
            if ((curremtTime > privilege.activityTime.get(0) && curremtTime < privilege.activityTime.get(1)) ||
                    ((privilege.activityTime.get(0) > privilege.activityTime.get(1)) &&
                            (curremtTime > privilege.activityTime.get(0) || curremtTime < privilege.activityTime.get(1)))) {
                if (!(totalFee.compareTo(privilege.condition) == -1)) {
                    switch (privilege.type) {
                        case DISCOUNT:
                            temp = totalFee.multiply(privilege.value);
                            break;
                        case REDUCE:
                            temp = totalFee.subtract(privilege.value);
                            break;
                        case ALIQUOT:
                            BigDecimal multiple = totalFee.divide(privilege.condition).setScale(0, BigDecimal.ROUND_DOWN);
                            temp = totalFee.subtract(privilege.value.multiply(multiple));
                            break;
                    }
                    if (temp.compareTo(previous) == -1) {
                        previous = temp;
                    }
                }
            }
        }
        totalFee = previous;
        tvAccoutnFinal.setText("¥ " + totalFee.floatValue());
    }

    public void doPay(View view) {
        NewPayPanel payPanel = new NewPayPanel(this, this);
        payPanel.setPayBeforeDiscount(etAccount.getText().toString().trim());
        payPanel.setData(totalFee.floatValue()+"", storeId, "face2face");
        payPanel.show();
    }

    @Override
    public void onPaySuccess() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPayFail(ApiException e) {
        if (e.getCode() == 412) {
            createDialog();
        } else {
            ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
        }
    }

    private AlertDialog checkDialog;

    private void createDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.myCorDialog);
        View view = View.inflate(this, R.layout.pay2merchant_error, null);
        builder.setView(view);
        builder.setCancelable(true);
        Button button = (Button) view.findViewById(R.id.btn_pay2m_error_finish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDialog.dismiss();
                finish();
            }
        });
        TextView tvContent = (TextView) view.findViewById(R.id.error_dialog_content);
        tvContent.setText("今日优惠买单金额已达上限，\n 暂不可用优惠买单进行支付！");
        checkDialog = builder.create();
        checkDialog.show();
        WindowManager.LayoutParams params = checkDialog.getWindow().getAttributes();
        params.width = DensityUtils.dip2px(this, 229);
        params.height = DensityUtils.dip2px(this, 223);
        checkDialog.getWindow().setAttributes(params);
    }
}
