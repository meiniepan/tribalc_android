package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CompanyDetail;
import com.gs.buluo.app.bean.CompanyInfo;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.network.CompanyApis;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.network.BaseResponse;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import retrofit2.Response;

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
    @Bind(R.id.company_detail_logo)
    public SimpleDraweeView mLogo;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(CompanyApis.class).queryCompany(TribeApplication.getInstance().getUserInfo().getId())
                .enqueue(new TribeCallback<CompanyDetail>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CompanyDetail>> response) {
                        dismissDialog();
                        CompanyDetail detail = response.body().data;
                        setData(detail);
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<CompanyDetail> body) {
                        dismissDialog();
                        if (responseCode== ResponseCode.WRONG_PARAMETER ||responseCode== ResponseCode.USER_NOT_FOUND ){
                            ToastUtils.ToastMessage(getCtx(),"公司无此员工信息");
                        }else {
                            ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
                        }
                    }
                });
    }

    private void setData(CompanyDetail mDetail ) {
        CompanyInfo company = mDetail.company;
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.isAutoPlay(false);
        mBanner.setImages(company.getPictures()==null? new ArrayList<>(): company.getPictures());
        mBanner.start();

        FresoUtils.loadImage(mDetail.company.getLogo(),mLogo);
        mCompanyName.setText(company.getName());
        mCompanyDesc.setText(company.getDesc());
        mCompanyInfoName.setText(company.getName());

        UserInfoEntity entity = new UserInfoDao().findFirst();
        mCompanyUsername.setText(entity.getName());
        mDepartment.setText(mDetail.department);
        mPosition.setText(mDetail.position);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_detail;
    }
}
