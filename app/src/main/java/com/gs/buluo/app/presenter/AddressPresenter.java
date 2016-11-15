package com.gs.buluo.app.presenter;

import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.model.AddressModel;
import com.gs.buluo.app.view.impl.IAddressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressPresenter extends BasePresenter<IAddressView>{
    AddressModel addressModel;
    public AddressPresenter(){
        addressModel=new AddressModel();
    }


    public void getAddress(String uid,String addId){
        addressModel.getAddress(uid, addId, new Callback<UserAddressResponse>() {
            @Override
            public void onResponse(Call<UserAddressResponse> call, Response<UserAddressResponse> response) {
                Log.d("aaaaaaaaa", "onResponse: ");
            }

            @Override
            public void onFailure(Call<UserAddressResponse> call, Throwable t) {
                Log.d("aaaaaaaaa", "onResponse: ");
            }
        });
    }

    public void addAddress(String uid, final UserAddressEntity entity){
        addressModel.addAddress(uid, entity, new Callback<UserAddressResponse>() {
            @Override
            public void onResponse(Call<UserAddressResponse> call, Response<UserAddressResponse> response) {
                if (response.code()==200){
                    UserAddressEntity addressEntity = response.body().data;
                    addressEntity.setArea(addressEntity.getProvice(),addressEntity.getCity(),addressEntity.getDistrict());
                    new AddressInfoDao().saveBindingId(addressEntity);
                    mView.setSuccessInfo(addressEntity);
                }
            }

            @Override
            public void onFailure(Call<UserAddressResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void updateAddress(String uid ,String addId,UserAddressEntity entity){
        addressModel.updateAddress(uid, addId,entity, new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                Log.d("aaaaaaaaa", "onResponse: ");
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void deleteAddress(String uid , final UserAddressEntity entity){
        addressModel.deleteAddress(uid, entity.getId(), new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                mView.setSuccessInfo(entity);
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }


}
