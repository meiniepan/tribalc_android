package com.gs.buluo.app.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.HomeMessageAdapter;
import com.gs.buluo.app.bean.HomeMessage;
import com.gs.buluo.app.bean.HomeMessageResponse;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.PayRentEvent;
import com.gs.buluo.app.bean.PropertyBeen;
import com.gs.buluo.app.bean.RequestBodyBean.MultiLockRequest;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.MainFragmentPresenter;
import com.gs.buluo.app.view.activity.AddPartFixActivity;
import com.gs.buluo.app.view.activity.BindCompanyActivity;
import com.gs.buluo.app.view.activity.BoardroomFilterActivity;
import com.gs.buluo.app.view.activity.CaptureActivity;
import com.gs.buluo.app.view.activity.DoorListActivity;
import com.gs.buluo.app.view.activity.IdentifyActivity;
import com.gs.buluo.app.view.activity.MainSearchActivity;
import com.gs.buluo.app.view.activity.OpenDoorActivity;
import com.gs.buluo.app.view.impl.IMainFragmentView;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/12.
 */

public class MainFragment extends BaseFragment implements IMainFragmentView, View.OnClickListener, XRecyclerView.LoadingListener {
    @BindView(R.id.home_rc)
    XRecyclerView mRefreshRecycleView;
    private ArrayList<HomeMessage> datas = new ArrayList<>();
    private HomeMessageAdapter adapter;
    private boolean firstRequestSuccess = false;
    private boolean noMore;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_n;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        getActivity().findViewById(R.id.tv_search).setOnClickListener(this);
        getActivity().findViewById(R.id.btn_scan).setOnClickListener(this);
        getActivity().findViewById(R.id.btn_open_lock).setOnClickListener(this);
        getActivity().findViewById(R.id.btn_fix).setOnClickListener(this);
        getActivity().findViewById(R.id.btn_conference).setOnClickListener(this);
        getActivity().findViewById(R.id.small_scan).setOnClickListener(this);
        getActivity().findViewById(R.id.small_open_lock).setOnClickListener(this);
        getActivity().findViewById(R.id.small_fix).setOnClickListener(this);
        getActivity().findViewById(R.id.small_conference).setOnClickListener(this);
        initRecyclerView();
        if (!EventBus.getDefault().isRegistered(this))EventBus.getDefault().register(this);
    }


    private void initRecyclerView() {
        mRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        View view = LayoutInflater.from(mContext).inflate(R.layout.home_weather_head, null);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WeatherPanel panel = new WeatherPanel(mContext);
//                panel.show();
//            }
//        });
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_head, null);
        if (mRefreshRecycleView.getHeadSize() < 1) {
            mRefreshRecycleView.addHeaderView(view);
        }
        mRefreshRecycleView.setRefreshPosition(1);
        mRefreshRecycleView.setLoadingListener(this);
        mRefreshRecycleView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        adapter = new HomeMessageAdapter(getActivity(), datas);
        mRefreshRecycleView.setAdapter(adapter);
    }

    @Override
    public void fetchData() {
        getData();
    }

    public void getData() {
        datas.clear();
        doGetData();
    }

    private void doGetData() {
        if (TribeApplication.getInstance().getUserInfo() == null || mPresenter == null) return;
        ((MainFragmentPresenter) mPresenter).getHomeMessage();
    }

    @Override
    public void onRefresh() {
        long createTime = 0;
        if (datas != null && datas.size() > 0) {
            createTime = datas.get(0).createTime;
        }
        if (firstRequestSuccess && createTime != 0)
            doRefresh(createTime);
        else doGetData();
    }

    private void doRefresh(long createTime) {
        ((MainFragmentPresenter) mPresenter).getRefreshMessage(createTime);
    }

    @Override
    public void onLoadMore() {
        ((MainFragmentPresenter) mPresenter).getMoreMessage(datas.get(datas.size() - 1).createTime);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_search:
                intent.setClass(getActivity(), MainSearchActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.btn_scan:
            case R.id.small_scan:
                intent.setClass(getActivity(), CaptureActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.btn_open_lock:
            case R.id.small_open_lock:
                if (checkIsReady()) {
                    getLockInfo();
                }
                break;
            case R.id.btn_fix:
            case R.id.small_fix:
                if (checkIsReady()) {
                    UserInfoEntity entity = TribeApplication.getInstance().getUserInfo();
                    intent.setClass(mContext, AddPartFixActivity.class);
                    PropertyBeen propertyBeen = new PropertyBeen();
                    propertyBeen.communityID = entity.getCommunityID();
                    propertyBeen.enterpriseID = entity.getCompanyID();
                    propertyBeen.name = entity.getName();
                    propertyBeen.enterpriseName = entity.getCompanyName();
                    propertyBeen.communityName = entity.getCommunityName();
                    intent.putExtra(Constant.ForIntent.PROPERTY_BEEN, propertyBeen);
                    startActivity(intent);
                }
                break;
            case R.id.btn_conference:
            case R.id.small_conference:
                if (checkIsReady()) {
                    intent.setClass(getActivity(), BoardroomFilterActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }


    public void getLockInfo() {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(DoorApis.class).getMultiKey(TribeApplication.getInstance().getUserInfo().getId(), new MultiLockRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<LockKey>>() {
                    @Override
                    public void onNext(BaseResponse<LockKey> response) {
                        dismissDialog();
                        if (response.code == 300) {
                            Intent intent = new Intent(getActivity(), DoorListActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), OpenDoorActivity.class);
                            intent.putExtra(Constant.DOOR, response.data);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        dismissDialog();
                        if (e.getCode() == 403) {
                            ToastUtils.ToastMessage(getActivity(), R.string.no_door);
                        } else {
                            ToastUtils.ToastMessage(getActivity(), R.string.connect_fail);
                        }
                    }
                });
    }

    private boolean checkIsReady() {
        UserInfoEntity entity = TribeApplication.getInstance().getUserInfo();
        if (TextUtils.isEmpty(entity.getName())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("您好").setMessage("请先进行个人实名认证");
            builder.setPositiveButton("去认证", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(mContext, IdentifyActivity.class));
                }
            });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
            return false;
        } else {
            //判断用户是否绑定公司
            String communityID = entity.getCommunityID();
            String enterpriseID = entity.getCompanyID();
            if (TextUtils.isEmpty(communityID) || TextUtils.isEmpty(enterpriseID)) {
                CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(mContext);
                builder.setTitle("您好").setMessage("请先进行企业绑定");
                builder.setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mContext, BindCompanyActivity.class));
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return false;
            } else {
                return true;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void refreshData(PayRentEvent event) {
        getData();
    }

    @Override
    protected BasePresenter getPresenter() {
        return new MainFragmentPresenter();
    }

    @Override
    public void showError(int res) {

    }

    @Override
    public void getHomeMessageSuccess(HomeMessageResponse messageResponse) {
        mRefreshRecycleView.refreshComplete();
        firstRequestSuccess = true;
        datas.addAll(messageResponse.content);
        noMore = false;
        if (datas.size() > 3 && !messageResponse.hasMore)
            noMore = true;
        mRefreshRecycleView.setNoMore(noMore);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getHomeMessageFail(ApiException e) {
        mRefreshRecycleView.refreshComplete();
        ToastUtils.ToastMessage(mContext, "获取消息错误");
    }

    @Override
    public void getRefreshMessageSuccess(HomeMessageResponse messageResponse) {
        mRefreshRecycleView.refreshComplete();
        datas.addAll(0, messageResponse.content);
        if (noMore)
            mRefreshRecycleView.setNoMore(true);
        adapter.notifyItemRangeInserted(2, messageResponse.content.size());
    }

    @Override
    public void getRefreshMessageFail(ApiException e) {
        mRefreshRecycleView.refreshComplete();
        ToastUtils.ToastMessage(mContext, "获取消息错误");
    }

    @Override
    public void getMoreMessageSuccess(HomeMessageResponse messageResponse) {
        noMore = false;
        if (!messageResponse.hasMore) {
            noMore = true;
            mRefreshRecycleView.loadMoreComplete();
            mRefreshRecycleView.setNoMore(true);
            return;
        }
        mRefreshRecycleView.loadMoreComplete();
        int pos = datas.size();
        datas.addAll(messageResponse.content);
        adapter.notifyItemRangeInserted(pos + 2, messageResponse.content.size());
    }

    @Override
    public void getMoreMessageFail(ApiException e) {
        mRefreshRecycleView.loadMoreComplete();
        ToastUtils.ToastMessage(mContext, "获取消息错误");
    }

}
