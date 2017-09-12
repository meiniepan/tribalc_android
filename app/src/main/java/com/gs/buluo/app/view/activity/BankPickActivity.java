package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.BankListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BankPickActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.bank_pick_list)
    ListView mListView;
    @Bind(R.id.bank_status)
    StatusLayout statusLayout;

    private List<BankCard> mList;

    int withHoldAmount = 0;

    private int type = 0;  // 0 普通添加银行卡； 1.代扣添加银行卡
    private BankListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        withHoldAmount = intent.getIntExtra(Constant.BANK_LIMIT, 0);
        type = intent.getIntExtra(Constant.BANK_TYPE, 0);
        adapter = new BankListAdapter(this);
        adapter.setType(type);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mList = new ArrayList<>();
        getBankList();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_back_pick;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(Constant.ForIntent.FLAG, mList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    public void getBankList() {
        statusLayout.showProgressView();
        if (type == 0) {
            setAllBank();
        } else if (type == 1) {
            setSupportWithdrawBank();
        }

    }

    private void setSupportWithdrawBank() {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getSupportBankCards("WITHHOLD")
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResponse<ArrayList<BankCard>>, Observable<BankCard>>() {
                    @Override
                    public Observable<BankCard> call(BaseResponse<ArrayList<BankCard>> response) {
                        return Observable.from(response.data);
                    }
                })
                .filter(new Func1<BankCard, Boolean>() {
                    @Override
                    public Boolean call(BankCard BankCard) {
                        return BankCard.limit > withHoldAmount;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BankCard>() {
                    @Override
                    public void onNext(BankCard BankCard) {
                        mList.add(BankCard);
                    }

                    @Override
                    public void onCompleted() {
                        statusLayout.showContentView();
                        adapter.setData(mList);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(getString(R.string.connect_fail));
                    }
                });
    }

    private void setAllBank() {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getAllCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(BaseResponse<List<BankCard>> listBaseResponse) {
                        statusLayout.showContentView();
                        mList.addAll(listBaseResponse.data);
                        adapter.setData(mList);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(e.getDisplayMessage());
                    }
                });
    }
}
