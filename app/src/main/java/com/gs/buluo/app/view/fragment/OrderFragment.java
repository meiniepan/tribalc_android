package com.gs.buluo.app.view.fragment;

import android.os.Bundle;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderFragment extends BaseFragment{

    private int type;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    public void setType(int type) {
        this.type = type;
    }
}
