package com.gs.buluo.app.view.fragment;

import android.os.Bundle;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.OrderAdapter;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.eventbus.PaymentEvent;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.OrderPresenter;
import com.gs.buluo.app.view.impl.IOrderView;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.app.view.widget.recyclerHelper.NewRefreshRecyclerView;
import com.gs.buluo.app.view.widget.recyclerHelper.OnRefreshListener;
import com.gs.buluo.common.widget.StatusLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderFragment extends BaseFragment implements IOrderView {
    private int type;

    @BindView(R.id.order_list)
    NewRefreshRecyclerView recyclerView;
    @BindView(R.id.order_list_layout)
    StatusLayout mStatusLayout;

    OrderAdapter adapter;

    List<OrderBean> list = new ArrayList<>();
    private boolean refreshing;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new OrderAdapter(R.layout.order_list_item, list);
        recyclerView.setAdapter(adapter);
        mStatusLayout.showProgressView();
        ((OrderPresenter) mPresenter).getOrderListFirst(type);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                ((OrderPresenter) mPresenter).getOrderListMore();
            }
        }, recyclerView.getRecyclerView());
        recyclerView.setRefreshAction(new OnRefreshListener() {
            @Override
            public void onAction() {
                refreshing = true;
                ((OrderPresenter) mPresenter).getOrderListFirst(type);
            }
        });

        EventBus.getDefault().register(this);
    }

    //订单详情付款成功后，刷新订单列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderChanged(PaymentEvent event) {
        if (adapter != null) {
            ((OrderPresenter) mPresenter).getOrderListFirst(type);
            adapter.clearData();
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new OrderPresenter();
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void getOrderInfoSuccess(OrderResponse data) {
        mStatusLayout.showContentView();
        if (refreshing) {
            recyclerView.setRefreshFinished();
            refreshing = false;
        }
        adapter.addData(data.content);
        if (adapter.getData().size() == 0) {
            mStatusLayout.showEmptyView(getString(R.string.no_order));
            return;
        }
//        adapter.loadMoreComplete();
        if (!data.hasMore) {
            adapter.loadMoreEnd();
        }
    }

    @Override
    public void updateSuccess(OrderBean status) {
    }

    @Override
    public void getOrderDetail(OrderBean data) {
    }

    @Override
    public void showError(int res) {
        mStatusLayout.showErrorView(getString(res));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
