package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.RentPlanListAdapter;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/6/21.
 */

public class RentPaymentPlanActivity extends BaseActivity {
    @Bind(R.id.tv_rent_plan_number)
    TextView tvNumber;
    @Bind(R.id.tv_rent_plan_apartment)
    TextView tvApartment;
    @Bind(R.id.lv_rent_plan_list)
    ListView mListView;
    @Bind(R.id.sl_rent_plan)
    StatusLayout mStatusLayout;
    BaseAdapter mAdapter;
    Context mContext;
    ArrayList<RentPlanItem> listData = new ArrayList<>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = this;
        tvNumber.setText(getIntent().getStringExtra(Constant.RENT_APARTMENT_CODE));
        tvApartment.setText(getIntent().getStringExtra(Constant.RENT_APARTMENT_NAME));
        getData();
    }

    private void getData() {
        String uid = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).getRentPlanItems(getIntent().getStringExtra(Constant.RENT_PROTOCOL_ID), uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<RentPlanItem>>>() {
                    @Override
                    public void onNext(BaseResponse<List<RentPlanItem>> listBaseResponse) {
                        setData(listBaseResponse.data);
                    }
                });
    }

    public void setData(List<RentPlanItem> data) {
        if (data == null || data.size() == 0) {
            mStatusLayout.showEmptyView("尚无计划");
            return;
        }
        listData.addAll(data);
        mStatusLayout.showContentView();
        mAdapter = new RentPlanListAdapter(mContext, listData);
        mListView.setAdapter(mAdapter);
        CommonUtils.setListViewHeightBasedOnChildren(mListView);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_rent_plan;
    }
}
