package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.StoreTagsAdapter;
import com.gs.buluo.app.bean.Privilege;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.AutoLineFeedLayoutManager;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/19.
 */

public class StoreInfoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.recycle_store_tag)
    RecyclerView mRecyclerView;
    @Bind(R.id.store_info_head)
    Banner mBanner;
    @Bind(R.id.tv_store_name)
    TextView tvName;
    @Bind(R.id.tv_store_category)
    TextView tvCategory;
    @Bind(R.id.tv_store_mark_place)
    TextView tvPlace;
    @Bind(R.id.tv_store_desc)
    TextView tvDesc;
    @Bind(R.id.tv_store_address)
    TextView tvAddress;
    @Bind(R.id.tv_store_phone)
    TextView tvPhone;
    @Bind(R.id.tv_store_time)
    TextView tvTime;
    @Bind(R.id.tv_store_average)
    TextView tvAverage;
    @Bind(R.id.tv_store_discount)
    TextView tvDiscount;
    @Bind(R.id.buy)
    Button buy;
    @Bind(R.id.store_info_status)
    StatusLayout mStatusLayout;
    @Bind(R.id.back2)
    View back2;
    private String storeId;
    private Context mCtx;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_info;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = getCtx();
        storeId = getIntent().getStringExtra(Constant.STORE_ID);
        getData();
        mBanner.setImageLoader(new FrescoImageLoader(false));
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        buy.setOnClickListener(this);
        back2.setOnClickListener(this);
        AutoLineFeedLayoutManager layout = new AutoLineFeedLayoutManager();
        layout.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layout);

    }

    private void getData() {
        LoadingDialog.getInstance().show(getCtx(), "", true);
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreInfo(storeId, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreInfo>>() {
                    @Override
                    public void onNext(BaseResponse<StoreInfo> response) {
                        mStatusLayout.showContentView();
                        back2.setVisibility(View.GONE);
                        buy.setEnabled(true);
                        setData(response.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        buy.setEnabled(false);
                        mStatusLayout.showErrorView("获取商户信息失败!");
                    }
                });
    }

    private void setData(StoreInfo data) {
        setBanner(data.pictures);
        tvName.setText(data.name);
        tvCategory.setText(data.category);
        tvPlace.setText(data.markPlace);
        tvDesc.setText(data.desc);
        tvAddress.setText(data.address);
        tvPhone.setText(data.phone);
        tvTime.setText(data.businessHours);
        tvAverage.setText(data.avgprice);
        setDiscount(data.privileges);
        StoreTagsAdapter adapter = new StoreTagsAdapter(getCtx(), Arrays.asList(data.sellingPoint));
        mRecyclerView.setAdapter(adapter);

    }

    private void setDiscount(ArrayList<Privilege> privileges) {
        StringBuffer value = new StringBuffer();
        for (int i = 0; i < privileges.size(); i++) {
            if (i != 0)
                value.append(" ");
            Privilege privilege = privileges.get(i);
            switch (privilege.type) {
                case DISCOUNT:
                    value.append(privilege.value.floatValue() * 10).append("折");
                    break;
                case REDUCE:
                    value.append("满").append(privilege.condition).append("减").append(privilege.value).append("元");
                    break;
                case ALIQUOT:
                    value.append("每满").append(privilege.condition).append("减").append(privilege.value).append("元");
                    break;
            }
        }
        tvDiscount.setText(value);
    }

    private void setBanner(String[] pictures) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(pictures).subList(1, pictures.length));
        mBanner.setImages(list);
        mBanner.setDelayTime(3000);
        mBanner.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy:
                Intent intent = new Intent(getCtx(), PayBillActivity.class);
                intent.putExtra(Constant.STORE_ID, storeId);
                startActivity(intent);
                break;
            case R.id.back2:
                finish();
                break;
        }
    }
}
