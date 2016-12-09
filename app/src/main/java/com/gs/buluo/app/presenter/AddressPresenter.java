package com.gs.buluo.app.presenter;

import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.model.AddressModel;
import com.gs.buluo.app.view.impl.IAddressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressPresenter extends BasePresenter<IAddressView> {
    AddressModel addressModel;

    public AddressPresenter() {
        addressModel = new AddressModel();
    }


    public void getAddress(String uid, String addId) {
        addressModel.getAddress(uid, addId, new Callback<UserAddressResponse>() {
            @Override
            public void onResponse(Call<UserAddressResponse> call, Response<UserAddressResponse> response) {
            }

            @Override
            public void onFailure(Call<UserAddressResponse> call, Throwable t) {
            }
        });
    }

    public void deleteAddress(String uid, final UserAddressEntity entity) {
        addressModel.deleteAddress(uid, entity.getId(), new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if (response.body().code == 204) {
                    new AddressInfoDao().deleteAddress(entity);
                    mView.deleteSuccessInfo(entity);
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }


    public void updateDefaultAddress(final UserAddressEntity entity) {
        addressModel.updateDefaultAddress(TribeApplication.getInstance().getUserInfo().getId(), entity.getId(), new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if (response.body().code == 200) {
                    UserSensitiveDao userSensitiveDao = new UserSensitiveDao();
                    UserSensitiveEntity first = userSensitiveDao.findFirst();
                    first.setAddressID(entity.getId());
                    userSensitiveDao.update(first);
                    mView.updateDefaultAddressSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });

    }
}
