package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BillListAdapter;
import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.BillPresenter;
import com.gs.buluo.app.view.impl.IBillView;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class BillActivity extends BaseActivity implements IBillView, View.OnClickListener {
    @Bind(R.id.bill_list)
    RefreshRecyclerView recyclerView;

    BillListAdapter adapter;
    List<BillEntity> list;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        list=new ArrayList<>();
        adapter=new BillListAdapter(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL,4, getResources().getColor(R.color.tint_bg)));
        recyclerView.setNeedLoadMore(true);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((BillPresenter)mPresenter).loadMoreBill();
            }
        });

        ((BillPresenter)mPresenter).getBillListFirst();

        findViewById(R.id.bill_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BillPresenter();
    }

    @Override
    public void getBillSuccess(BillResponse.BillResponseData response) {
        adapter.addAll(response.content);
        if (!response.hasMoren){
            adapter.showNoMore();
        }
    }

    @Override
    public void showError(int res) {

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
        }
    }
}
