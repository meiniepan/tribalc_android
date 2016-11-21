package com.gs.buluo.app.presenter;

import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.WalletResponse;
import com.gs.buluo.app.bean.UpdatePwdBody;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.utils.SharePreferenceManager;
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
        moneyModel.getWelletInfo(TribeApplication.getInstance().getUserInfo().getId(), new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getWalletInfoFinished(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
