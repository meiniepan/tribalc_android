package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.BillListAdapter;
import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.bean.ResponseBody.BillResponseData;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.BillPresenter;
import com.gs.buluo.app.view.impl.IBillView;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class BillActivity extends BaseActivity implements IBillView, View.OnClickListener {
    @Bind(R.id.bill_list)
    RefreshRecyclerView recyclerView;
    @Bind(R.id.bill_list_layout)
    StatusLayout mStatusLayout;

    BillListAdapter adapter;
    List<BillEntity> list;
    private String targetId;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (getIntent().getStringExtra(Constant.TARGET_ID) == null) {
            targetId = TribeApplication.getInstance().getUserInfo().getId();
        }else {
            targetId = getIntent().getStringExtra(Constant.TARGET_ID);
        }
        list = new ArrayList<>();
        adapter = new BillListAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 4, getResources().getColor(R.color.tint_bg)));
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((BillPresenter) mPresenter).loadMoreBill(targetId);
            }
        });
        showLoadingDialog();
        ((BillPresenter) mPresenter).getBillListFirst(targetId);

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
    public void getBillSuccess(BillResponseData response) {
        adapter.addAll(response.content);
        if (!response.hasMoren) {
            adapter.showNoMore();
        }
    }

    @Override
    public void showError(int res) {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        }
    }
}
