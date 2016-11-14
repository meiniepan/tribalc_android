package com.gs.buluo.app.view.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.AppManager;
import com.gs.buluo.app.utils.SystemBarTintManager;
import com.gs.buluo.app.view.impl.IBaseView;
import com.gs.buluo.app.widget.LoadingDialog;

import butterknife.ButterKnife;


/**
 * Created by admin on 2016/11/1.
 */
public abstract class BaseActivity<T extends BasePresenter<IBaseView>> extends AppCompatActivity {
    View mRoot;
    protected BasePresenter mPresenter;
    private Toolbar mToolbar;

    private int color = R.color.titlebar_background;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        mPresenter=getPresenter();
        if (mPresenter != null && this instanceof IBaseView ) {
            mPresenter.attach((IBaseView) this);
        }

        mRoot = createView();
        setContentView(mRoot);
//        mToolbar = (Toolbar) findViewById(getToolBarId());
//        setSupportActionBar(mToolbar);
        bindView(savedInstanceState);
    }

    private View createView() {
        View view = LayoutInflater.from(this).inflate(getContentLayout(),null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    public View getRootView(){
        return mRoot;
    }

    /**
     * 设置状态栏颜色
     * @param colorInt
     */
    public void setBarColor(int colorInt){
        color = colorInt;
        initSystemBar(this);
    }


    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showDialog() {
        LoadingDialog.getInstance().show(mRoot.getContext(),getString(R.string.connecting),true);
    }


    protected abstract void bindView(Bundle savedInstanceState);
    protected abstract int getContentLayout();
    protected  <T extends BasePresenter>T getPresenter(){
        return (T) mPresenter;
    }
}


