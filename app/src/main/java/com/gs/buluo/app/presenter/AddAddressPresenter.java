package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.model.AddressModel;
import com.gs.buluo.app.impl.IAddAddressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/15.
 */
public class AddAddressPresenter extends BasePresenter<IAddAddressView> {

    AddressModel addressModel;

    public AddAddressPresenter(){
        addressModel=new AddressModel();
    }
    public void addAddress(String uid, final UserAddressEntity entity){
        addressModel.addAddress(uid, entity, new Callback<UserAddressResponse>() {
            @Override
            public void onResponse(Call<UserAddressResponse> call, Response<UserAddressResponse> response) {
                if (response.code()==200){
                    UserAddressEntity addressEntity = response.body().data;
                    addressEntity.setArea(addressEntity.getProvice(),addressEntity.getCity(),addressEntity.getDistrict());
                    addressEntity.setUid(TribeApplication.getInstance().getUserInfo().getId());
                    new AddressInfoDao().saveBindingId(addressEntity);
                    mView.addAddressSuccess(addressEntity);
                }
            }

            @Override
            public void onFailure(Call<UserAddressResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void updateAddress(String uid , String addId, final UserAddressEntity entity){
        addressModel.updateAddress(uid, addId,entity, new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                entity.setArea(entity.getProvice(),entity.getCity(),entity.getDistrict());
                new AddressInfoDao().update(entity);
                mView.updateAddressSuccess(entity);
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
