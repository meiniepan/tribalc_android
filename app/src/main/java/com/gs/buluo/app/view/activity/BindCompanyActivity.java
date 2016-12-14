package com.gs.buluo.app.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CompanyPlate;
import com.gs.buluo.app.bean.RequestBodyBean.BindCompanyRequestBody;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.network.CompanyService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BindCompanyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_company_name)
    TextView mCompanyName;
    @Bind(R.id.et_user_name)
    EditText mUsername;
    @Bind(R.id.et_part_name)
    EditText mPartName;
    @Bind(R.id.et_position_name)
    EditText mPositionName;
    @Bind(R.id.et_work_number)
    EditText mWorkNumber;
    private UserInfoEntity mUserInfo;
    private CompanyPlate mCompanyPlate;
    private Context mContext;
    private static final String TAG = "BindCompanyActivity";

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.bind_company).setOnClickListener(this);
        findViewById(R.id.bind_company_back).setOnClickListener(this);
        mCompanyName.setOnClickListener(this);
        EventBus.getDefault().register(this);
        mUserInfo = TribeApplication.getInstance().getUserInfo();
        mContext = this;

        checkIsVerify();
    }

    private void checkIsVerify() {
//        String id = mUserInfo.getId();
        UserSensitiveDao dao = new UserSensitiveDao();
        UserSensitiveEntity entity = dao.findFirst();
        String name = entity.getName();

        if (TextUtils.isEmpty(name)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        }else {
            mUsername.setText(name);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveCompanyPanel(CompanyPlate companyPlate) {
        mCompanyPlate = companyPlate;
        mCompanyName.setText(companyPlate.name);

    }



    private boolean checkTextIsEmpty(String conpanyName, String username, String partname, String position, String number) {
        if (TextUtils.isEmpty(conpanyName) || TextUtils.isEmpty(username) || TextUtils.isEmpty(partname) || TextUtils.isEmpty(position) || TextUtils.isEmpty(number)) {
            ToastUtils.ToastMessage(this, getString(R.string.not_empty));
            return true;
        }
        return false;
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
                    String conpanyName = mCompanyName.getText().toString().trim();
                    String username = mUsername.getText().toString().trim();
                    String partname = mPartName.getText().toString().trim();
                    String position = mPositionName.getText().toString().trim();
                    String number = mWorkNumber.getText().toString().trim();


                    if (!checkTextIsEmpty(conpanyName, username, partname, position, number)) {
                        BindCompanyRequestBody requestBody = new BindCompanyRequestBody();

                        requestBody.setCompanyId(mCompanyPlate.id);
                        requestBody.setDepartment(partname);
                        requestBody.setPosition(position);
                        requestBody.setPersonNum(number);

                        TribeRetrofit.getIntance().createApi(CompanyService.class).bindCompany(mUserInfo.getId(), requestBody).enqueue(new Callback<SimpleCodeResponse>() {
                            @Override
                            public void onResponse(Call<SimpleCodeResponse> call, Response<SimpleCodeResponse> response) {
                                switch (response.body().code) {
                                    case 201:
                                        startActivity(new Intent(mContext,BindCompanyProcessingActivity.class));
                                        finish();
                                        break;
                                    case 507:
                                        ToastUtils.ToastMessage(mContext, "存储失败");
                                        break;
                                    case 409:
                                        ToastUtils.ToastMessage(mContext, "已存在,请求冲突");
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<SimpleCodeResponse> call, Throwable t) {
                                ToastUtils.ToastMessage(mContext, "请求绑定失败");
                            }
                        });
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


}
