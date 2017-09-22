package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.StoreInfoGoodsListAdapter;
import com.gs.buluo.app.adapter.StoreTagsAdapter;
import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.bean.Privilege;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.network.GoodsApis;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.AutoLineFeedLayoutManager;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.StatusLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Bind(R.id.list_goods)
    RecyclerView goodsList;
    @Bind(R.id.tv_more_goods)
    TextView tvMoreGoods;
    private String storeId;
    private String storeName;
    private Context mCtx;
    private StoreInfoGoodsListAdapter mAdapter;
    private ArrayList<ListGoods> list;

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
        tvMoreGoods.setOnClickListener(this);
        buy.setOnClickListener(this);
        AutoLineFeedLayoutManager layout = new AutoLineFeedLayoutManager();
        layout.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layout);
        list = new ArrayList<>();
        mAdapter = new StoreInfoGoodsListAdapter(R.layout.store_goods_list_item, list);
        goodsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        goodsList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mCtx, GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_ID, ((ListGoods) adapter.getData().get(position)).id);
                intent.putExtra(Constant.GOODS_PIC, ((ListGoods) adapter.getData().get(position)).mainPicture);
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        StoreInfoActivity.this,
                        new Pair<>(view.findViewById(R.id.goods_list_picture),
                                Constant.DETAIL_HEADER_IMAGE));
                ActivityCompat.startActivity(mCtx, intent, activityOptions.toBundle());
            }
        });
        getGoodsData();
    }

    private void getGoodsData() {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsOfStoreFirst(storeId, 3 + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodList>>() {
                    @Override
                    public void onNext(BaseResponse<GoodList> response) {
                        mAdapter.setNewData(response.data.content);
                        if (response.data.hasMore)
                            tvMoreGoods.setVisibility(View.VISIBLE);
                        else tvMoreGoods.setVisibility(View.GONE);
                    }
                });
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
                        buy.setEnabled(true);
                        setData(response.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        buy.setEnabled(false);
                        mStatusLayout.showErrorView("获取商户信息失败!");
                    }
                });
    }

    private void setData(StoreInfo data) {
        setBanner(data.pictures);
        storeName = data.name;
        tvName.setText(data.name);
        tvCategory.setText(data.category);
        tvPlace.setText(data.markPlace);
        tvDesc.setText(data.desc);
        tvAddress.setText(data.address);
        tvPhone.setText(data.serviceLine);
        tvTime.setText(data.businessHours);
        tvAverage.setText(data.avgprice);
        if (data.privileges != null) setDiscount(data.privileges);

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
        List<String> list = Arrays.asList(pictures);
        mBanner.setImages(list);
        mBanner.setDelayTime(3000);
        mBanner.start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.buy:
                intent.setClass(getCtx(), PayBillActivity.class);
                intent.putExtra(Constant.STORE_ID, storeId);
                startActivity(intent);
                break;
            case R.id.tv_more_goods:
                intent.setClass(getCtx(), GoodsOfStoreActivity.class);
                intent.putExtra(Constant.STORE_ID, storeId);
                intent.putExtra(Constant.STORE_NAME, storeName);
                startActivity(intent);
                break;
        }
    }
}
