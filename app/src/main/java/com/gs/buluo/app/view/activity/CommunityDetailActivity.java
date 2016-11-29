package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CommunityDetailStoreAdapter;
import com.gs.buluo.app.bean.CommunityDetail;
import com.gs.buluo.app.bean.ResponseBody.CommunityDetailResponse;
import com.gs.buluo.app.model.CommunityModel;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityDetailActivity extends BaseActivity implements View.OnClickListener, Callback<CommunityDetailResponse> {
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

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx=this;
        String id = getIntent().getStringExtra(Constant.COMMUNITY_ID);
        new CommunityModel().getCommunityDetail(id,this);
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
        switch (v.getId()){
            case R.id.community_detail_order:
                intent.setClass(mCtx,CommunityVisitActivity.class);
                startActivity(intent);
                break;
            case R.id.community_detail_back:
                finish();
                break;
        }
    }


    @Override
    public void onResponse(Call<CommunityDetailResponse> call, Response<CommunityDetailResponse> response) {
        if (response.body()!=null&&response.body().code==200){
            CommunityDetail communityDetail = response.body().data;
            banner.setImages(communityDetail.pictures);
            banner.setImageLoader(new FrescoImageLoader());
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.isAutoPlay(false);
            banner.start();
            if (communityDetail.repastList==null||communityDetail.entertainmentList==null)return;
            lvFood.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,communityDetail.repastList.size()* DensityUtils.dip2px(mCtx,82)));
            lvFun.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,communityDetail.entertainmentList.size()* DensityUtils.dip2px(mCtx,82)));
            setData(communityDetail);
        }
    }

    @Override
    public void onFailure(Call<CommunityDetailResponse> call, Throwable t) {
        ToastUtils.ToastMessage(mCtx,R.string.connect_fail);
    }

    public void setData(CommunityDetail data) {
        tvAddress.setText(data.address);
        tvAddressMap.setText(data.address);
        tvDesc.setText(data.desc);
        tvName.setText(data.name);
        FresoUtils.loadImage(Constant.BASE_IMG_URL+data.map,map);
        CommunityDetailStoreAdapter adapter=new CommunityDetailStoreAdapter(mCtx,data.repastList);
        lvFood.setAdapter(adapter);
        CommunityDetailStoreAdapter adapter1=new CommunityDetailStoreAdapter(mCtx,data.entertainmentList);
        lvFun.setAdapter(adapter1);
    }
}
