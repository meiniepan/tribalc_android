package com.gs.buluo.app.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.HighBuyListAdapter;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.bean.StoreListResponse;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.gs.buluo.common.widget.loadMoreRecycle.RefreshRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/18.
 */

public class HighBuyFragment extends BaseFragment implements XRecyclerView.LoadingListener {
    @Bind(R.id.list_high_buy)
    XRecyclerView mListView;
    @Bind(R.id.high_buy_status)
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
        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mListView.setLoadingListener(this);
        mAdapter = new HighBuyListAdapter(mContext, datas);
        mListView.setAdapter(mAdapter);
        getData();
    }

    public void getData() {
        datas.clear();
        mStatusLayout.showProgressView();
        getDataFirst();
    }

    private void getDataFirst() {
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreListFirst(TribeApplication.getInstance().getUserInfo().getId(), 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreListResponse>>() {
                    @Override
                    public void onNext(BaseResponse<StoreListResponse> response) {
                        datas = response.data.content;
                        nextSkip = response.data.nextSkip;
                        if (datas.size() > 0)
                            mStatusLayout.showContentView();
                        else
                            mStatusLayout.showEmptyView("暂无信息");
                        mListView.refreshComplete();
                        mAdapter.setDatas(datas);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        mStatusLayout.showErrorView("获取嗨购信息失败");
                    }
                });
    }

    @Override
    public void onRefresh() {
        getDataFirst();
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(mContext, "已经到底了", Toast.LENGTH_SHORT).show();
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreListMore(TribeApplication.getInstance().getUserInfo().getId(),5,nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreListResponse>>() {
                    @Override
                    public void onNext(BaseResponse<StoreListResponse> response) {
                        nextSkip = response.data.nextSkip;
                        mListView.loadMoreComplete();
                        datas.addAll(datas.size() - 1, response.data.content);
                        mAdapter.notifyItemRangeInserted(datas.size() , response.data.content.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        mStatusLayout.showErrorView("获取嗨购信息失败");
                    }
                });
    }
}
