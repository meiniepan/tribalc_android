package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.HighBuyListAdapter;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.bean.StoreListResponse;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.activity.StoreInfoActivity;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/18.
 */

public class HighBuyFragment extends BaseFragment {
    @Bind(R.id.list_high_buy)
    ListView mListView;
    @Bind(R.id.high_buy_status)
    StatusLayout mStatusLayout;
    HighBuyListAdapter mAdapter;
    ArrayList<StoreInfo> datas = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_high_buy;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        getData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StoreInfoActivity.class);
                intent.putExtra(Constant.STORE_ID, datas.get(position).id);
                startActivity(intent);
            }
        });

    }

    public void getData() {
        datas.clear();
        mStatusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreListResponse>>() {
                    @Override
                    public void onNext(BaseResponse<StoreListResponse> response) {
                        datas = response.data.content;
                        if (datas.size() > 0)
                            mStatusLayout.showContentView();
                        else
                            mStatusLayout.showEmptyView("暂无信息");
                        mAdapter = new HighBuyListAdapter(mContext, response.data.content);
                        mListView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        mStatusLayout.showErrorView("获取嗨购信息失败");
                    }
                });
    }
}
