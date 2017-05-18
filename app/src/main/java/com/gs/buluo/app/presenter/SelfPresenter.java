package com.gs.buluo.app.presenter;

import android.util.Log;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.SelfEvent;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.view.impl.ISelfView;
import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;

/**
 * Created by hjn on 2016/11/3.
 */
public class SelfPresenter extends BasePresenter<ISelfView> {
    private static final String TAG = "UpdateUser";
    private MainModel mainModel;

    private UserInfoEntity userInfo;
    public SelfPresenter(){
        userInfo = TribeApplication.getInstance().getUserInfo();
        mainModel =new MainModel();
    }

    public void  updateUser(final String key, final String value){
        mainModel.updateUser(TribeApplication.getInstance().getUserInfo().getId(), key, value, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.contains("200")){
                    Log.e(TAG,"update user success !!!!! key is "+key+"  value is"+value);
                    updateDao(key,value);
                    if (isAttach())mView.updateSuccess(key,value);
                } else {
                    if (isAttach())mView.showError(R.string.connect_fail);
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

    private void updateDao(String key, String value) {
        switch (key) {
            case Constant.NICKNAME:
                userInfo.setNickname(value);
                EventBus.getDefault().post(new SelfEvent());
                break;
            case Constant.SEX:
                userInfo.setSex(value);
                break;
            case Constant.BIRTHDAY:
                userInfo.setBirthday(value);
                break;
            case Constant.EMOTION:
                userInfo.setEmotion(value);
                break;
            case Constant.AREA:
                userInfo.setArea(value);
                break;
        }
        new UserInfoDao().update(userInfo);
    }
}
