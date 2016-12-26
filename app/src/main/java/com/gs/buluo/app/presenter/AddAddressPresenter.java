package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.model.AddressModel;
import com.gs.buluo.app.view.impl.IAddAddressView;

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
        addressModel.addAddress(uid, entity, new Callback<BaseCodeResponse<UserAddressEntity>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<UserAddressEntity>> call, Response<BaseCodeResponse<UserAddressEntity>> response) {
                if (response.body().code==201){
                    UserAddressEntity addressEntity = response.body().data;
                    addressEntity.setArea(addressEntity.getProvice(),addressEntity.getCity(),addressEntity.getDistrict());
                    addressEntity.setUid(TribeApplication.getInstance().getUserInfo().getId());
                    new AddressInfoDao().saveBindingId(addressEntity);
                    mView.addAddressSuccess(addressEntity);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<UserAddressEntity>> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void updateAddress(String uid , String addId, final UserAddressEntity entity){
        addressModel.updateAddress(uid, addId,entity, new Callback<BaseCodeResponse>() {
            @Override
            public void onResponse(Call<BaseCodeResponse> call, Response<BaseCodeResponse> response) {
                if (response.body().code==200){
                    entity.setArea(entity.getProvice(),entity.getCity(),entity.getDistrict());
                    new AddressInfoDao().update(entity);
                    mView.updateAddressSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
