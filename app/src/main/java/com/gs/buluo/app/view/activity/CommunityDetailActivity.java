package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CommunityDetailStoreAdapter;
import com.gs.buluo.app.bean.CommunityDetail;
import com.gs.buluo.app.network.CommunityApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityDetailActivity extends BaseActivity implements View.OnClickListener {
    Context mCtx;
    private Banner banner;

    @Bind(R.id.community_detail_list1)
    ListView lvFood;
    @Bind(R.id.community_detail_list2)
    ListView lvFun;

    @Bind(R.id.community_detail_address)
    TextView tvAddress;
    @Bind(R.id.community_detail_address_map)
    TextView tvAddressMap;
    @Bind(R.id.community_detail_description)
    TextView tvDesc;
    @Bind(R.id.community_detail_map)
    SimpleDraweeView map;
    @Bind(R.id.community_detail_name)
    TextView tvName;
    private String name;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        String id = getIntent().getStringExtra(Constant.COMMUNITY_ID);

        TribeRetrofit.getInstance().createApi(CommunityApis.class).
                getCommunityDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CommunityDetail>>() {
                    @Override
                    public void onNext(BaseResponse<CommunityDetail> response) {
                        CommunityDetail communityDetail = response.data;
                        banner.setImages(communityDetail.pictures==null ? new ArrayList<>():communityDetail.pictures);
                        banner.setImageLoader(new FrescoImageLoader());
                        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                        banner.isAutoPlay(false);
                        banner.start();
                        setData(communityDetail);
                    }
                });


        banner = (Banner) findViewById(R.id.community_detail_banner);
        findViewById(R.id.community_detail_order).setOnClickListener(this);
        findViewById(R.id.community_detail_back).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_community_detail;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.community_detail_order:
                if (!checkUser(CommunityDetailActivity.this)) return;
                intent.setClass(mCtx, CommunityVisitActivity.class);
                intent.putExtra(Constant.COMMUNITY_NAME, name);
                startActivity(intent);
                break;
            case R.id.community_detail_back:
                finish();
                break;
        }
    }

    public void setData(final CommunityDetail data) {
        tvAddress.setText(data.address);
        tvAddressMap.setText(data.address);
        tvDesc.setText(data.desc);
        name = data.name;
        tvName.setText(name);
        FresoUtils.loadImage(data.map, map);
        if (data.repastList != null || data.entertainmentList != null) {
            CommunityDetailStoreAdapter adapter = new CommunityDetailStoreAdapter(mCtx, data.repastList);
            lvFood.setAdapter(adapter);
            CommunityDetailStoreAdapter adapter1 = new CommunityDetailStoreAdapter(mCtx, data.entertainmentList);
            lvFun.setAdapter(adapter1);
            CommonUtils.setListViewHeightBasedOnChildren(lvFood);
            CommonUtils.setListViewHeightBasedOnChildren(lvFun);
        }

        lvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CommunityDetailActivity.this, StoreDetailActivity.class);
                intent.putExtra(Constant.STORE_ID, data.repastList.get(position).id);
                startActivity(intent);
            }
        });
        lvFun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CommunityDetailActivity.this, StoreDetailActivity.class);
                intent.putExtra(Constant.STORE_ID, data.entertainmentList.get(position).id);
                startActivity(intent);
            }
        });
    }
}
