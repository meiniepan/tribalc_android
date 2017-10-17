package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.RentPlanListAdapter;
import com.gs.buluo.app.bean.PayRentEvent;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/8/8.
 */

public class CompanyPayRentPlanActivity extends BaseActivity {
    @BindView(R.id.tv_rent_plan_apartment)
    TextView tvApartment;
    @BindView(R.id.lv_rent_plan_list)
    ListView mListView;
    @BindView(R.id.sl_rent_plan)
    StatusLayout mStatusLayout;
    RentPlanListAdapter mAdapter;
    Context mCtx;
    ArrayList<RentPlanItem> listData = new ArrayList<>();
    private String protocolId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_rent_plan;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        protocolId = getIntent().getStringExtra(Constant.RENT_PROTOCOL_ID);
        tvApartment.setText(TribeApplication.getInstance().getUserInfo().getCompanyName());
        EventBus.getDefault().register(this);
        getData();
        mAdapter = new RentPlanListAdapter(mCtx,listData);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(PayRentEvent event) {
        getData();
    }

    private void getData() {
        String uid = TribeApplication.getInstance().getUserInfo().getId();
        mStatusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).getRentPlanItems(protocolId, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<RentPlanItem>>>() {
                    @Override
                    public void onNext(BaseResponse<List<RentPlanItem>> listBaseResponse) {
                        mStatusLayout.showContentView();
                        setData(listBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        mStatusLayout.showErrorView();
                    }
                });
    }

    public void setData(List<RentPlanItem> data) {
        if (data == null || data.size() == 0) {
            mStatusLayout.showEmptyView("尚无计划");
            return;
        }
        listData.clear();
        listData.addAll(data);
        mStatusLayout.showContentView();
        mAdapter.setData(listData, protocolId, "", TribeApplication.getInstance().getUserInfo().getCompanyName());
        mAdapter.refresh();
        CommonUtils.setListViewHeightBasedOnChildren(mListView);
    }
}
