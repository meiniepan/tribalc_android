package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.CreditBillAdapter;
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.bean.ResponseBody.CreditBillResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditBillActivity extends BaseActivity {
    @Bind(R.id.credit_status_layout)
    StatusLayout statusLayout;
    @Bind(R.id.credit_history_bill)
    RecyclerView recyclerView;
    private String nextSkip;
    private CreditBillAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        statusLayout.showProgressView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getCreditBillList(TribeApplication.getInstance().getUserInfo().getId(), 20, System.currentTimeMillis() + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CreditBillResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CreditBillResponse> listBaseResponse) {
                        statusLayout.showContentView();
                        setData(listBaseResponse.data.content);
                        nextSkip = listBaseResponse.data.nextSkip;
                        if (!listBaseResponse.data.hasMore) {
                            adapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(e.getDisplayMessage());
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit_bill;
    }

    public void setData(List<CreditBill> data) {
        adapter = new CreditBillAdapter(R.layout.credit_bill_item, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getMore();
            }
        });
    }

    public void getMore() {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getCreditBillList(TribeApplication.getInstance().getUserInfo().getId(), 20, nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CreditBillResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CreditBillResponse> listBaseResponse) {
                        adapter.addData(listBaseResponse.data.content);
                        nextSkip = listBaseResponse.data.nextSkip;
                        if (!listBaseResponse.data.hasMore) {
                            adapter.loadMoreEnd();
                        }
                    }
                });
    }
}
