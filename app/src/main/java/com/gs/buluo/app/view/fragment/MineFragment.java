package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MinePresenter;
import com.gs.buluo.app.view.activity.LoginActivity;
import com.gs.buluo.app.view.activity.SelfActivity;
import com.gs.buluo.app.view.activity.SettingActivity;
import com.gs.buluo.app.view.activity.VerifyActivity;
import com.gs.buluo.app.widget.ChoosePhotoPanel;

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
        getActivity().findViewById(R.id.mine_head).setOnClickListener(this);
        getActivity().findViewById(R.id.mine_cover).setOnClickListener(this);
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
                startActivity(intent);
                break;
            case R.id.mine_login:
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_register:
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_verify:
                intent.setClass(getActivity(),VerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_setting:
                intent.setClass(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_cover:
                ChoosePhotoPanel window=new ChoosePhotoPanel(getContext(), new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String path) {
                        SimpleDraweeView simpleDraweeView= (SimpleDraweeView) getActivity().findViewById(R.id.rl_head_bg);
                        simpleDraweeView.setImageURI("file://"+path);
                    }
                });
                window.show();
                break;
        }
    }
}
