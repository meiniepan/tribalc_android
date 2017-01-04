package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
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
        moneyModel.getWelletInfo(TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseCodeResponse<WalletAccount>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<WalletAccount>> call, Response<BaseCodeResponse<WalletAccount>> response) {
                if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
                    mView.getWalletInfoFinished(response.body().data);
                }else {
                    mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<WalletAccount>> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
