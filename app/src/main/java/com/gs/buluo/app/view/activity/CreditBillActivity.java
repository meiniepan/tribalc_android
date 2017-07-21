package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.gs.buluo.app.R;

import butterknife.Bind;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditBillActivity extends BaseActivity{
    @Bind(R.id.credit_history_bill)
    ListView listView;
    @Override
    protected void bindView(Bundle savedInstanceState) {


    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit_bill;
    }
}
