package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.StoreInfoGoodsListAdapter;
import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.GoodsOfStorePresenter;
import com.gs.buluo.app.view.impl.IGoodsView;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.app.view.widget.recyclerHelper.NewRefreshRecyclerView;
import com.gs.buluo.app.view.widget.recyclerHelper.OnRefreshListener;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Solang on 2017/7/25.
 */

public class GoodsOfStoreActivity extends BaseActivity implements IGoodsView {
    @Bind(R.id.goods_list_layout)
    StatusLayout statusLayout;
    @Bind(R.id.goods_list)
    NewRefreshRecyclerView recyclerView;
    @Bind(R.id.tv_store_name)
    TextView tvNmae;
    List<ListGoods> list;

    private StoreInfoGoodsListAdapter adapter;
    private String storeId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods_store;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        storeId = getIntent().getStringExtra(Constant.STORE_ID);
        tvNmae.setText(getIntent().getStringExtra(Constant.STORE_NAME));
        list = new ArrayList<>();
        adapter = new StoreInfoGoodsListAdapter(R.layout.store_goods_list_item, list);
        recyclerView.setAdapter(adapter);
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ((GoodsOfStorePresenter) mPresenter).getGoodsList(storeId);
        statusLayout.showProgressView();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                ((GoodsOfStorePresenter) mPresenter).loadMore(storeId);
            }
        }, recyclerView.getRecyclerView());
        recyclerView.setRefreshAction(new OnRefreshListener() {
            @Override
            public void onAction() {
                adapter.clearData();
                ((GoodsOfStorePresenter) mPresenter).getGoodsList(storeId);
            }
        });
        statusLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoodsOfStorePresenter) mPresenter).getGoodsList(storeId);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getCtx(), GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_ID, ((ListGoods) adapter.getData().get(position)).id);
                intent.putExtra(Constant.GOODS_PIC, ((ListGoods) adapter.getData().get(position)).mainPicture);
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        GoodsOfStoreActivity.this,
                        new Pair<>(view.findViewById(R.id.goods_list_picture),
                                Constant.DETAIL_HEADER_IMAGE));
                ActivityCompat.startActivity(getCtx(), intent, activityOptions.toBundle());
            }
        });
    }

    @Override
    public void getGoodsInfo(GoodList responseList) {
        recyclerView.setRefreshFinished();
        adapter.setNewData(responseList.content);
        statusLayout.showContentView();
        if (!responseList.hasMore) {
            adapter.loadMoreEnd(true);
            return;
        }
        if (adapter.getData().size() == 0) {
            statusLayout.showEmptyView(getString(R.string.no_goods));
        }
    }

    @Override
    public void getGoodsMore(GoodList data) {
        adapter.addData(data.content);
//        adapter.loadMoreComplete();
        if (!data.hasMore) {
            adapter.loadMoreEnd(false);
        }
    }

    @Override
    public void loadMoreError(int code) {
        adapter.loadMoreFail();
    }

    @Override
    public void showError(int res) {
        recyclerView.setRefreshFinished();
        statusLayout.showErrorView(getString(res));
    }

    @Override
    protected BasePresenter getPresenter() {
        return new GoodsOfStorePresenter();
    }
}
