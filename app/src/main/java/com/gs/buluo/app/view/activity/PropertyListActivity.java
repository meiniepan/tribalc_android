package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.PropertyFixListAdapter;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.PropertyFixListResponseData;
import com.gs.buluo.app.network.PropertyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PropertyListActivity extends BaseActivity implements View.OnClickListener {
    private RefreshRecyclerView mRecyclerView;
    private List<ListPropertyManagement> mData;
    private Context mContext;
    private PropertyFixListAdapter mAdapter;
    private String sortSkip;
    @Bind(R.id.property_list_layout)
    StatusLayout mStatusLayout;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = this;
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.property_list_recycleView);
        findViewById(R.id.property_list_back).setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PropertyFixListAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.showSwipeRefresh();

        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                mAdapter.clear();
                initData(true);
            }
        });
        initData(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mAdapter != null) mAdapter.clear();
        initData(false);
    }

    private void initData(final boolean isRefresh) {
        mStatusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(PropertyApis.class).getPropertyFixList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<PropertyFixListResponseData>>(!isRefresh) {
                    @Override
                    public void onNext(BaseResponse<PropertyFixListResponseData> response) {
                        mStatusLayout.showContentView();
                        sortSkip = response.data.nextSkip;
                        mData = response.data.content;
                        mAdapter.addAll(mData);
                        if (mData.size() == 0) {
                            mRecyclerView.showNoData(R.string.no_order);
                            return;
                        }
                        if (isRefresh) {
                            mRecyclerView.dismissSwipeRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mStatusLayout.showErrorView(getString(R.string.connect_fail));
                    }
                });
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getMore();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_property_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.property_list_back:
                finish();
                break;
        }
    }

    public void getMore() {
        TribeRetrofit.getInstance().createApi(PropertyApis.class).getPropertyFixListMore(TribeApplication.getInstance().getUserInfo().getId(), sortSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<PropertyFixListResponseData>>() {
                    @Override
                    public void onNext(BaseResponse<PropertyFixListResponseData> response) {
                        sortSkip = response.data.nextSkip;
                        mData = response.data.content;
                        mAdapter.addAll(mData);
                        if (!response.data.hasMore) {
                            mRecyclerView.showNoMore();
                        }
                    }
                });
    }
}
