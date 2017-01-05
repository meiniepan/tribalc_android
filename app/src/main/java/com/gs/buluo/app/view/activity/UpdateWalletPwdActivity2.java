package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;
import com.gs.buluo.app.network.MoneyService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.PwdEditText;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/21.
 */
public class UpdateWalletPwdActivity2 extends BaseActivity {

   @Bind(R.id.update_pwd_sign)
    TextView mText;
    @Bind(R.id.pwd_title)
    TextView title;
    @Bind(R.id.wallet_pwd_1)
    PwdEditText editText;

    String mPwd;
    private String firstPwd;
    Context mCtx;
    private String oldPwd;
    private String vCode;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        firstPwd = getIntent().getStringExtra(Constant.WALLET_PWD);
        oldPwd = getIntent().getStringExtra(Constant.OLD_PWD);
        vCode = getIntent().getStringExtra(Constant.VCODE);
        mText.setText(R.string.re_input_new_pwd);
        title.setText(R.string.pay_pwd);
        mCtx=this;
        editText.requestFocus();
        editText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                mPwd=editText.getStrPassword();
            }
        });

        findViewById(R.id.wallet_pwd_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==mPwd) {
                    ToastUtils.ToastMessage(mCtx,getString(R.string.pwd_not_6));
                    return;
                }
                if (!TextUtils.equals(firstPwd,mPwd)){
                    ToastUtils.ToastMessage(mCtx,getString(R.string.re_input_wrong));
                    editText.clear();
                    return;
                }
                updatePwd();
            }
        });
    }

    private void updatePwd() {
        UpdatePwdBody bod=new UpdatePwdBody();
        bod.oldPassword=oldPwd;
        if (TextUtils.isEmpty(bod.oldPassword))bod.oldPassword=null;
        bod.newPassword=mPwd;
        if (vCode==null){
            doUpdatePwd(bod);
        }else {
            doForgetPwd(bod);
        }
        findViewById(R.id.wallet_pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void doForgetPwd(UpdatePwdBody bod) {
        TribeRetrofit.getInstance().createApi(MoneyService.class).updatePwd(TribeApplication.getInstance().getUserInfo().getId(),
                bod,vCode).enqueue(new retrofit2.Callback<BaseCodeResponse<CodeResponse>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<CodeResponse>> call, Response<BaseCodeResponse<CodeResponse>> response) {
                if (response.body()!=null&&response.body().code==200){
                    ToastUtils.ToastMessage(mCtx,getString(R.string.update_success));
                    startActivity(new Intent(UpdateWalletPwdActivity2.this,WalletActivity.class));
                    finish();
                }else if (response.body()!=null&&response.body().code==401){
                    ToastUtils.ToastMessage(mCtx,getString(R.string.wrong_pwd));
                }else {
                    ToastUtils.ToastMessage(mCtx,getString(R.string.connect_fail));
                }
            }
            @Override
            public void onFailure(Call<BaseCodeResponse<CodeResponse>> call, Throwable t) {
                ToastUtils.ToastMessage(mCtx,getString(R.string.connect_fail));
            }
        });
    }

    private void doUpdatePwd(UpdatePwdBody bod) {
        TribeRetrofit.getInstance().createApi(MoneyService.class).updatePwd(TribeApplication.getInstance().getUserInfo().getId(),
                bod).enqueue(new retrofit2.Callback<BaseCodeResponse<CodeResponse>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<CodeResponse>> call, Response<BaseCodeResponse<CodeResponse>> response) {
                if (response.body()!=null&&response.body().code==200){
                    ToastUtils.ToastMessage(mCtx,getString(R.string.update_success));
                    startActivity(new Intent(UpdateWalletPwdActivity2.this,WalletActivity.class));
                    finish();
                }else if (response.body()!=null&&response.body().code==401){
                    ToastUtils.ToastMessage(mCtx,getString(R.string.wrong_pwd));
                }else {
                    ToastUtils.ToastMessage(mCtx,getString(R.string.connect_fail));
                }
            }
            @Override
            public void onFailure(Call<BaseCodeResponse<CodeResponse>> call, Throwable t) {
                ToastUtils.ToastMessage(mCtx,getString(R.string.connect_fail));
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_update_pwd;
    }
}
