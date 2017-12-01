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
import com.gs.buluo.app.network.CompanyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;

import java.util.HashSet;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CompanyDetailActivity extends BaseActivity {
    @BindView(R.id.company_logo)
    SimpleDraweeView companyLogo;
    @BindView(R.id.company_name)
    TextView companyName;
    @BindView(R.id.company_name_2)
    TextView companyName2;
    @BindView(R.id.company_employee)
    TextView companyEmployee;
    @BindView(R.id.company_department)
    TextView companyDepartment;
    @BindView(R.id.company_position)
    TextView companyPosition;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.custom_blue2);
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(CompanyApis.class).queryCompany(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CompanyDetail>>() {
                    @Override
                    public void onFail(ApiException e) {
                        if (e.getCode() == ResponseCode.WRONG_PARAMETER || e.getCode() == ResponseCode.USER_NOT_FOUND) {
                            ToastUtils.ToastMessage(getCtx(), "公司无此员工信息");
                        } else {
                            ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<CompanyDetail> response) {
                        setData(response.data);
                    }
                });
    }

    private void setData(CompanyDetail mDetail) {
        CompanyInfo company = mDetail.company;
        FresoUtils.loadImage(mDetail.company.getLogo(), companyLogo);
        companyName.setText(company.getName());
        companyName2.setText(company.getName());
        companyDepartment.setText(mDetail.department);
        companyPosition.setText(mDetail.position);

        UserInfoEntity entity = TribeApplication.getInstance().getUserInfo();
        companyEmployee.setText(entity.getName());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_detail;
    }
}
