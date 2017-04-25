package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CompanyPlate;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.network.CompanyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.CustomAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BindCompanyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_company_name)
    TextView mCompanyName;
    @Bind(R.id.et_user_name)
    EditText mUsername;
    @Bind(R.id.et_part_name)
    EditText mPartName;
    @Bind(R.id.et_position_name)
    EditText mPositionName;
    private CompanyPlate mCompanyPlate;
    private Context mContext;
    private UserInfoEntity entity;
    private UserInfoDao dao;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.bind_company).setOnClickListener(this);
        findViewById(R.id.bind_company_back).setOnClickListener(this);
        mCompanyName.setOnClickListener(this);
        EventBus.getDefault().register(this);
        mContext = this;
        checkIsVerify();
    }

    private void checkIsVerify() {
        dao = new UserInfoDao();
        entity = dao.findFirst();
        String name = entity.getName();
        if (TextUtils.isEmpty(name)) {
            CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(this);
            builder.setTitle("您好").setMessage("请先进行个人实名认证")
                    .setPositiveButton("去认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(new Intent(mContext, VerifyActivity.class));
                        }
                    });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } else {
            mUsername.setText(name);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveCompanyPanel(CompanyPlate companyPlate) {
        mCompanyPlate = companyPlate;
        mCompanyName.setText(companyPlate.companyName);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_company;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind_company:
                if (mCompanyPlate == null) {
                    ToastUtils.ToastMessage(mContext, "请选择公司");
                } else {
                    String companyName = mCompanyName.getText().toString().trim();
                    if (!TextUtils.isEmpty(companyName)) {
                        bindCompany(mCompanyPlate.id);
                    }
                }
                break;
            case R.id.bind_company_back:
                finish();
                break;
            case R.id.tv_company_name:
                startActivity(new Intent(this, PickCommunityActivity.class));
                break;
        }
    }

    public void bindCompany(String id) {
        TribeRetrofit.getInstance().createApi(CompanyApis.class).bindCompany(TribeApplication.getInstance().getUserInfo().getId(),
                new ValueRequestBody(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfoEntity>>(false) {
                    @Override
                    public void onNext(BaseResponse<UserInfoEntity> response) {
                        ToastUtils.ToastMessage(mContext, "绑定成功");
                        entity.setCompanyID(response.data.getCompanyID());
                        entity.setCompanyName(response.data.getCompanyName());
                        dao.update(entity);
                        finish();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (e.getCode() == 400 || e.getCode() == 404) {
                            ToastUtils.ToastMessage(mContext, "公司未录入信息");
                        }else if (e.getCode()==409){
                            ToastUtils.ToastMessage(mContext, "公司未授权");
                        }else {
                            ToastUtils.ToastMessage(mContext, "绑定失败"+e.getCode());
                        }
                    }
                });
    }
}
