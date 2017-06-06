package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.ICardView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCardPresenter extends BasePresenter<ICardView> {
    public void getCardList(String uid){
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getCardList(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(BaseResponse<List<BankCard>> response) {
                        if (isAttach())  mView.getCardInfoSuccess(response.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isAttach())  mView.showError(R.string.net_error);
                    }
                });
    }
}
