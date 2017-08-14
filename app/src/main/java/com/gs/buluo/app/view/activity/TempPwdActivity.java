package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/6/30.
 */

public class TempPwdActivity extends BaseActivity {
    @Bind(R.id.temp_pwd)
    TextView tempPwd;
    @Bind(R.id.temp_pwd_name)
    TextView tvName;
    @Bind(R.id.temp_pwd_number)
    TextView tvNum;

    @Override
    protected void bindView(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String name = intent.getStringExtra(Constant.DEPARTMENT_NAME);
        String number = intent.getStringExtra(Constant.DEPARTMENT_NUMBER);
        String sourceId = intent.getStringExtra(Constant.DEPARTMENT_ID);
        String sn = intent.getStringExtra(Constant.DEPARTMENT_SN);
        tvName.setText(name);
        tvNum.setText(number);
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).getTempPwd(TribeApplication.getInstance().getUserInfo().getId(),
                sourceId, sn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<String>>() {
                    @Override
                    public void onNext(BaseResponse<String> response) {
                        tempPwd.setText(response.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_temp_pwd;
    }
}
