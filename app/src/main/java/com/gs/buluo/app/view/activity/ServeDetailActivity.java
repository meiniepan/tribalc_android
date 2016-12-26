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
import com.gs.buluo.app.bean.DetailStoreSetMeal;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.model.ServeModel;
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
 * Created by hjn on 2016/11/24.
 */
public class ServeDetailActivity extends BaseActivity implements View.OnClickListener, Callback<BaseCodeResponse<DetailStoreSetMeal>> {
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
        id = getIntent().getStringExtra(Constant.SERVE_ID);
        getDetailInfo(id);
        mCtx = this;
        setBarColor(R.color.transparent);
        initMap();
        initContentView();
    }
    private void initContentView() {
        banner = (Banner) findViewById(R.id.server_detail_banner);
        tvName = (TextView) findViewById(R.id.server_detail_name);
        tvPrice = (TextView) findViewById(R.id.server_detail_person_price);
        tvCollectNum = (TextView) findViewById(R.id.server_detail_collect);
        tvAddress = (TextView) findViewById(R.id.service_shop_address);
        tvPhone = (TextView) findViewById(R.id.service_shop_number);
        tvReason = (TextView) findViewById(R.id.server_detail_comment_reason);
        tvMarkplace = (TextView) findViewById(R.id.server_detail_markPlace);
        tvDistance = (TextView) findViewById(R.id.server_detail_distance);
        tvBrand = (TextView) findViewById(R.id.server_detail_category);
        tvTime = (TextView) findViewById(R.id.server_detail_work_time);
        tvTopic = (TextView) findViewById(R.id.server_detail_topic);
        logo = (SimpleDraweeView) findViewById(R.id.server_detail_logo);
        facilitiesGroup = (LinearLayout) findViewById(R.id.server_detail_facilities);

        findViewById(R.id.service_phone_call).setOnClickListener(this);
        findViewById(R.id.service_location).setOnClickListener(this);
        findViewById(R.id.service_call_server).setOnClickListener(this);
        findViewById(R.id.service_booking_food).setOnClickListener(this);
        findViewById(R.id.service_booking_seat).setOnClickListener(this);
        findViewById(R.id.server_detail_back).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_service_detail;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.service_phone_call:
                intent.setAction(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvPhone.getText().toString());
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.service_location:
                intent.setClass(mCtx, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.service_booking_food:
                ToastUtils.ToastMessage(mCtx,R.string.no_function);
                break;
            case R.id.service_booking_seat:
                if (!checkUser(mCtx))return;
                intent.setClass(mCtx, BookingServeActivity.class);
                intent.putExtra(Constant.SERVE_ID, id);
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
        new ServeModel().getServeDetail(id, this);
    }

    @Override
    public void onResponse(Call<BaseCodeResponse<DetailStoreSetMeal>> call, Response<BaseCodeResponse<DetailStoreSetMeal>> response) {
        dismissDialog();
        if (response.body() != null && response.body().code == 200 && response.body().data != null) {
            DetailStoreSetMeal data = response.body().data;
            setData(data);
        }
    }

    private void setData(DetailStoreSetMeal data) {
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setImageLoader(new FrescoImageLoader());
        banner.isAutoPlay(false);
        banner.setImages(data.pictures);
        banner.start();
        tvName.setText(data.name);
        tvPhone.setText(data.detailStore.phone);
        tvAddress.setText(data.detailStore.address);
        tvCollectNum.setText(data.detailStore.collectionNum + "");
        tvMarkplace.setText(data.detailStore.markPlace);
        tvPrice.setText(data.personExpense);
        tvReason.setText(data.recommendedReason);
        tvBrand.setText(data.detailStore.brand);
        tvTime.setText("每天 " + data.detailStore.businessHours);
        tvTopic.setText(data.topics);
        setFacilities(data.detailStore.faclities);
        FresoUtils.loadImage(data.mainPicture, logo); //TODO  LOGO
    }

    @Override
    public void onFailure(Call<BaseCodeResponse<DetailStoreSetMeal>> call, Throwable t) {
        dismissDialog();
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }


    private void initMap() {
        map.put("baby_chair",R.mipmap.baby_chair);
        map.put("bar",R.mipmap.bar);
        map.put("business_circle",R.mipmap.business_circle);
        map.put("business_dinner",R.mipmap.business_dinner);
        map.put("facilities_for_disabled",R.mipmap.facilities_for_disabled);
        map.put("organic_food",R.mipmap.organic_food);
        map.put("parking",R.mipmap.parking);
        map.put("pet_ok",R.mipmap.pet_ok);
        map.put("restaurants_of_hotel",R.mipmap.restaurants_of_hotel);
        map.put("room",R.mipmap.room);
        map.put("scene_seat",R.mipmap.scene_seat);
        map.put("small_party",R.mipmap.small_party);
        map.put("subway",R.mipmap.subway);
        map.put("valet_parking",R.mipmap.valet_parking);
        map.put("vip_rights",R.mipmap.vip_rights);
        map.put("weekend_brunch",R.mipmap.weekend_brunch);
        map.put("wi-fi",R.mipmap.wi_fi);
    }

    public void setFacilities(List<String> faclities) {
        for (String facility:faclities){
            View facilityView=View.inflate(this,R.layout.serve_detail_facility,null);
            ImageView iv= (ImageView) facilityView.findViewById(R.id.facility_image);
            TextView  tv= (TextView) facilityView.findViewById(R.id.facility_text);
            tv.setText(getFacilityText(facility));
            iv.setImageResource(map.get(facility));
            facilitiesGroup.addView(facilityView);
        }
    }

    public int getFacilityText(String facility) {
        switch (facility){
            case "wi-fi":
                return R.string.wi_fi;
            case "baby_chair":
                return R.string.baby_chair;
            case "bar":
                return R.string.bar;
            case "business_circle":
                return R.string.business_circle;
            case "business_dinner":
                return R.string.business_dinner;
            case "facilities_for_disabled":
                return R.string.facilities_for_disabled;
            case "organic_food":
                return R.string.organic_food;
            case "parking":
                return R.string.parking;
            case "pet_ok":
                return R.string.pet_ok;
            case "room":
                return R.string.room;
            case "restaurants_of_hotel":
                return R.string.restaurants_of_hotel;
            case "scene_seat":
                return R.string.scene_seat;
            case "small_party":
                return R.string.small_party;
            case "subway":
                return R.string.subway;
            case "valet_parking":
                return R.string.valet_parking;
            case "vip_rights":
                return R.string.vip_rights;
            case "weekend_brunch":
                return R.string.weekend_brunch;
        }
        return 0;
    }
}
