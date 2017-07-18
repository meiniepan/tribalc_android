package com.gs.buluo.app.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.GrideBean;
import com.gs.buluo.app.holder.HomeCarouselHolder;
import com.gs.buluo.app.holder.ItemHolder;
import com.gs.buluo.app.view.activity.MainSearchActivity;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.RefreshRecycleView;
import em.sang.com.allrecycleview.adapter.RefreshAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewLisenter;
import em.sang.com.allrecycleview.inter.DefaultRefrushListener;

/**
 * Created by Solang on 2017/7/12.
 */

public class NMainFragment extends BaseFragment implements View.OnClickListener{
    private RefreshRecycleView mRefreshRecycleView;
    private RefreshAdapter<String> adapter;
    private List<String> lists;
    private HomeCarouselHolder carouselHolder;
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_n;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initView();
    }
    private void initView() {
        getActivity().findViewById(R.id.tv_search).setOnClickListener(this);
        mRefreshRecycleView = (RefreshRecycleView) getActivity().findViewById(R.id.rc_home);
        lists=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add("ITEM"+i);
        }

        adapter = new RefreshAdapter<>(getContext(), lists, R.layout.item, new DefaultAdapterViewLisenter() {
            @Override
            public CustomHolder getBodyHolder(Context context, List lists, int itemID) {
                return new ItemHolder(  getActivity(),context,   lists,   itemID);
            }
        });


        //显示下拉刷新
        mRefreshRecycleView.setHasTop(true);
        mRefreshRecycleView.setRefrushListener(new DefaultRefrushListener() {
            @Override
            public void onLoading() {
                super.onLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        mRefreshRecycleView.post(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshRecycleView.loadSuccess();
                            }
                        });

                    }
                }).start();

            }
        });
        /**
         * 设置下拉刷新位置
         */
        adapter.setRefreshPosition(1);

        //上侧九宫格

        List<GrideBean> list = new ArrayList<>();
        list.add(new GrideBean("", R.mipmap.home_banner));


        //中间广告条
        carouselHolder = new HomeCarouselHolder(getContext(), list, R.layout.item_home_carousel);
        adapter.addHead(carouselHolder);


        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRefreshRecycleView.setLayoutManager(manager);
        mRefreshRecycleView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
