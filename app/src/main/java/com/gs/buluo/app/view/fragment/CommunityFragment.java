package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.BankCardListAdapter;
import com.gs.buluo.app.adapter.DoorListAdapter;
import com.gs.buluo.app.adapter.GoodsListAdapter;
import com.gs.buluo.app.adapter.HighBuyListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.bean.LockEquip;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.GoodsPresenter;
import com.gs.buluo.app.utils.FastScrollGridManager;
import com.gs.buluo.app.utils.MyRecyclerViewScrollListener;
import com.gs.buluo.app.view.activity.GoodsDetailActivity;
import com.gs.buluo.app.view.activity.LoginActivity;
import com.gs.buluo.app.view.activity.ShoppingCarActivity;
import com.gs.buluo.app.view.impl.IGoodsView;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.app.view.widget.recyclerHelper.NewRefreshRecyclerView;
import com.gs.buluo.app.view.widget.recyclerHelper.OnRefreshListener;
import com.gs.buluo.common.widget.StatusLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Solang on 2017/7/25.
 */

public class CommunityFragment extends BaseFragment implements IGoodsView {
    @BindView(R.id.goods_list_layout)
    StatusLayout statusLayout;
    @BindView(R.id.goods_list)
    NewRefreshRecyclerView recyclerView;
    @BindView(R.id.rl_to_top)
    View toTop;
    List<ListGoods> list;
    private GoodsListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list = new ArrayList<>();
        adapter = new GoodsListAdapter(R.layout.good_list_item, list);
        recyclerView.setAdapter(adapter);
        final MyRecyclerViewScrollListener scrollListener = new MyRecyclerViewScrollListener(mContext,toTop);
        recyclerView.getRecyclerView().addOnScrollListener(scrollListener);
        recyclerView.getRecyclerView().setLayoutManager(new FastScrollGridManager(getContext(), 2));
        recyclerView.getRecyclerView().addItemDecoration(new RecycleViewDivider(
                getActivity(), GridLayoutManager.HORIZONTAL, 16, getResources().getColor(R.color.tint_bg)));
        recyclerView.getRecyclerView().addItemDecoration(new RecycleViewDivider(
                getActivity(), GridLayoutManager.VERTICAL, 12, getResources().getColor(R.color.tint_bg)));
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                ((GoodsPresenter) mPresenter).loadMore();
            }
        }, recyclerView.getRecyclerView());
        recyclerView.setRefreshAction(new OnRefreshListener() {
            @Override
            public void onAction() {
                scrollListener.setRefresh();
                adapter.clearData();
                ((GoodsPresenter) mPresenter).getGoodsList();
            }
        });
        statusLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoodsPresenter) mPresenter).getGoodsList();
            }
        });

        getActivity().findViewById(R.id.good_list_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TribeApplication.getInstance().getUserInfo() == null)
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                else
                    startActivity(new Intent(getActivity(), ShoppingCarActivity.class));
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_ID, ((ListGoods) adapter.getData().get(position)).id);
                intent.putExtra(Constant.GOODS_PIC, ((ListGoods) adapter.getData().get(position)).mainPicture);
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        new Pair<>(view.findViewById(R.id.goods_list_picture),
                                Constant.DETAIL_HEADER_IMAGE));
                ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
            }
        });
        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getRecyclerView().smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void fetchData() {
        ((GoodsPresenter) mPresenter).getGoodsList();
        statusLayout.showProgressView();
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
            adapter.loadMoreEnd(true);
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
        return new GoodsPresenter();
    }
}
