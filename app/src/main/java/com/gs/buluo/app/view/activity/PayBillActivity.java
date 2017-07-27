package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.DiscountListAdapter;
import com.gs.buluo.app.bean.Privilege;
import com.gs.buluo.app.bean.PrivilegeResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.panel.NewPayPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/17.
 */

public class PayBillActivity extends BaseActivity implements NewPayPanel.OnPayPanelDismissListener {
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
    private List<Privilege> priData = new ArrayList<>();
    private double curremtTime;
    private DiscountListAdapter adapter;
    private String storeId;
    private BigDecimal totalFee;
    private String storeName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        CommonUtils.setHint(etAccount, "输入金额", getResources().getDimensionPixelSize(R.dimen.dimens_16sp));
        storeId = getIntent().getStringExtra(Constant.STORE_ID);
        if (storeId == null) return;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        curremtTime = (double) (hour * 60 * 60 + minute * 60);

        findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), DiscountActivity.class);
                intent.putExtra(Constant.STORE_NAME, storeName);
                intent.putExtra(Constant.STORE_ID, storeId);
                startActivity(intent);
            }
        });

        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getDiscountInfo(storeId, true)
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
                    setActualResult(0f);
                    return;
                }
                try {
                    setActualResult(Float.parseFloat(s.toString()));
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
        priData = data.privileges;
        if (data.privileges == null) return;
        ArrayList<Privilege> result = new ArrayList<>();
        for (Privilege privilege : data.privileges) {                  //删除不在时间段内的优惠信息
            if ((curremtTime > privilege.activityTime.get(0) && curremtTime < privilege.activityTime.get(1)) ||
                    ((privilege.activityTime.get(0) > privilege.activityTime.get(1)) &&
                            (curremtTime > privilege.activityTime.get(0) || curremtTime < privilege.activityTime.get(1)))) {
                result.add(privilege);
            }
        }
        Collections.sort(result, new Comparator<Privilege>() {
            @Override
            public int compare(Privilege o1, Privilege o2) {
                return o1.condition.subtract(o2.condition).intValue();
            }
        });
        adapter = new DiscountListAdapter(this, result);
        listView.setAdapter(adapter);
    }

    public void setActualResult(float account) {
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
        payPanel.setData(etAccount.getText().toString().trim(), storeId,"face2face");
        payPanel.show();
    }

    @Override
    public void onPayPanelDismiss() { //彈出警告后回到此方法
        finish();
    }
}
