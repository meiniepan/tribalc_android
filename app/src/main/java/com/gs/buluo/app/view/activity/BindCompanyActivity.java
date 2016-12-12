package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CompanyPlate;
import com.gs.buluo.app.bean.RequestBodyBean.BindCompanyRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CompanyResponse;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.network.CompanyService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.BindCompanyPresenter;
import com.gs.buluo.app.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.bind_company).setOnClickListener(this);
        findViewById(R.id.bind_company_back).setOnClickListener(this);
        mCompanyName.setOnClickListener(this);
        EventBus.getDefault().register(this);
        mUserInfo = TribeApplication.getInstance().getUserInfo();
//        UserSensitiveDao dao = new UserSensitiveDao();
//        UserSensitiveEntity entity = dao.find(id);
//        String name = entity.getName();
        String name = TribeApplication.getInstance().getUserInfo().getNickname();
        mUsername.setText(name);
        mContext = this;
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
    protected BasePresenter getPresenter() {
        return new BindCompanyPresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_company;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind_company:
                if (mCompanyPlate==null) {
                    ToastUtils.ToastMessage(mContext,"请选择公司");
                }else {
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
                                        Intent intent = new Intent(mContext, BindCompanySuccessfulActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 507:
                                        ToastUtils.ToastMessage(mContext,"存储失败");
                                        break;
                                    case 409:
                                        ToastUtils.ToastMessage(mContext,"已存在,请求冲突");
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
