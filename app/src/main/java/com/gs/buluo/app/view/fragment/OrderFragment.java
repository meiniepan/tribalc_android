package com.gs.buluo.app.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.OrderListAdapter;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.OrderPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IOrderView;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderFragment extends BaseFragment implements IOrderView {

    private final int ALL = 0;
    private final int PAY = 1;
    private final int RECEIVE = 2;
    private final int COMPLETE = 3;
    private int type;

    @Bind(R.id.order_list)
    RefreshRecyclerView recyclerView;

    OrderListAdapter adapter;

    List<OrderBean> list;
    private String nextSkip;

    public OrderFragment(int position) {
        super();
        type=position;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter=new OrderListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNeedLoadMore(true);

        adapter.setType(type);
        ((OrderPresenter)mPresenter).getOrderListFirst();
        if (type==0){
            showLoadingDialog();
        }else if (type==1) {
//            ((OrderPresenter)mPresenter).getOrderListFirst();
        } else if (type==2){
            showLoadingDialog();
//            ((OrderPresenter)mPresenter).getOrderListFirst();
        } else {
//            ((OrderPresenter)mPresenter).getOrderListFirst();
        }
        adapter.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((OrderPresenter)mPresenter).getOrderListMore(nextSkip);
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return new OrderPresenter();
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void getOrderInfoSuccess(OrderResponse.OrderResponseBean data) {
        list=data.content;
        if (list.size()==0){
            recyclerView.showNoData(R.string.no_order);
        }
        dismissDialog();
        nextSkip=data.nextSkip;

        adapter.addAll(list);
        if (!data.haseMore){
            adapter.showNoMore();
        }
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(getActivity(),getString(res));
        dismissDialog();
    }
}
