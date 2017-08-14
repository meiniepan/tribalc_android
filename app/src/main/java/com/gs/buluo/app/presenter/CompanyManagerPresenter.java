package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.ICompanyManagerView;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/24.
 */

public class CompanyManagerPresenter extends BasePresenter<ICompanyManagerView> {
    public void getCompanyInfo(String companyId){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(companyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                               @Override
                               public void onNext(BaseResponse<WalletAccount> response) {
                                   if (isAttach()) mView.getCompanyInfoFinished(response.data);
                               }

                               @Override
                               public void onFail(ApiException e) {
                                   if (e.getCode()==403){
                                       mView.showError(R.string.no_manager);
                                   }else {
                                       mView.showError(R.string.connect_fail);
                                   }
                               }
                           }
                );
    }
}
