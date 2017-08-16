package com.gs.buluo.app.view.activity;

import android.content.Context;
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
import com.gs.buluo.app.network.TribeUploader;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.fragment.BaseFragment;
import com.gs.buluo.app.view.fragment.CommunityFragment;
import com.gs.buluo.app.view.fragment.HighBuyFragment;
import com.gs.buluo.app.view.fragment.MainFragment;
import com.gs.buluo.app.view.fragment.MineFragment;
import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.TribeCrashCollector;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.n_main_pager)
    ViewPager mPager;
    @Bind(R.id.n_main_found_text)
    TextView mFound;
    @Bind(R.id.n_main_mine_text)
    TextView mMine;
    @Bind(R.id.n_main_usual_text)
    TextView mUsual;
    @Bind(R.id.n_main_home_text)
    TextView mHome;
    @Bind(R.id.n_main_found)
    ImageView mFoundImage;
    @Bind(R.id.n_main_mine)
    ImageView mMineImage;
    @Bind(R.id.n_main_usual)
    ImageView mUsualImage;
    @Bind(R.id.n_main_home)
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
    private Context mCtx;
    private static final String TAG = "MainActivity";

    @Override
    protected int getContentLayout() {
        return R.layout.n_activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mineFragment.setLoginState(TribeApplication.getInstance().getUserInfo() != null);
        mainFragment.getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(TokenEvent event) {
        new UserInfoDao().clear();
        TribeApplication.getInstance().setUserInfo(null);

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
        mCtx = getCtx();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        initBuglyUpgrade();
        list = new ArrayList<>();
        mainFragment = new MainFragment();
        list.add(mainFragment);
        highBuyFragment = new HighBuyFragment();
        list.add(highBuyFragment);
        communityFragment = new CommunityFragment();
//        list.add(communityFragment);
        mineFragment = new MineFragment();
        list.add(mineFragment);
        findViewById(R.id.n_main_home_layout).setOnClickListener(new MainOnClickListener(0));
        findViewById(R.id.n_main_found_layout).setOnClickListener(new MainOnClickListener(1));
        findViewById(R.id.n_main_usual_layout).setOnClickListener(new MainOnClickListener(3));
        findViewById(R.id.n_main_mine_layout).setOnClickListener(new MainOnClickListener(2));
        initBar();
        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), list));
        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(3);
        setCurrentTab(0);
        initUser();
        if (!Constant.Base.BASE_URL.contains("dev")) TribeCrashCollector.getIns(this);
    }

    private void initBuglyUpgrade() {
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onCreate");
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:
                // 通过id方式获取控件，并更改imageview图片
//                ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
//                imageView.setImageResource(R.mipmap.ic_launcher);
//
//                // 通过tag方式获取控件，并更改布局内容
//                TextView textView = (TextView) view.findViewWithTag("textview");
//                textView.setText("my custom text");
//
//                // 更多的操作：比如设置控件的点击事件
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                });
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
                CommonUtils.backgroundAlpha(MainActivity.this,0.7f);
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
                CommonUtils.backgroundAlpha(MainActivity.this,1);
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestory");
            }
        };
        Bugly.init(getApplicationContext(), "29add4efd5", false);
    }

    private void initUser() {
        UserInfoEntity first = new UserInfoDao().findFirst();
        TribeApplication.getInstance().setUserInfo(first);
    }

    private void initBar() {
        tabs.add(mHome);
        tabs.add(mFound);
//        tabs.add(mUsual);
        tabs.add(mMine);
        imageRids.add(R.mipmap.tabbar_hom_normal);
        imageRids.add(R.mipmap.tabbar_buy_normal);
//        imageRids.add(R.mipmap.tabbar_com_normal);
        imageRids.add(R.mipmap.tabbar_mine_normal);
        imageSelectedRids.add(R.mipmap.tabbar_hom_selected);
        imageSelectedRids.add(R.mipmap.tabbar_buy_selected);
//        imageSelectedRids.add(R.mipmap.tabbar_com_selected);
        imageSelectedRids.add(R.mipmap.tabbar_mine_selected);
        tabIcons.add(mHomeImage);
        tabIcons.add(mFoundImage);
//        tabIcons.add(mUsualImage);
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
            if (mIndex == 3) {
                ToastUtils.ToastMessage(mCtx, "暂无社区");
                return;
            } else
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

