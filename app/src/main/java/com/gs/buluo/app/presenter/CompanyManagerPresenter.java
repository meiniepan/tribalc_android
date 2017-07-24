package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.ICompanyManagerView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/24.
 */

public class CompanyManagerPresenter extends BasePresenter<ICompanyManagerView> {
    public void getCompanyInfo(){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                               @Override
                               public void onNext(BaseResponse<WalletAccount> response) {
//                                   if (isAttach()) mView.getCompanyInfoFinished(response.data);
                               }
                           }
                );
    }
}
