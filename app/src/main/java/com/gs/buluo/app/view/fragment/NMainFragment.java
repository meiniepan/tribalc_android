package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.HomeMessageAdapter;
import com.gs.buluo.app.bean.HomeMessage;
import com.gs.buluo.app.bean.HomeMessageResponse;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.activity.MainSearchActivity;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/12.
 */

public class NMainFragment extends BaseFragment implements View.OnClickListener, XRecyclerView.LoadingListener {
    @Bind(R.id.home_rc)
    XRecyclerView mRefreshRecycleView;
    private ArrayList<HomeMessage> datas = new ArrayList<>();
    private HomeMessageAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_n;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        getActivity().findViewById(R.id.tv_search).setOnClickListener(this);
        initRecyclerView();
        getData();
    }

    private void initRecyclerView() {
        mRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_head, null);
        mRefreshRecycleView.addHeaderView(view);
        mRefreshRecycleView.setRefreshPosition(1);
        mRefreshRecycleView.setLoadingListener(this);
    }

    private void initView(final ArrayList<HomeMessage> lists) {
        adapter = new HomeMessageAdapter(getActivity(), lists);
        mRefreshRecycleView.setAdapter(adapter);
    }

    public void getData() {
        datas.clear();
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessage(TribeApplication.getInstance().getUserInfo().getId(), 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        datas = response.data.content;
                        initView(response.data.content);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        ToastUtils.ToastMessage(mContext, "获取消息错误");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(),
                5, datas.get(0).createTime
                , true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
//                                if (response.data.hasMore) {
                        mRefreshRecycleView.refreshComplete();
                        datas.addAll(0, response.data.content);
                        adapter.notifyItemRangeInserted(2, response.data.content.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        ToastUtils.ToastMessage(mContext, "获取消息错误");
                    }
                });
    }

    @Override
    public void onLoadMore() {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(),
                1, datas.get(datas.size()-1).createTime
                , false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
//                                if (response.data.hasMore) {
                        mRefreshRecycleView.refreshComplete();
                        datas.addAll(datas.size()-1, response.data.content);
                        adapter.notifyItemRangeInserted(datas.size()+1, response.data.content.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        ToastUtils.ToastMessage(mContext, "获取消息错误");
                    }
                });
    }
}
