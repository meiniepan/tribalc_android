package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.model.LoginModel;
import com.gs.buluo.app.impl.ISelfView;

import org.xutils.common.Callback;

/**
 * Created by hjn on 2016/11/3.
 */
public class SelfPresenter extends BasePresenter<ISelfView> {
    LoginModel loginModel;
    public SelfPresenter(){
        loginModel=new LoginModel();
    }

    public void  updateUser(final String key, final String value){
        loginModel.updateUser(TribeApplication.getInstance().getUserInfo().getId(), key, value, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.contains("200")){
                    if (isAttach())mView.updateSuccess(key,value);
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
