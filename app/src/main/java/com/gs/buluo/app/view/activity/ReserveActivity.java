package com.gs.buluo.app.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ReserveListAdapter;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponse;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.ReservePresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IReserveView;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReserveActivity extends BaseActivity implements IReserveView{
    @Bind(R.id.reserve_list)
    RefreshRecyclerView recyclerView;
    private ReserveListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new ReserveListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNeedLoadMore(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                ((ReservePresenter)mPresenter).getReserveMore("");
            }
        });

        findViewById(R.id.reserve_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((ReservePresenter)mPresenter).getReserveListFirst("");
        showLoadingDialog();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_reserve;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new ReservePresenter();
    }

    @Override
    public void getReserveSuccess(ReserveResponse.ReserveResponseBody data) {
        dismissDialog();
        adapter.addAll(data.content);
        if (data.content.size()==0){
            recyclerView.showNoData(R.string.no_order);
            return;
        }
        if (!data.hasMore){
            recyclerView.showNoMore();
        }
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(res));
        dismissDialog();
    }
}
