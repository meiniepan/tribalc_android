package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/8.
 */
public class AddAddressActivity extends BaseActivity{
    @Bind(R.id.et_address_code)
    EditText mCode;
    @Bind(R.id.et_address_area_detail)
    EditText mDetail;
    @Bind(R.id.et_address_name)
    EditText mName;
    @Bind(R.id.et_address_area)
    EditText mAddress;
    @Bind(R.id.et_address_number)
    EditText mNumber;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.add_address_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_address;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
