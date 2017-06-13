package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.MainPagerAdapter;
import com.gs.buluo.app.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.network.TribeUploader;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.view.fragment.BaseFragment;
import com.gs.buluo.app.view.fragment.FoundFragment;
import com.gs.buluo.app.view.fragment.MainFragment;
import com.gs.buluo.app.view.fragment.MineFragment;
import com.gs.buluo.app.view.fragment.UsualFragment;
import com.gs.buluo.app.view.widget.panel.AroundPanel;
import com.gs.buluo.common.network.TokenEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
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

    private ArrayList<BaseFragment> list;
    private ArrayList<TextView> tabs = new ArrayList<>(4);
    private List<Integer> imageRids = new ArrayList<>(4);
    private List<Integer> imageSelectedRids = new ArrayList<>(4);
    private List<ImageView> tabIcons = new ArrayList<>(4);
    private MineFragment mineFragment;
    private long mkeyTime = 0;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mineFragment.setLoginState(new UserInfoDao().findFirst() != null);
        setIntent(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(TokenEvent event) {
        SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).clearValue(Constant.WALLET_PWD);
        new AddressInfoDao().clear();
        new UserInfoDao().clear();
        TribeApplication.getInstance().setUserInfo(null);

        Intent intent = new Intent(getCtx(), LoginActivity.class);
        intent.putExtra(Constant.RE_LOGIN, true);
        startActivity(intent);
        if (mineFragment != null) {
            mineFragment.setLoginState(false);
        }
    }

    private void checkCrash() {
        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/tribe/", "crash");
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] files = dir.listFiles();
        for (File f : files) {
            TribeUploader.getInstance().uploadFile("crash", "text/plain", f, new TribeUploader.UploadCallback() {
                @Override
                public void uploadSuccess(UploadResponseBody url) {
                }

                @Override
                public void uploadFail() {
                }
            });
        }
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        list = new ArrayList<>();
        list.add(new MainFragment());
        list.add(new FoundFragment());
        list.add(new UsualFragment());
        mineFragment = new MineFragment();
        list.add(mineFragment);
        findViewById(R.id.main_home_layout).setOnClickListener(new MainOnClickListener(0));
        findViewById(R.id.main_found_layout).setOnClickListener(new MainOnClickListener(1));
        findViewById(R.id.main_usual_layout).setOnClickListener(new MainOnClickListener(2));
        findViewById(R.id.main_mine_layout).setOnClickListener(new MainOnClickListener(3));
        findViewById(R.id.main_arround_layout).setOnClickListener(this);
        initBar();
        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), list));
        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(3);
        setCurrentTab(0);
        initUser();
        if (!Constant.Base.BASE_URL.contains("dev")) checkCrash();
    }

    private void initUser() {
        UserInfoEntity first = new UserInfoDao().findFirst();
        TribeApplication.getInstance().setUserInfo(first);
    }

    private void initBar() {
        tabs.add(mHome);
        tabs.add(mFound);
        tabs.add(mUsual);
        tabs.add(mMine);
        imageRids.add(R.mipmap.tabbar_home_normal);
        imageRids.add(R.mipmap.tabbar_discover_normal);
        imageRids.add(R.mipmap.tabbar_common_normal);
        imageRids.add(R.mipmap.tabbar_profile_normal);
        imageSelectedRids.add(R.mipmap.tabbar_home_selected);
        imageSelectedRids.add(R.mipmap.tabbar_discover_selected);
        imageSelectedRids.add(R.mipmap.tabbar_common_selected);
        imageSelectedRids.add(R.mipmap.tabbar_profile_selected);
        tabIcons.add(mHomeImage);
        tabIcons.add(mFoundImage);
        tabIcons.add(mUsualImage);
        tabIcons.add(mMineImage);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    private void changeFragment(int i) {
        mPager.setCurrentItem(i, false);
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
    public void onClick(View v) {
        final AroundPanel panel = new AroundPanel(this);
        panel.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                panel.showMenu();
            }
        }, 500);
    }

    private class MainOnClickListener implements View.OnClickListener {
        private int mIndex;

        public MainOnClickListener(int index) {
            mIndex = index;
        }

        @Override
        public void onClick(View v) {
            changeFragment(mIndex);
            setCurrentTab(mIndex);
        }
    }

    public void setCurrentTab(int currentTab) {
        for (int i = 0; i < tabs.size(); i++) {
            TextView textView = tabs.get(i);
            ImageView img = tabIcons.get(i);
            if (i == 2) {
//                setBarColor(R.color.black);
            }
            if (i == currentTab) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
