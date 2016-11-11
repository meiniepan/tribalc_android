package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MinePresenter;
import com.gs.buluo.app.view.activity.LoginActivity;
import com.gs.buluo.app.view.activity.SelfActivity;
import com.gs.buluo.app.view.activity.SettingActivity;
import com.gs.buluo.app.view.activity.VerifyActivity;
import com.gs.buluo.app.widget.ChoosePhotoPanel;

import butterknife.Bind;
import de.greenrobot.event.EventBus;


/**
 * Created by admin on 2016/11/1.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.mine_head)
    SimpleDraweeView mHead;
    @Bind(R.id.self_ll_login)
    LinearLayout llLogin;
    @Bind(R.id.self_ll_un_login)
    LinearLayout llUnLogin;
    @Bind(R.id.self_nickname)
    TextView mNick;
    @Bind(R.id.rl_head_bg)
    SimpleDraweeView mCover;
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
        if (TribeApplication.getInstance().getUserInfo()!=null){
            setLoginState(true);
        }
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
                        mCover.setImageURI("file://"+path);
                    }
                });
                window.show();
                break;
        }
    }

    public void setLoginState(boolean loginState) {
        if (loginState){
            llLogin.setVisibility(View.VISIBLE);
            llUnLogin.setVisibility(View.GONE);
            String nickname = TribeApplication.getInstance().getUserInfo().getNickname();
            if (!TextUtils.isEmpty(nickname)){
                mNick.setText(nickname);
            }
        }else {
            llLogin.setVisibility(View.GONE);
            llUnLogin.setVisibility(View.VISIBLE);
        }
    }
}
