package com.gs.buluo.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.GrideBean;
import com.gs.buluo.app.holder.HomeCarouselHolder;
import com.gs.buluo.app.holder.ItemHolder;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.RefrushRecycleView;
import em.sang.com.allrecycleview.adapter.RefrushAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewLisenter;
import em.sang.com.allrecycleview.inter.DefaultRefrushListener;

/**
 * Created by Solang on 2017/7/12.
 */

public class NMainFragment extends BaseFragment {
    private RefrushRecycleView refrushRecycleView;
    private RefrushAdapter<String> adapter;
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
        refrushRecycleView = (RefrushRecycleView) getActivity().findViewById(R.id.rc_home);
        lists=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add("ITEM"+i);
        }

        adapter = new RefrushAdapter<>(getContext(), lists, R.layout.item, new DefaultAdapterViewLisenter() {
            @Override
            public CustomHolder getBodyHolder(Context context, List lists, int itemID) {
                return new ItemHolder(  context,   lists,   itemID);
            }
        });


        //显示下拉刷新
        refrushRecycleView.setHasTop(true);
        refrushRecycleView.setRefrushListener(new DefaultRefrushListener() {
            @Override
            public void onLoading() {
                super.onLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        refrushRecycleView.post(new Runnable() {
                            @Override
                            public void run() {
                                refrushRecycleView.loadSuccess();
                            }
                        });

                    }
                }).start();

            }
        });
        /**
         * 设置下拉刷新位置
         */
        adapter.setRefrushPosition(1);

        //上侧九宫格

        List<GrideBean> list = new ArrayList<>();
        list.add(new GrideBean("", R.mipmap.home_banner1));
        list.add(new GrideBean("", R.mipmap.home_banner2));
        list.add(new GrideBean("", R.mipmap.home_banner3));
        list.add(new GrideBean("", R.mipmap.home_banner4));
        list.add(new GrideBean("", R.mipmap.home_banner5));

        //中间广告条
        carouselHolder = new HomeCarouselHolder(getContext(), list, R.layout.item_home_carousel);
        adapter.addHead(carouselHolder);


        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        refrushRecycleView.setLayoutManager(manager);
        refrushRecycleView.setAdapter(adapter);
    }
}
