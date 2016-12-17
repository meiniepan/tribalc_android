package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.GoodsListAdapter;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.GoodsPresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IGoodsView;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsListActivity extends BaseActivity implements IGoodsView {

    @Bind(R.id.goods_list)
    RefreshRecyclerView recyclerView;
    List<ListGoods> list;
    private boolean hasMore;
    private GoodsListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list=new ArrayList<>();
        adapter = new GoodsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, GridLayoutManager.HORIZONTAL, 16, getResources().getColor(R.color.tint_bg)));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, GridLayoutManager.VERTICAL, 12, getResources().getColor(R.color.tint_bg)));
        recyclerView.setNeedLoadMore(true);

        ((GoodsPresenter)mPresenter).getGoodsList();
        showLoadingDialog();

        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((GoodsPresenter)mPresenter).loadMore();
                showLoadingDialog();
            }
        });

        findViewById(R.id.goods_list_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.good_list_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TribeApplication.getInstance().getUserInfo()==null)
                    startActivity(new Intent(GoodsListActivity.this,LoginActivity.class));
                else
                    startActivity(new Intent(GoodsListActivity.this,ShoppingCarActivity.class));
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods;
    }

    @Override
    public void getGoodsInfo(GoodList responseList) {
        dismissDialog();
        list=responseList.content;
        adapter.addAll(list);
        hasMore = responseList.hasMore;
        if (!hasMore){
            adapter.showNoMore();
            return;
        }
        if (list.size()==0){
            recyclerView.showNoData(R.string.no_goods);
        }
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(res));
    }

    @Override
    protected BasePresenter getPresenter() {
        return new GoodsPresenter();
    }
}
