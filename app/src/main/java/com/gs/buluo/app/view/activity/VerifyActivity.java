package com.gs.buluo.app.view.activity;

import android.os.Bundle;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

/**
 * Created by hjn on 2016/11/7.
 */
public class VerifyActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
