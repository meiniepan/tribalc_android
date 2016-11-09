package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.login_send_verify)
    TextView mCode;
    @Bind(R.id.login_username)
    EditText et_phone;
    @Bind(R.id.login_verify)
    EditText et_verify;
    private String TAG="LoginActivity";

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.login_back).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_back:
                finish();
                break;
            case R.id.login:
                RequestParams params=new RequestParams(Constant.BASE_URL+"persons/login");
                params.setHeader("Content-Type","application/json");
                params.setHeader("Accept","application/json");
                params.setAsJsonContent(true);
                params.addParameter("phone",et_phone.getText().toString().trim());
                params.addParameter("verification",et_verify.getText().toString().trim());
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "onSuccess: "+result);
                    }
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e(TAG, "onError: "+ex.toString());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
                break;
        }
    }
}
