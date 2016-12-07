package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.NewOrderAdapter;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.widget.PayPanel;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/5.
 */
public class NewOrderActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.new_order_detail_goods_list)
    ListView listView;
    @Bind(R.id.new_order_price_total)
    TextView tvTotal;
    private float count;

    @Bind(R.id.new_order_pay_balance)
    RadioButton rbBalance;
    @Bind(R.id.new_order_pay_wechat)
    RadioButton rbWeChat;
    @Bind(R.id.new_order_pay_ali)
    RadioButton rbAli;

    private String payMethod;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        payMethod =getString(R.string.pay_balance);
        findViewById(R.id.new_order_back).setOnClickListener(this);
        findViewById(R.id.new_order_finish).setOnClickListener(this);
        count = getIntent().getFloatExtra("count",0);
        tvTotal.setText(count +"");
        List<ShoppingCart> carts= (List<ShoppingCart>) getIntent().getSerializableExtra("cart");
        NewOrderAdapter adapter=new NewOrderAdapter(this,carts);
        listView.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(listView);
        rbBalance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbBalance.setChecked(true);
                    rbAli.setChecked(false);
                    rbWeChat.setChecked(false);
                    payMethod=getString(R.string.pay_balance);
                }
            }
        });
        rbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbBalance.setChecked(false);
                    rbAli.setChecked(true);
                    rbWeChat.setChecked(false);
                    payMethod=getString(R.string.pay_ali);
                }
            }
        });
        rbWeChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rbBalance.setChecked(false);
                    rbAli.setChecked(false);
                    rbWeChat.setChecked(true);
                    payMethod=getString(R.string.pay_wechat);
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_order;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_order_back:
                finish();
                break;
            case R.id.new_order_finish:
                showPayBoard();
                break;
        }
    }

    private void showPayBoard() {
        PayPanel payBoard=new PayPanel(this);
        payBoard.setData(payMethod,count+"");
        payBoard.show();
    }
}
