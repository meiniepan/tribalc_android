package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.view.impl.IWalletView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/18.
 */
public class WalletPresenter extends BasePresenter<IWalletView>{

    MoneyModel moneyModel;
    public WalletPresenter(){
        moneyModel=new MoneyModel();
    }

    public void getWalletInfo(){
        moneyModel.getWelletInfo(TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseResponse<WalletAccount>>() {
            @Override
            public void onResponse(Call<BaseResponse<WalletAccount>> call, Response<BaseResponse<WalletAccount>> response) {
                if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
                    if (isAttach()) mView.getWalletInfoFinished(response.body().data);
                }else {
                    if (isAttach()) mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<WalletAccount>> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }
}
