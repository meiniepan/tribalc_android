package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CompanyDetail;
import com.gs.buluo.app.bean.CompanyInfo;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import butterknife.Bind;

public class CompanyDetailActivity extends BaseActivity {
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
    @Bind(R.id.company_detail_logo)
    public SimpleDraweeView mLogo;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.company_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CompanyDetail mDetail = getIntent().getParcelableExtra(Constant.ForIntent.COMPANY_FLAG);
        setData(mDetail);
    }

    private void setData(CompanyDetail mDetail ) {
        CompanyInfo company = mDetail.company;
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.isAutoPlay(false);
        mBanner.setImages(company.getPictures());
        mBanner.start();

        FresoUtils.loadImage(mDetail.company.getLogo(),mLogo);
        mCompanyName.setText(company.getName());
        mCompanyDesc.setText(company.getDesc());
        mCompanyInfoName.setText(company.getName());

        UserSensitiveEntity entity = new UserSensitiveDao().findFirst();
        mCompanyUsername.setText(entity.getName());
        mDepartment.setText(mDetail.department);
        mPosition.setText(mDetail.position);
        mPersonNum.setText(mDetail.personNum);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_detail;
    }
}
