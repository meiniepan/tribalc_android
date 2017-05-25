package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.app.bean.SignResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.SelfEvent;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.network.TribeUploader;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MinePresenter;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.CaptureActivity;
import com.gs.buluo.app.view.activity.CompanyActivity;
import com.gs.buluo.app.view.activity.CompanyDetailActivity;
import com.gs.buluo.app.view.activity.LoginActivity;
import com.gs.buluo.app.view.activity.OrderActivity;
import com.gs.buluo.app.view.activity.PropertyListActivity;
import com.gs.buluo.app.view.activity.ReserveActivity;
import com.gs.buluo.app.view.activity.SelfActivity;
import com.gs.buluo.app.view.activity.SettingActivity;
import com.gs.buluo.app.view.activity.SignActivity;
import com.gs.buluo.app.view.activity.VerifyActivity;
import com.gs.buluo.app.view.activity.WalletActivity;
import com.gs.buluo.app.view.widget.panel.ChoosePhotoPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.pulltozoom.PullToZoomScrollViewEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by admin on 2016/11/1.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    SimpleDraweeView mHead;
    LinearLayout llLogin;
    LinearLayout llUnLogin;
    TextView mNick;

    SimpleDraweeView mCover;
    PullToZoomScrollViewEx scrollView;
    private String lastTime;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initZoomView();
        setLoginState(TribeApplication.getInstance().getUserInfo() != null);
        getActivity().findViewById(R.id.mine_wallet).setOnClickListener(this);
        lastTime = SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).getStringValue(Constant.SIGN_IN);
        EventBus.getDefault().register(this);
    }

    private void initZoomView() {
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.self_zoom_layout, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.self_content_layout, null, false);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.self_head_layout, null, false);
        scrollView = (PullToZoomScrollViewEx) getActivity().findViewById(R.id.self_scroll_view);
        mHead = (SimpleDraweeView) headView.findViewById(R.id.mine_head);
        mHead.setOnClickListener(this);
        headView.findViewById(R.id.mine_login).setOnClickListener(this);
        headView.findViewById(R.id.mine_register).setOnClickListener(this);
        headView.findViewById(R.id.mine_update).setOnClickListener(this);
        headView.findViewById(R.id.mine_sign_icon).setOnClickListener(this);

        initContentView(contentView);
        zoomView.findViewById(R.id.mine_setting).setOnClickListener(this);
        zoomView.findViewById(R.id.mine_cover).setOnClickListener(this);
        zoomView.findViewById(R.id.self_scan).setOnClickListener(this);

        contentView.findViewById(R.id.mine_tenement).setOnClickListener(this);
        contentView.findViewById(R.id.mine_company).setOnClickListener(this);
        contentView.findViewById(R.id.mine_sign_in).setOnClickListener(this);

        llLogin = (LinearLayout) headView.findViewById(R.id.self_ll_login);
        llUnLogin = (LinearLayout) headView.findViewById(R.id.self_ll_un_login);
        mNick = (TextView) headView.findViewById(R.id.self_nickname);
        tvSign = (TextView) headView.findViewById(R.id.mine_sign_icon);

        mCover = (SimpleDraweeView) zoomView.findViewById(R.id.rl_head_bg);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (12.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    private void initContentView(View contentView) {
        contentView.findViewById(R.id.mine_verify).setOnClickListener(this);
        contentView.findViewById(R.id.mine_all).setOnClickListener(this);
        contentView.findViewById(R.id.mine_pay).setOnClickListener(this);
        contentView.findViewById(R.id.mine_receive).setOnClickListener(this);
        contentView.findViewById(R.id.mine_finish).setOnClickListener(this);
        contentView.findViewById(R.id.mine_order).setOnClickListener(this);
        contentView.findViewById(R.id.mine_reserve).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SelfEvent event) {
        setLoginState(true);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MinePresenter();
    }

    @Override
    public void onClick(View view) {
        if (!checkUser(getActivity())) return;

        final Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_head:
                intent.setClass(getActivity(), SelfActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_login:
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_register:
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_verify:
                intent.setClass(getActivity(), VerifyActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_update:
                ToastUtils.ToastMessage(getActivity(), R.string.no_function);
                break;
            case R.id.mine_setting:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_cover:
                chooseCover();
                break;
            case R.id.self_scan:
                intent.setClass(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;

            case R.id.mine_wallet:
                intent.setClass(getActivity(), WalletActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_order:
                intent.setClass(getActivity(), OrderActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_reserve:
                intent.setClass(getActivity(), ReserveActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_company:
                dealWithCompany(intent);
                break;
            case R.id.mine_tenement:
                intent.setClass(getActivity(), PropertyListActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_all:
                intent.setClass(getActivity(), OrderActivity.class);
                intent.putExtra(Constant.TYPE, 0);
                startActivity(intent);
                break;
            case R.id.mine_pay:
                intent.setClass(getActivity(), OrderActivity.class);
                intent.putExtra(Constant.TYPE, 1);
                startActivity(intent);
                break;
            case R.id.mine_receive:
                intent.setClass(getActivity(), OrderActivity.class);
                intent.putExtra(Constant.TYPE, 2);
                startActivity(intent);
                break;
            case R.id.mine_finish:
                intent.setClass(getActivity(), OrderActivity.class);
                intent.putExtra(Constant.TYPE, 3);
                startActivity(intent);
                break;
            case R.id.mine_sign_in:
                intent.setClass(getActivity(), SignActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_sign_icon:
                if (TextUtils.equals(tvSign.getText().toString().trim(),getString(R.string.sign_in))){
                    signIn();
                }
                break;
        }
    }

    private TextView tvSign;

    private void signIn() {
        tvSign.setText(R.string.signing_in);
        TribeRetrofit.getInstance().createApi(MainApis.class).signIn(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<BaseResponse<SignResponse>>() {
                    @Override
                    public void call(BaseResponse<SignResponse> baseResponse) {
                        lastTime = TribeDateUtils.dateFormat5(new Date(baseResponse.data.lastTimestamp));
                        SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).setValue(Constant.SIGN_IN, lastTime);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        ToastUtils.ToastMessage(getContext(), R.string.sign_success);
                        tvSign.setText(R.string.already_sign_in);
                        tvSign.setTextColor(getResources().getColor(R.color.white));
                        tvSign.setBackgroundResource(R.drawable.signed_background_round);
                    }
                });
    }

    public void chooseCover() {
        ChoosePhotoPanel window = new ChoosePhotoPanel(getActivity(), new ChoosePhotoPanel.OnSelectedFinished() {
            @Override
            public void onSelected(final String path) {
                showLoadingDialog();
                TribeUploader.getInstance().uploadFile("cover.jpeg", "", path, new TribeUploader.UploadCallback() {
                    @Override
                    public void uploadSuccess(UploadResponseBody body) {
                        dismissDialog();
                        ToastUtils.ToastMessage(mContext, mContext.getString(R.string.upload_success));
                        updateUserCover(body, path);
                    }

                    @Override
                    public void uploadFail() {
                        dismissDialog();
                        ToastUtils.ToastMessage(mContext, mContext.getString(R.string.upload_fail));
                    }
                });
            }
        });
        window.show();
    }

    public void dealWithCompany(final Intent intent) {
        String companyID = new UserInfoDao().findFirst().getCompanyID();
        if (companyID != null) {
            intent.setClass(mContext, CompanyDetailActivity.class);
            startActivity(intent);
        } else {
            intent.setClass(mContext, CompanyActivity.class);
            startActivity(intent);
        }
    }

    private void updateUserCover(final UploadResponseBody body, final String path) {
        final String url = body.objectKey;
        new MainModel().updateUser(TribeApplication.getInstance().getUserInfo().getId(),
                "cover", url, new org.xutils.common.Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.contains("200")) {
                            UserInfoEntity userInfo = TribeApplication.getInstance().getUserInfo();
                            userInfo.setCover(url);
                            new UserInfoDao().update(userInfo);
                            mCover.setImageURI("file://" + path);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ToastUtils.ToastMessage(getActivity(), R.string.connect_fail);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
    }

    public void setLoginState(boolean loginState) {
        if (null == llUnLogin || null == llLogin) {
            SystemClock.sleep(300);
        }
        if (loginState) {
            llLogin.setVisibility(View.VISIBLE);
            llUnLogin.setVisibility(View.GONE);
            String nickname = TribeApplication.getInstance().getUserInfo().getNickname();
            if (!TextUtils.isEmpty(nickname)) {
                mNick.setText(nickname);
            } else {
                mNick.setText("");
            }
            FresoUtils.loadImage(TribeApplication.getInstance().getUserInfo().getPicture(), mHead);
            FresoUtils.loadImage(TribeApplication.getInstance().getUserInfo().getCover(), mCover);
        } else {
            llLogin.setVisibility(View.GONE);
            llUnLogin.setVisibility(View.VISIBLE);
            FresoUtils.loadImage("", mHead);
            FresoUtils.loadImage("", mCover);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String currentTime = TribeDateUtils.dateFormat5(new Date(System.currentTimeMillis()));
        if (lastTime == null) {
            lastTime = SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).getStringValue(Constant.SIGN_IN);
        }
        if (TextUtils.equals(currentTime, lastTime)) {
            tvSign.setText(R.string.already_sign_in);
            tvSign.setTextColor(getResources().getColor(R.color.white));
            tvSign.setBackgroundResource(R.drawable.signed_background_round);
        } else {
            tvSign.setText(R.string.sign_in);
            tvSign.setTextColor(getResources().getColor(R.color.custom_blue));
            tvSign.setBackgroundResource(R.drawable.sign_background_round);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
