package com.gs.buluo.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gs.buluo.app.view.fragment.OrderFragment;

import java.util.List;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderFragmentAdapter extends FragmentStatePagerAdapter{
    List<String>  list;
    public OrderFragmentAdapter(FragmentManager supportFragmentManager, List<String> list) {
        super(supportFragmentManager);
        this.list=list;
    }


    @Override
    public Fragment getItem(int position) {
        OrderFragment fragment=new OrderFragment();
        fragment.setType(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
