package com.gs.buluo.app.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MainPresenter;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.view.activity.PropertyActivity;
import com.gs.buluo.app.view.activity.ServeActivity;
import com.gs.buluo.app.view.impl.IMainView;
import com.gs.buluo.app.view.activity.GoodsListActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by admin on 2016/11/1.
 */
public class MainFragment extends BaseFragment implements IMainView, View.OnClickListener {
    @Bind(R.id.fragment_main_head)
    Banner mBanner;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        List list=new ArrayList();
        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImages(list);
        mBanner.setDelayTime(2000);
        mBanner.start();

        getActivity().findViewById(R.id.shopping).setOnClickListener(this);
        getActivity().findViewById(R.id.shopping_area).setOnClickListener(this);
        getActivity().findViewById(R.id.food).setOnClickListener(this);
        getActivity().findViewById(R.id.food_area).setOnClickListener(this);
        getActivity().findViewById(R.id.fun).setOnClickListener(this);
        getActivity().findViewById(R.id.fun_area).setOnClickListener(this);
        getActivity().findViewById(R.id.main_property).setOnClickListener(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void showError(int res) {

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.shopping:
                intent.setClass(getActivity(), GoodsListActivity.class);
                startActivity(intent);
                break;
            case R.id.shopping_area:
                intent.setClass(getActivity(), GoodsListActivity.class);
                startActivity(intent);
                break;
            case R.id.food:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.REPAST);
                startActivity(intent);
                break;
            case R.id.food_area:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.REPAST);
                startActivity(intent);
                break;
            case R.id.fun:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.ENTERTAINMENT);
                startActivity(intent);
                break;
            case R.id.fun_area:
                intent.setClass(getActivity(), ServeActivity.class);
                intent.putExtra(Constant.TYPE,Constant.ENTERTAINMENT);
                startActivity(intent);
            case R.id.main_property:
                intent.setClass(getActivity(), PropertyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
