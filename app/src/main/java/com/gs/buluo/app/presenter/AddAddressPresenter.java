package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.network.AddressApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IAddAddressView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/15.
 */
public class AddAddressPresenter extends BasePresenter<IAddAddressView> {


    public void addAddress(String uid, final UserAddressEntity entity){

        TribeRetrofit.getInstance().createApi(AddressApis.class).
               addAddress(uid, entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserAddressEntity>>() {
                    @Override
                    public void onNext(BaseResponse<UserAddressEntity> response) {
                        if (response.code==201){
                            UserAddressEntity addressEntity = response.data;
                            addressEntity.setArea(addressEntity.getProvice(),addressEntity.getCity(),addressEntity.getDistrict());
                            addressEntity.setUid(TribeApplication.getInstance().getUserInfo().getId());
                            new AddressInfoDao().saveBindingId(addressEntity);
                            mView.addAddressSuccess(addressEntity);
                        }
                    }
                });
    }

    public void updateAddress(String uid , String addId, final UserAddressEntity entity){

        TribeRetrofit.getInstance().createApi(AddressApis.class).
                updateAddress(uid, addId,entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.code==200){
                            entity.setArea(entity.getProvice(),entity.getCity(),entity.getDistrict());
                            new AddressInfoDao().update(entity);
                            mView.updateAddressSuccess(entity);
                        }
                    }
                });
    }
}
