package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.OrderBean;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.order_detail_back).setOnClickListener(this);


        OrderBean bean= (OrderBean) getIntent().getSerializableExtra(Constant.ORDER);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_detail_back:
                finish();
                break;
        }
    }
}
