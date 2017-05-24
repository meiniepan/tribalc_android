package com.gs.buluo.app.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CommunityListAdapter;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.network.CommunityApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/11/1.
 */
public class FoundFragment extends BaseFragment {
    @Bind(R.id.community_list)
    RefreshRecyclerView recyclerView;
    private CommunityListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_found;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setUserVisibleHint(false);
        getData();
        adapter = new CommunityListAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                adapter.clear();
                getData();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void getData() {
        TribeRetrofit.getInstance().createApi(CommunityApis.class).
                getCommunitiesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CommunityResponse>(false) {
                    @Override
                    public void onNext(CommunityResponse response) {
                        recyclerView.dismissSwipeRefresh();
                        if (response != null && response.code == 200 && mContext != null) {
                            adapter.clear();
                            adapter.addAll(response.data);
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        recyclerView.dismissSwipeRefresh();
                    }
                });
    }


}
