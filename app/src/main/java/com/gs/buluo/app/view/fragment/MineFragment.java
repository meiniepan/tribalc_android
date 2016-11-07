package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MinePresenter;
import com.gs.buluo.app.view.activity.LoginActivity;
import com.gs.buluo.app.view.activity.RegisterActivity;
import com.gs.buluo.app.view.activity.SelfActivity;
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
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MinePresenter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_head:
                startActivity(new Intent(getActivity(), SelfActivity.class));
                break;
            case R.id.mine_login:
                startActivity(new Intent(getActivity(),LoginActivity.class));
                break;
            case R.id.mine_register:
                startActivity(new Intent(getActivity(),RegisterActivity.class));
                break;
            case R.id.mine_verify:
                startActivity(new Intent(getActivity(),VerifyActivity.class));
                break;
        }

    }
}
