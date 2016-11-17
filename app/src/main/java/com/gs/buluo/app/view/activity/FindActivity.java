package com.gs.buluo.app.view.activity;

import android.os.Bundle;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

/**
 * Created by hjn on 2016/11/3.
 */
public class FindActivity  extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_find;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
