package com.gs.buluo.app.view.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.MainPagerAdapter;
import com.gs.buluo.app.bean.UserInfo;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MainPresenter;
import com.gs.buluo.app.view.fragment.AroundFragment;
import com.gs.buluo.app.view.fragment.BaseFragment;
import com.gs.buluo.app.view.fragment.FoundFragment;
import com.gs.buluo.app.view.fragment.MainFragment;
import com.gs.buluo.app.view.fragment.MineFragment;
import com.gs.buluo.app.view.fragment.UsualFragment;
import com.gs.buluo.app.view.impl.IMainView;

import java.util.ArrayList;

import butterknife.Bind;


public class MainActivity extends BaseActivity implements IMainView {
    @Bind(R.id.main_pager)
    ViewPager mPager;
    @Bind(R.id.tabs_first)
    TextView tFirst;
    @Bind(R.id.tabs_around)
    TextView tAround;
    @Bind(R.id.tabs_found)
    TextView tFound;
    @Bind(R.id.tabs_mine)
    TextView tMine;
    @Bind(R.id.tabs_usual)
    TextView tUsual;


    private ArrayList<BaseFragment> list;
    private ArrayList<TextView> tabs=new ArrayList<>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list = new ArrayList<>();
        list.add(new MainFragment());
        list.add(new FoundFragment());
        list.add(new AroundFragment());
        list.add(new UsualFragment());
        list.add(new MineFragment());

        tFirst.setTextColor(Color.BLUE);
        tFirst.setOnClickListener(new MainOnClickListener(0));
        tFound.setOnClickListener(new MainOnClickListener(1));
        tAround.setOnClickListener(new MainOnClickListener(2));
        tUsual.setOnClickListener(new MainOnClickListener(3));
        tMine.setOnClickListener(new MainOnClickListener(4));
        tabs.add(tFirst);
        tabs.add(tFound);
        tabs.add(tAround);
        tabs.add(tUsual);
        tabs.add(tMine);

        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),list));
        mPager.setCurrentItem(0);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MainPresenter();
    }


    private void changeFragment(int i) {
        mPager.setCurrentItem(i,false);
    }

    @Override
    public void setUser(UserInfo info) {

    }

    @Override
    public void showError() {

    }

    private class MainOnClickListener implements View.OnClickListener {
        private int mIndex;
        public MainOnClickListener(int index) {
            mIndex=index;
        }

        @Override
        public void onClick(View v) {
            changeFragment(mIndex);
            setCurrentTab(mIndex);
        }
    }

    public void setCurrentTab(int currentTab) {
        for (int i =0; i < tabs.size(); i++){
            TextView textView = tabs.get(i);
            if (i==2){
//                setBarColor(R.color.black);
            }
            if (i == currentTab){
                textView.setTextColor(Color.BLUE);
                textView.setBackgroundResource(R.drawable.text_background);
            } else {
                textView.setTextColor(Color.BLACK);
                textView.setBackground(null);
            }
        }
    }

}
