package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CompanyDetail;
import com.gs.buluo.app.bean.CompanyInfo;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import butterknife.Bind;

public class CompanyDetailActivity extends BaseActivity {

    private static final String TAG = "CompanyDetailActivity";
    private CompanyDetail mDetail;
    @Bind(R.id.company_detail_banner)
    public Banner mBanner;
    @Bind(R.id.company_detail_name)
    public TextView mCompanyName;
    @Bind(R.id.company_detail_desc)
    public TextView mCompanyDesc;
    @Bind(R.id.company_detail_name1)
    public TextView mCompanyInfoName;
    @Bind(R.id.company_detail_username)
    public TextView mCompanyUsername;
    @Bind(R.id.company_detail_department)
    public TextView mDepartment;
    @Bind(R.id.company_detail_position)
    public TextView mPosition;
    @Bind(R.id.company_detail_personNum)
    public TextView mPersonNum;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        mDetail = (CompanyDetail) getIntent().getSerializableExtra(Constant.COMPANY_FLAG);
        setData();

    }

    private void setData() {
        CompanyInfo company = mDetail.company;
        Log.d(TAG, "setData: "+company);
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.isAutoPlay(false);
        mBanner.setImages(company.getPictures());
        mBanner.start();

        mCompanyName.setText(company.getName());
        mCompanyDesc.setText(company.getDesc());
        mCompanyInfoName.setText(company.getName());
        mCompanyUsername.setText(TribeApplication.getInstance().getUserInfo().getNickname());
        mDepartment.setText(mDetail.department);
        mPosition.setText(mDetail.position);
        mPersonNum.setText(mDetail.personNum);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_detail;
    }
}
