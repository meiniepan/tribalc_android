package com.gs.buluo.app.view.fragment;

import android.os.Bundle;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

/**
 * Created by admin on 2016/11/1.
 */
public class UsualFragment extends BaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_usual;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
