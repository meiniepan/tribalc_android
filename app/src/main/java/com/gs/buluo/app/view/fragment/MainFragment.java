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
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.activity.AddPartFixActivity;
import com.gs.buluo.app.view.activity.BindCompanyActivity;
import com.gs.buluo.app.view.activity.CaptureActivity;
import com.gs.buluo.app.view.activity.DoorListActivity;
import com.gs.buluo.app.view.activity.IdentifyActivity;
import com.gs.buluo.app.view.activity.MainSearchActivity;
import com.gs.buluo.app.view.activity.OpenDoorActivity;
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

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/12.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener, XRecyclerView.LoadingListener {
    @Bind(R.id.home_rc)
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
        getData();
        EventBus.getDefault().register(this);
    }


    private void initRecyclerView() {
        mRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_head, null);
        mRefreshRecycleView.addHeaderView(view);
        mRefreshRecycleView.setRefreshPosition(1);
        mRefreshRecycleView.setLoadingListener(this);
        mRefreshRecycleView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        adapter = new HomeMessageAdapter(getActivity(), datas);
        mRefreshRecycleView.setAdapter(adapter);
    }

    public void getData() {
        datas.clear();
        doGetData();
    }

    private void doGetData() {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessage(TribeApplication.getInstance().getUserInfo().getId(), 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        mRefreshRecycleView.refreshComplete();
                        firstRequestSuccess = true;
                        datas.addAll(response.data.content);
                        noMore = false;
                        if (datas.size() > 3 && !response.data.hasMore)
                            noMore = true;
                        mRefreshRecycleView.setNoMore(noMore);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mRefreshRecycleView.refreshComplete();
                        ToastUtils.ToastMessage(mContext, "获取消息错误");
                    }
                });
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
                getLockInfo();
                break;
            case R.id.btn_fix:
            case R.id.small_fix:
                checkIsReady();
                break;
            case R.id.btn_conference:
            case R.id.small_conference:
                ToastUtils.ToastMessage(mContext, R.string.not_open);
                break;
        }
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
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(),
                8, createTime
                , true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        mRefreshRecycleView.refreshComplete();
                        datas.addAll(0, response.data.content);
                        if (noMore)
                            mRefreshRecycleView.setNoMore(true);
                        adapter.notifyItemRangeInserted(2, response.data.content.size());
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mRefreshRecycleView.refreshComplete();
                        ToastUtils.ToastMessage(mContext, "获取消息错误");
                    }
                });
    }

    @Override
    public void onLoadMore() {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(),
                5, datas.get(datas.size() - 1).createTime
                , false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        noMore = false;
                        if (!response.data.hasMore) {
                            noMore = true;
                            mRefreshRecycleView.loadMoreComplete();
                            mRefreshRecycleView.setNoMore(true);
                            return;
                        }
                        mRefreshRecycleView.loadMoreComplete();
                        int pos = datas.size();
                        datas.addAll(response.data.content);
                        adapter.notifyItemRangeInserted(pos + 2, response.data.content.size());
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mRefreshRecycleView.loadMoreComplete();
                        ToastUtils.ToastMessage(mContext, "获取消息错误");
                    }
                });
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
                            com.gs.buluo.app.utils.ToastUtils.ToastMessage(getActivity(), R.string.no_door);
                        } else {
                            com.gs.buluo.app.utils.ToastUtils.ToastMessage(getActivity(), R.string.connect_fail);
                        }
                    }
                });
    }

    private void checkIsReady() {
        UserInfoDao dao = new UserInfoDao();
        UserInfoEntity entity = dao.findFirst();
        String name = entity.getName();

        if (TextUtils.isEmpty(name)) {
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
        } else {
            //判断用户是否绑定公司
            String communityID = entity.getCommunityID();
            String enterpriseID = entity.getCompanyID();
            String companyName = entity.getCompanyName();
            String communityName = entity.getCommunityName();

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
            } else {
                //用户绑定和个人认证都进行了
                Intent intent = new Intent(mContext, AddPartFixActivity.class);
                PropertyBeen propertyBeen = new PropertyBeen();
                propertyBeen.communityID = communityID;
                propertyBeen.enterpriseID = enterpriseID;
                propertyBeen.name = name;
                propertyBeen.enterpriseName = companyName;
                propertyBeen.communityName = communityName;
                intent.putExtra(Constant.ForIntent.PROPERTY_BEEN, propertyBeen);
                startActivity(intent);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void refreshData(PayRentEvent event) {
        getData();
    }

}
