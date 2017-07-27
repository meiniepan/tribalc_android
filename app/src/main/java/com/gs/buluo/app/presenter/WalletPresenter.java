package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IWalletView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/18.
 */
public class WalletPresenter extends BasePresenter<IWalletView> {

    public void getWalletInfo() {
        String id = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(id, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                               @Override
                               public void onNext(BaseResponse<WalletAccount> response) {
                                   if (isAttach()) mView.getWalletInfoFinished(response.data);
                               }
                           }
                );
    }
}
