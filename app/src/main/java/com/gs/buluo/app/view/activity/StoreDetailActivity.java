package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.StoreDetail;
import com.gs.buluo.app.model.CommunityModel;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/20.
 */
public class StoreDetailActivity extends BaseActivity implements Callback<BaseResponse<StoreDetail>>, View.OnClickListener {
    Context mCtx;
    TextView tvName;
    private TextView tvPrice;
    private TextView tvCollectNum;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvReason;
    private TextView tvMarkplace;
    private TextView tvDistance;
    private TextView tvBrand;
    private TextView tvTime;
    private TextView tvTopic;
    private Banner banner;
    private String id;
    private SimpleDraweeView logo;
    private LinearLayout facilitiesGroup;
    private HashMap<String,Integer> map=new HashMap<>();
    @Override
    protected void bindView(Bundle savedInstanceState) {
        id = getIntent().getStringExtra(Constant.STORE_ID);
        getDetailInfo(id);
        mCtx=this;
        setBarColor(R.color.transparent);
        initContentView();
        initMap();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_service_detail;
    }

    private void initMap() {
        map.put(getString(R.string.baby_chair),R.mipmap.baby_chair);
        map.put(getString(R.string.bar),R.mipmap.bar);
        map.put(getString(R.string.business_circle),R.mipmap.business_circle);
        map.put(getString(R.string.business_dinner),R.mipmap.business_dinner);
        map.put(getString(R.string.facilities_for_disabled),R.mipmap.facilities_for_disabled);
        map.put(getString(R.string.organic_food),R.mipmap.organic_food);
        map.put(getString(R.string.parking),R.mipmap.parking);
        map.put(getString(R.string.pet_ok),R.mipmap.pet_ok);
        map.put(getString(R.string.restaurants_of_hotel),R.mipmap.restaurants_of_hotel);
        map.put(getString(R.string.room),R.mipmap.room);
        map.put(getString(R.string.scene_seat),R.mipmap.scene_seat);
        map.put(getString(R.string.small_party),R.mipmap.small_party);
        map.put(getString(R.string.subway),R.mipmap.subway);
        map.put(getString(R.string.valet_parking),R.mipmap.valet_parking);
        map.put(getString(R.string.vip_rights),R.mipmap.vip_rights);
        map.put(getString(R.string.weekend_brunch),R.mipmap.weekend_brunch);
        map.put(getString(R.string.wi_fi),R.mipmap.wi_fi);
    }

    private void initContentView() {
        banner = (Banner) findViewById(R.id.server_detail_banner);
        tvName = (TextView)findViewById(R.id.server_detail_name);
        tvPrice =  (TextView)findViewById(R.id.server_detail_person_price);
        tvCollectNum = (TextView)findViewById(R.id.server_detail_collect);
        tvAddress = (TextView)findViewById(R.id.service_shop_address);
        tvPhone = (TextView)findViewById(R.id.service_shop_number);
        tvReason = (TextView)findViewById(R.id.server_detail_comment_reason);
        tvMarkplace = (TextView)findViewById(R.id.server_detail_markPlace);
        tvDistance = (TextView)findViewById(R.id.server_detail_distance);
        tvBrand = (TextView)findViewById(R.id.server_detail_category);
        tvTime = (TextView)findViewById(R.id.server_detail_work_time);
        tvTopic = (TextView)findViewById(R.id.server_detail_topic);
        logo= (SimpleDraweeView) findViewById(R.id.server_detail_logo);
        facilitiesGroup = (LinearLayout) findViewById(R.id.server_detail_facilities);

        findViewById(R.id.service_phone_call).setOnClickListener(this);
        findViewById(R.id.service_location).setOnClickListener(this);
        findViewById(R.id.service_call_server).setOnClickListener(this);
        findViewById(R.id.service_booking_food).setOnClickListener(this);
        findViewById(R.id.service_booking_seat).setOnClickListener(this);
        findViewById(R.id.server_detail_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.service_phone_call:
                intent.setAction(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvPhone.getText().toString());
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.service_location:
                intent.setClass(mCtx,MapActivity.class);
                startActivity(intent);
                break;
            case R.id.service_booking_food:
                ToastUtils.ToastMessage(mCtx,R.string.no_function);
                break;
            case R.id.service_booking_seat:
                if (!checkUser(mCtx))return;
                intent.setClass(mCtx, BookingServeActivity.class);
                intent.putExtra(Constant.SERVE_ID,id);
                startActivity(intent);
                break;
            case R.id.server_detail_back:
                finish();
                break;
            case R.id.service_call_server:
                intent.setAction(Intent.ACTION_DIAL);
                Uri data1 = Uri.parse("tel:" + "123456789");
                intent.setData(data1);
                startActivity(intent);
                break;
        }
    }

    private void getDetailInfo(String id) {
        showLoadingDialog();
        new CommunityModel().getStoreDetail(id,this);
    }

    @Override
    public void onResponse(Call<BaseResponse<StoreDetail>> call, Response<BaseResponse<StoreDetail>> response) {
        dismissDialog();
        if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
            StoreDetail data = response.body().data;
            setData(data);
        }
    }

    private void setData(StoreDetail data) {
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setImageLoader(new FrescoImageLoader());
        banner.isAutoPlay(false);
        banner.setImages(data.pictures);
        banner.start();
        tvName.setText(data.name);
        tvPhone.setText(data.phone);
        tvAddress.setText(data.address);
        tvCollectNum.setText(data.collectNum + "");
        tvMarkplace.setText(data.markPlace);
//        tvPrice.setText(data.personExpense);
//        tvReason.setText(data.recommendedReason);
        tvBrand.setText(data.brand);
        tvTime.setText("每天 " + data.businessHours);
//        tvTopic.setText(data.topics);
        setFacilities(data.faclities);
        FresoUtils.loadImage(data.logo, logo);
    }
    public void setFacilities(List<String> faclities) {
        for (String facility:faclities){
            View facilityView=View.inflate(this,R.layout.serve_detail_facility,null);
            ImageView iv= (ImageView) facilityView.findViewById(R.id.facility_image);
            TextView  tv= (TextView) facilityView.findViewById(R.id.facility_text);
            tv.setText(facility);
            iv.setImageResource(map.get(facility));
            facilitiesGroup.addView(facilityView);
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<StoreDetail>> call, Throwable t) {

    }
}
