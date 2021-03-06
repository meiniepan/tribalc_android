package com.gs.buluo.app.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.HighBuyListAdapter;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.bean.StoreListResponse;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/18.
 */

public class HighBuyFragment extends BaseFragment implements XRecyclerView.LoadingListener {
    @BindView(R.id.list_high_buy)
    XRecyclerView mXRecyclerView;
    @BindView(R.id.high_buy_status)
    StatusLayout mStatusLayout;

    private HighBuyListAdapter mAdapter;
    private ArrayList<StoreInfo> datas = new ArrayList<>();
    private String nextSkip;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_high_buy;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mXRecyclerView.setLoadingListener(this);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallPulse);
        mAdapter = new HighBuyListAdapter(mContext, datas);
        mXRecyclerView.setAdapter(mAdapter);
        mStatusLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void fetchData() {
        getData();
    }

    public void getData() {
        datas.clear();
        mStatusLayout.showProgressView();
        getDataFirst();
    }

    private void getDataFirst() {
        if (TribeApplication.getInstance().getUserInfo() == null) return;
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreListFirst(TribeApplication.getInstance().getUserInfo().getId(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreListResponse>>() {
                    @Override
                    public void onNext(BaseResponse<StoreListResponse> response) {
                        datas.addAll(response.data.content);
                        nextSkip = response.data.nextSkip;
                        if (datas.size() > 0)
                            mStatusLayout.showContentView();
                        else
                            mStatusLayout.showEmptyView("暂无信息");
                        mXRecyclerView.refreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        mStatusLayout.showErrorView(getString(R.string.connect_fail));
                    }
                });
    }

    @Override
    public void onRefresh() {
        datas.clear();
        mAdapter.notifyDataSetChanged();
        getDataFirst();
    }

    @Override
    public void onLoadMore() {
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreListMore(TribeApplication.getInstance().getUserInfo().getId(), 3, nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreListResponse>>() {
                    @Override
                    public void onNext(BaseResponse<StoreListResponse> response) {
                        nextSkip = response.data.nextSkip;
                        mXRecyclerView.loadMoreComplete();
                        int pos = datas.size();
                        datas.addAll(response.data.content);
                        mAdapter.notifyItemRangeInserted(pos + 1, response.data.content.size());

                        if (!response.data.hasMore) {
                            mXRecyclerView.loadMoreComplete();
                            mXRecyclerView.setNoMore(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        mStatusLayout.showErrorView("获取嗨购信息失败");
                    }
                });
    }
}
