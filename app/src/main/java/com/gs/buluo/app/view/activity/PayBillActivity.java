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
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.DiscountListAdapter;
import com.gs.buluo.app.bean.Privilege;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
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

public class PayBillActivity extends BaseActivity {
    @Bind(R.id.pay_account)
    EditText etAccount;
    @Bind(R.id.pay_discount_list)
    ListView listView;
    @Bind(R.id.pay_symbol)
    View vSymbol;
    @Bind(R.id.pay_account_final)
    TextView tvAccoutnFinal;
    private ArrayList<Privilege> data = new ArrayList<>();
    private double curremtTime;
    private DiscountListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String storeId = getIntent().getStringExtra(Constant.STORE_ID);
        storeId = "592fcc7a0cf2350d29554ae5";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        curremtTime = (double) (hour * 60 * 60 + minute * 60);

        findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), DiscountActivity.class);
                intent.putParcelableArrayListExtra(Constant.DISCOUNT_INFO, data);
                startActivity(intent);
            }
        });

        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getDiscountInfo(storeId, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<Privilege>>>() {
                    @Override
                    public void onNext(BaseResponse<List<Privilege>> pResponse) {
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
                if (s.length() <= 0) return;
                try {
                    setActualResult(Float.parseFloat(s.toString()));
                    if (adapter!=null)adapter.setAmount(s.toString());
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

    public void setData(List<Privilege> list) {
        if (list == null) return;
        for (Privilege privilege : list) {                  //删除不在时间段内的优惠信息
            if ((curremtTime > privilege.activityTime.get(0) && curremtTime < privilege.activityTime.get(1)) ||
                    (curremtTime > privilege.activityTime.get(0) || curremtTime < privilege.activityTime.get(1))) {
                data.add(privilege);
            }
        }
        data.addAll(list);
        Collections.sort(data, new Comparator<Privilege>() {
            @Override
            public int compare(Privilege o1, Privilege o2) {
                return o1.condition.subtract(o2.condition).intValue();
            }
        });
        adapter = new DiscountListAdapter(this, data);
        listView.setAdapter(adapter);
    }

    public void setActualResult(float account) {
        BigDecimal totalFee = new BigDecimal(account);
        BigDecimal previous = totalFee;
        BigDecimal temp = totalFee;
        for (Privilege privilege : data) {
            if ((curremtTime > privilege.activityTime.get(0) && curremtTime < privilege.activityTime.get(1)) ||
                    (curremtTime > privilege.activityTime.get(0) || curremtTime < privilege.activityTime.get(1))) {
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
}
