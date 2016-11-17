package com.gs.buluo.app.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.GoodResponseList;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MainPresenter;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.impl.IMainView;
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
        startActivity(new Intent(getActivity(),GoodsListActivity.class));
    }
}
