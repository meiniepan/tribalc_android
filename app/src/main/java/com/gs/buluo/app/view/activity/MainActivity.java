package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.MainPagerAdapter;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.LoginPresenter;
import com.gs.buluo.app.view.fragment.BaseFragment;
import com.gs.buluo.app.view.fragment.FoundFragment;
import com.gs.buluo.app.view.fragment.MainFragment;
import com.gs.buluo.app.view.fragment.MineFragment;
import com.gs.buluo.app.view.fragment.UsualFragment;
import com.gs.buluo.app.view.impl.ILoginView;
import com.gs.buluo.app.view.widget.panel.AroundPanel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainActivity extends BaseActivity implements ILoginView, ViewPager.OnPageChangeListener, View.OnClickListener {
    @Bind(R.id.main_pager)
    ViewPager mPager;
    @Bind(R.id.main_around_text)
    TextView mAround;
    @Bind(R.id.main_found_text)
    TextView mFound;
    @Bind(R.id.main_mine_text)
    TextView mMine;
    @Bind(R.id.main_usual_text)
    TextView mUsual;
    @Bind(R.id.main_home_text)
    TextView mHome;
    @Bind(R.id.main_around)
    ImageView mAroundImage;
    @Bind(R.id.main_found)
    ImageView mFoundImage;
    @Bind(R.id.main_mine)
    ImageView mMineImage;
    @Bind(R.id.main_usual)
    ImageView mUsualImage;
    @Bind(R.id.main_home)
    ImageView mHomeImage;

    PopupWindow popupWindow;

    private ArrayList<BaseFragment> list;
    private ArrayList<TextView> tabs=new ArrayList<>(4);
    private List<Integer> imageRids = new ArrayList<>(4);
    private List<Integer> imageSelectedRids = new ArrayList<>(4);
    private List<ImageView> tabIcons = new ArrayList<>(4);
    private MineFragment mineFragment;
    private long mkeyTime=0;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (new UserInfoDao().findFirst()==null){
            mineFragment.setLoginState(false);
        }else {
            mineFragment.setLoginState(true);
        }
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        list = new ArrayList<>();
        list.add(new MainFragment());
        list.add(new FoundFragment());
//        list.add(new AroundFragment());
        list.add(new UsualFragment());
        mineFragment = new MineFragment();
        list.add(mineFragment);
        findViewById(R.id.main_home_layout).setOnClickListener(new MainOnClickListener(0));
        findViewById(R.id.main_found_layout).setOnClickListener(new MainOnClickListener(1));
        findViewById(R.id.main_usual_layout).setOnClickListener(new MainOnClickListener(2));
        findViewById(R.id.main_mine_layout).setOnClickListener(new MainOnClickListener(3));
        findViewById(R.id.main_arround_layout).setOnClickListener(this);
        initBar();
        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),list));
        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(3);
        setCurrentTab(0);
        initUser();
    }

    private void initUser() {
        UserInfoEntity first = new UserInfoDao().findFirst();
        TribeApplication.getInstance().setUserInfo(first);
    }

    private void initBar() {
        tabs.add(mHome);
        tabs.add(mFound);
//        tabs.add(mAround);
        tabs.add(mUsual);
        tabs.add(mMine);
        imageRids.add(R.mipmap.tabbar_home_normal);
        imageRids.add(R.mipmap.tabbar_discover_normal);
//        imageRids.add(R.mipmap.tabbar_vicinity);
        imageRids.add(R.mipmap.tabbar_common_normal);
        imageRids.add(R.mipmap.tabbar_profile_normal);
        imageSelectedRids.add(R.mipmap.tabbar_home_selected);
        imageSelectedRids.add(R.mipmap.tabbar_discover_selected);
//        imageSelectedRids.add(R.mipmap.tabbar_vicinity);
        imageSelectedRids.add(R.mipmap.tabbar_common_selected);
        imageSelectedRids.add(R.mipmap.tabbar_profile_selected);
        tabIcons.add(mHomeImage);
        tabIcons.add(mFoundImage);
//        tabIcons.add(mAroundImage);
        tabIcons.add(mUsualImage);
        tabIcons.add(mMineImage);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new LoginPresenter();
    }


    private void changeFragment(int i) {
        mPager.setCurrentItem(i,false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        changeFragment(position);
        setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void showError(int res) {
    }
    @Override
    public void loginSuccess() {
    }
    @Override
    public void dealWithIdentify(int res) {
    }

    @Override
    public void onClick(View v) {
        final AroundPanel panel=new AroundPanel(this);
        panel.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                panel.showMenu();
            }
        },500);
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
            ImageView img=tabIcons.get(i);
            if (i==2){
//                setBarColor(R.color.black);
            }
            if (i == currentTab){
                textView.setTextColor(getResources().getColor(R.color.black));
                img.setBackgroundResource(imageSelectedRids.get(i));
            } else {
                textView.setTextColor(getResources().getColor(R.color.main_tab));
                img.setBackgroundResource(imageRids.get(i));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                mkeyTime = System.currentTimeMillis();
                Toast.makeText(this, R.string.exit_app, Toast.LENGTH_LONG).show();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
