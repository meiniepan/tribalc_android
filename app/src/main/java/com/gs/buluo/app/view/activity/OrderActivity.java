package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.OrderFragmentAdapter;
import com.gs.buluo.app.view.widget.UnScrollViewPager;

import java.util.Arrays;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderActivity extends BaseActivity{
    @Bind(R.id.order_pager)
    UnScrollViewPager pager;
    @Bind(R.id.order_tab)
    TabLayout tabLayout;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.order_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String[] titles = new String[]{getString(R.string.all),getString(R.string.wait_pay),getString(R.string.wait_receive),getString(R.string.complete)};
        OrderFragmentAdapter adapter =
                new OrderFragmentAdapter(getSupportFragmentManager(), Arrays.asList(titles));
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        pager.setCurrentItem(getIntent().getIntExtra(Constant.TYPE,0),false);
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
