package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.DetailStoreSetMeal;
import com.gs.buluo.app.bean.ResponseBody.ServeDetailResponse;
import com.gs.buluo.app.model.ServeModel;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.pulltozoom.PullToZoomScrollViewEx;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/24.
 */
public class ServiceDetailActivity extends BaseActivity implements View.OnClickListener, Callback<ServeDetailResponse> {
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

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String id = getIntent().getStringExtra(Constant.SERVE_ID);
        getDetailInfo(id);
        mCtx=this;
        setBarColor(R.color.transparent);
        initContentView();
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

                break;
            case R.id.service_booking_seat:

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
        new ServeModel().getServeDetail(id,this);
    }

    @Override
    public void onResponse(Call<ServeDetailResponse> call, Response<ServeDetailResponse> response) {
        dismissDialog();
        if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
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
        tvCollectNum.setText(data.detailStore.collectionNum+"");
        tvMarkplace.setText(data.detailStore.markPlace);
        tvPrice.setText(data.personExpense);
        tvReason.setText(data.recommendedReason);
        tvBrand.setText(data.detailStore.brand);
        tvTime.setText(data.detailStore.businessHours);
        tvTopic.setText(data.topics);
    }

    @Override
    public void onFailure(Call<ServeDetailResponse> call, Throwable t) {
        dismissDialog();
        ToastUtils.ToastMessage(this,R.string.connect_fail);
    }
}
