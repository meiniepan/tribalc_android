package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.model.AddressModel;
import com.gs.buluo.app.network.TribeCallback;
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
        addressModel.getAddress(uid, addId, new Callback<BaseResponse<UserAddressEntity>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserAddressEntity>> call, Response<BaseResponse<UserAddressEntity>> response) {

            }

            @Override
            public void onFailure(Call<BaseResponse<UserAddressEntity>> call, Throwable t) {
            }
        });
    }

    public void deleteAddress(String uid, final UserAddressEntity entity) {
        addressModel.deleteAddress(uid, entity.getId(), new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().code == 204) {
                    new AddressInfoDao().deleteAddress(entity);
                    mView.deleteSuccessInfo(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }


    public void updateDefaultAddress(final UserAddressEntity entity) {
        addressModel.updateDefaultAddress(TribeApplication.getInstance().getUserInfo().getId(), entity.getId(), new TribeCallback<IBaseResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<IBaseResponse>> response) {
                UserInfoDao userSensitiveDao = new UserInfoDao();
                UserInfoEntity first = userSensitiveDao.findFirst();
                first.setAddressID(entity.getId());
                userSensitiveDao.update(first);
                mView.updateDefaultAddressSuccess(entity);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<IBaseResponse> body) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
