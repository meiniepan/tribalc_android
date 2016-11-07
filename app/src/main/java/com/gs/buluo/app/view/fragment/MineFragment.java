package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MinePresenter;
import com.gs.buluo.app.view.activity.CompanyRegisterActivity;
import com.gs.buluo.app.view.activity.LoginActivity;
import com.gs.buluo.app.view.activity.RegisterActivity;
import com.gs.buluo.app.view.activity.SelfActivity;
import com.gs.buluo.app.view.activity.SettingActivity;
import com.gs.buluo.app.view.activity.VerifyActivity;

import butterknife.Bind;


/**
 * Created by admin on 2016/11/1.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.mine_head)
    SimpleDraweeView mHead;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mHead.setOnClickListener(this);
        getActivity().findViewById(R.id.mine_login).setOnClickListener(this);
        getActivity().findViewById(R.id.mine_register).setOnClickListener(this);
        getActivity().findViewById(R.id.mine_login).setOnClickListener(this);
        getActivity().findViewById(R.id.mine_verify).setOnClickListener(this);
        getActivity().findViewById(R.id.mine_setting).setOnClickListener(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MinePresenter();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_head:
                intent.setClass(getActivity(),SelfActivity.class);
                break;
            case R.id.mine_login:
                intent.setClass(getActivity(),LoginActivity.class);
                break;
            case R.id.mine_register:
                intent.setClass(getActivity(),RegisterActivity.class);
                break;
            case R.id.mine_verify:
                intent.setClass(getActivity(),VerifyActivity.class);
                break;
            case R.id.mine_setting:
                intent.setClass(getActivity(),SettingActivity.class);
                break;
        }
        startActivity(intent);

    }
}
