package com.gs.buluo.app.view.activity;

import android.os.Bundle;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.OrderFragmentAdapter;
import com.gs.buluo.app.bean.HomeMessageEnum;
import com.gs.buluo.app.bean.RequestBodyBean.XgMessageReadBody;
import com.gs.buluo.app.bean.TabEntity;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.UnScrollViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderActivity extends BaseActivity {
    @Bind(R.id.order_pager)
    UnScrollViewPager pager;
    @Bind(R.id.tl_4)
    CommonTabLayout tabLayout;

    private int[] mIconUnselectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private int[] mIconSelectIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private HashMap<String, Integer> xgMessageMap;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        xgMessageMap = TribeApplication.getInstance().getXgMessageMap();
        String[] titles = new String[]{getString(R.string.all), getString(R.string.wait_pay), getString(R.string.wait_receive), getString(R.string.complete)};
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        OrderFragmentAdapter adapter =
                new OrderFragmentAdapter(getSupportFragmentManager(), Arrays.asList(titles));
        pager.setAdapter(adapter);
        tabLayout.setTabData(mTabEntities);
        if (xgMessageMap != null && xgMessageMap.get(HomeMessageEnum.ORDER_DELIVERY) != null) {
            showMsg(2, xgMessageMap.get(HomeMessageEnum.ORDER_DELIVERY));
        }
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                pager.setCurrentItem(position);
                tabLayout.hideMsg(position);
                if (position == 2)
                    readMessage();
            }

            @Override
            public void onTabReselect(int position) {
//                if (position == 0) {
//                    tabLayout.hideMsg(0);
//                }
            }
        });
        pager.setCurrentItem(getIntent().getIntExtra(Constant.TYPE, 0), false);
    }

    private void readMessage() {
        TribeApplication.getInstance().setXgMessageMap(HomeMessageEnum.ORDER_DELIVERY, 0);
        XgMessageReadBody body = new XgMessageReadBody();
        body.messageBodyType = HomeMessageEnum.ORDER_DELIVERY;
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).readXgMessage(TribeApplication.getInstance().getUserInfo().getId(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void showMsg(int position, int num) {
        tabLayout.showMsg(position, num);
        if (num == 0)
            tabLayout.hideMsg(position);
        else
        if (num > 9)
            tabLayout.setMsgMargin(1, -5, 2);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
