package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.GuidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/27.
 */
public class GuideActivity extends BaseActivity{
    @Bind(R.id.guide_pager)
    ViewPager viewPager;

    GuidePagerAdapter vAdapter;
    private List<Integer> list;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        list = new ArrayList<>();
        list.add(R.mipmap.guide_1);
        list.add(R.mipmap.guide2);
        list.add(R.mipmap.guide3);
        vAdapter=new GuidePagerAdapter(this,list);
        viewPager.setAdapter(vAdapter);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_guide;
    }
}
