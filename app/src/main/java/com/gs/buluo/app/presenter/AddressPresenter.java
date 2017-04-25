package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.CommonRequestBody;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.network.AddressApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IAddressView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressPresenter extends BasePresenter<IAddressView> {
    public void deleteAddress(String uid, final UserAddressEntity entity) {

        TribeRetrofit.getInstance().createApi(AddressApis.class).
                deleteAddress(uid,entity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        new AddressInfoDao().deleteAddress(entity);
                        mView.deleteSuccessInfo(entity);
                    }
                });
    }


    public void updateDefaultAddress(final UserAddressEntity entity) {
        TribeRetrofit.getInstance().createApi(AddressApis.class).
                updateDefaultAddress(TribeApplication.getInstance().getUserInfo().getId(), new CommonRequestBody(entity.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        UserInfoDao userSensitiveDao = new UserInfoDao();
                        UserInfoEntity first = userSensitiveDao.findFirst();
                        first.setAddressID(entity.getId());
                        userSensitiveDao.update(first);
                        mView.updateDefaultAddressSuccess(entity);
                    }
                });
    }
}
