package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.app.R;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/5.
 */
public class NewOrderActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.new_order_more)
    EditText etMore;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.new_order_back).setOnClickListener(this);
        etMore.clearFocus();
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
        }
    }
}
