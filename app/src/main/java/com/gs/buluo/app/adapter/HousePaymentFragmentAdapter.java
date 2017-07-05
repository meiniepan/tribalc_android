package com.gs.buluo.app.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gs.buluo.app.view.fragment.LifePaymentFragment;
import com.gs.buluo.app.view.fragment.RentPaymentAllPayedFragment;
import com.gs.buluo.app.view.fragment.RentPaymentFragment;

import java.util.List;

/**
 * Created by Solang on 2017/6/21.
 */

public class HousePaymentFragmentAdapter extends FragmentStatePagerAdapter {
    private final String protocolId;
    private final String code;
    private final String name;
    private Fragment fragment;
    List<String> list;

    public HousePaymentFragmentAdapter(String protocolId,String code,String name, FragmentManager supportFragmentManager, List<String> list) {
        super(supportFragmentManager);
        this.list = list;
        this.protocolId = protocolId;
        this.code = code;
        this.name = name;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            getFragmentCommon();
//             getFragmentAllPayed();
        } else {
            fragment = new LifePaymentFragment();
        }
        return fragment;
    }

    private void getFragmentAllPayed() {
        fragment = new RentPaymentAllPayedFragment();
    }

    @NonNull
    private void getFragmentCommon() {
        fragment = new RentPaymentFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("protocolId", protocolId);
        bundle1.putString("code", code);
        bundle1.putString("name", name);
        fragment.setArguments(bundle1);
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
