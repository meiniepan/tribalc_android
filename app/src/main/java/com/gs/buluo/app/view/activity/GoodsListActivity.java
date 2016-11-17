package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.GoodsListAdapter;
import com.gs.buluo.app.bean.Goods;
import com.gs.buluo.app.bean.ResponseBody.GoodResponseList;
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
    List<Goods> list;
    private boolean hasMore;
    private GoodsListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list=new ArrayList<>();
        adapter = new GoodsListAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, GridLayoutManager.HORIZONTAL, 16, getResources().getColor(R.color.divide_gray)));
        recyclerView.setNeedLoadMore(true);

        ((GoodsPresenter)mPresenter).getGoodsList();

        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((GoodsPresenter)mPresenter).loadMore();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods;
    }

    @Override
    public void getGoodsInfo(GoodResponseList responseList) {
        list=responseList.content;
        adapter.addAll(list);
        hasMore = responseList.hasMore;
        if (!hasMore){
            adapter.showNoMore();
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
