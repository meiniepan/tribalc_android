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

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.pulltozoom.PullToZoomScrollViewEx;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/24.
 */
public class ServiceDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.service_zoom_parent)
    PullToZoomScrollViewEx scrollViewEx;

    Context mCtx;
    TextView shopNumber;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx=this;
        View zoomView = LayoutInflater.from(this).inflate(R.layout.service_zoom_layout, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.service_content_layout, null, false);
        initContentView(contentView);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (12.0F * (mScreenWidth / 16.0F)));
        scrollViewEx.setHeaderLayoutParams(localObject);
        scrollViewEx.setZoomView(zoomView);
        scrollViewEx.setScrollContentView(contentView);
    }

    private void initContentView(View contentView) {
        shopNumber = (TextView) contentView.findViewById(R.id.service_shop_number);
        contentView.findViewById(R.id.service_phone_call).setOnClickListener(this);
        contentView.findViewById(R.id.service_location).setOnClickListener(this);
        contentView.findViewById(R.id.service_call_server).setOnClickListener(this);
        contentView.findViewById(R.id.service_booking_food).setOnClickListener(this);
        contentView.findViewById(R.id.service_booking_seat).setOnClickListener(this);
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
                Uri data = Uri.parse("tel:" + shopNumber.getText().toString());
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
            case R.id.service_call_server:
                intent.setAction(Intent.ACTION_DIAL);
                Uri data1 = Uri.parse("tel:" + "123456789");
                intent.setData(data1);
                startActivity(intent);
                break;
        }
    }
}
