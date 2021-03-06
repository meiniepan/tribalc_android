package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.DialogDismissEvent;
import com.gs.buluo.app.eventbus.DialogShowEvent;
import com.gs.buluo.app.network.TribeUploader;
import com.gs.buluo.app.view.fragment.BaseFragment;
import com.gs.buluo.app.view.fragment.CommunityFragment;
import com.gs.buluo.app.view.fragment.HighBuyFragment;
import com.gs.buluo.app.view.fragment.MainFragment;
import com.gs.buluo.app.view.fragment.MineFragment;
import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.common.utils.CommonUtils;
import com.tencent.android.tpush.XGIOperateCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tencent.android.tpush.XGPush4Msdk.registerPush;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.n_main_pager)
    ViewPager mPager;
    @BindView(R.id.n_main_found_text)
    TextView mFound;
    @BindView(R.id.n_main_mine_text)
    TextView mMine;
    @BindView(R.id.n_main_usual_text)
    TextView mUsual;
    @BindView(R.id.n_main_home_text)
    TextView mHome;
    @BindView(R.id.n_main_found)
    ImageView mFoundImage;
    @BindView(R.id.n_main_mine)
    ImageView mMineImage;
    @BindView(R.id.n_main_usual)
    ImageView mUsualImage;
    @BindView(R.id.n_main_home)
    ImageView mHomeImage;


    private ArrayList<BaseFragment> list;
    private ArrayList<TextView> tabs = new ArrayList<>(4);
    private List<Integer> imageRids = new ArrayList<>(4);
    private List<Integer> imageSelectedRids = new ArrayList<>(4);
    private List<ImageView> tabIcons = new ArrayList<>(4);
    private MainFragment mainFragment;
    private HighBuyFragment highBuyFragment;
    private CommunityFragment communityFragment;
    private MineFragment mineFragment;
    private long mkeyTime = 0;
    private static final String TAG = "MainActivity";

    @Override
    protected int getContentLayout() {
        return R.layout.n_activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        //如果需要绑定账号，请使用
        registerPush(getApplicationContext(), TribeApplication.getInstance().getUserInfo().getId(), null);
        mineFragment.setLoginState(TribeApplication.getInstance().getUserInfo() != null);
        mainFragment.getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(TokenEvent event) {
        new UserInfoDao().clear();
        TribeApplication.getInstance().setUserInfo(null);
        registerPush(getApplicationContext(), "", null);
        Intent intent = new Intent(getCtx(), LoginActivity.class);
        intent.putExtra(Constant.RE_LOGIN, true);
        startActivity(intent);
        if (mineFragment != null) {
            mineFragment.setLoginState(false);
        }
        finish();
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
//        QMUIStatusBarHelper.setStatusBarLightMode(this);
        TribeApplication.getInstance().getXgMessage();
        initPush();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        list = new ArrayList<>();
        mainFragment = new MainFragment();
        list.add(mainFragment);
        highBuyFragment = new HighBuyFragment();
        list.add(highBuyFragment);
        communityFragment = new CommunityFragment();
        list.add(communityFragment);
        mineFragment = new MineFragment();
        list.add(mineFragment);
        findViewById(R.id.n_main_home_layout).setOnClickListener(new MainOnClickListener(0));
        findViewById(R.id.n_main_found_layout).setOnClickListener(new MainOnClickListener(1));
        findViewById(R.id.n_main_usual_layout).setOnClickListener(new MainOnClickListener(2));
        findViewById(R.id.n_main_mine_layout).setOnClickListener(new MainOnClickListener(3));
        initBar();
        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), list));
        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(3);
        setCurrentTab(0);
        initUser();
    }

    private void initPush() {
        registerPush(getApplicationContext(), TribeApplication.getInstance().getUserInfo().getId(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.e("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }


    private void initUser() {
        UserInfoEntity first = new UserInfoDao().findFirst();
        TribeApplication.getInstance().setUserInfo(first);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuglyUpgradeDialogShow(DialogShowEvent event) {
        CommonUtils.backgroundAlpha(this, 0.7f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuglyUpgradeDialogDismiss(DialogDismissEvent event) {
        CommonUtils.backgroundAlpha(this, 1f);
    }

    private void initBar() {
        tabs.add(mHome);
        tabs.add(mFound);
        tabs.add(mUsual);
        tabs.add(mMine);
        imageRids.add(R.mipmap.tabbar_hom_normal);
        imageRids.add(R.mipmap.tabbar_buy_normal);
        imageRids.add(R.mipmap.tabbar_com_normal);
        imageRids.add(R.mipmap.tabbar_mine_normal);
        imageSelectedRids.add(R.mipmap.tabbar_hom_selected);
        imageSelectedRids.add(R.mipmap.tabbar_buy_selected);
        imageSelectedRids.add(R.mipmap.tabbar_com_selected);
        imageSelectedRids.add(R.mipmap.tabbar_mine_selected);
        tabIcons.add(mHomeImage);
        tabIcons.add(mFoundImage);
        tabIcons.add(mUsualImage);
        tabIcons.add(mMineImage);
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

