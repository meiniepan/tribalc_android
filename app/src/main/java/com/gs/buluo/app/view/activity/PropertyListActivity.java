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
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.network.PropertyService;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;


import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyListActivity extends BaseActivity implements View.OnClickListener {
    private RefreshRecyclerView mRecyclerView;
    private List<ListPropertyManagement> mData;
    private Context mContext;
    private PropertyFixListAdapter mAdapter;
    private String sortSkip;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext=this;
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.property_list_recycleView);
        findViewById(R.id.property_list_back).setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PropertyFixListAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNeedLoadMore(true);
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
        if (mAdapter!=null) mAdapter.clear();
        initData(false);
    }

    private void initData(final boolean isRefresh) {
        if (!isRefresh){
            showLoadingDialog();
        }
        TribeRetrofit.getInstance().createApi(PropertyService.class).getPropertyFixList(TribeApplication.getInstance().getUserInfo().getId()).
                enqueue(new TribeCallback<PropertyFixListResponseData>() {
                    @Override
                    public void onSuccess(Response<BaseCodeResponse<PropertyFixListResponseData>> response) {
                        dismissDialog();
                        sortSkip = response.body().data.nextSkip;
                        mData = response.body().data.content;

                        mAdapter.addAll(mData);
                        if (mData.size()==0){
                            mRecyclerView.showNoData(R.string.no_order);
                            return;
                        }

                        if (isRefresh){
                            mRecyclerView.dismissSwipeRefresh();
                        }
                    }

                    @Override
                    public void onFail(int responseCode, BaseCodeResponse<PropertyFixListResponseData> body) {
                        ToastUtils.ToastMessage(mContext,R.string.connect_fail);
                        dismissDialog();
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
        TribeRetrofit.getInstance().createApi(PropertyService.class).getPropertyFixListMore(TribeApplication.getInstance().getUserInfo().getId(),sortSkip).enqueue(new Callback<BaseCodeResponse<PropertyFixListResponseData>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<PropertyFixListResponseData>> call, Response<BaseCodeResponse<PropertyFixListResponseData>> response) {
                if (response.body().code==200) {
                    sortSkip = response.body().data.nextSkip;
                    mData = response.body().data.content;
                    mAdapter.addAll(mData);
                    if (!response.body().data.hasMore){
                        mRecyclerView.showNoMore();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<PropertyFixListResponseData>> call, Throwable t) {
                ToastUtils.ToastMessage(mContext,R.string.connect_fail);
            }
        });
    }
}
