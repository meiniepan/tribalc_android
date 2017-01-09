package com.gs.buluo.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CommunityListAdapter;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.model.CommunityModel;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.app.view.widget.loadMoreRecycle.Action;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2016/11/1.
 */
public class FoundFragment extends BaseFragment implements Callback<CommunityResponse> {
    @Bind(R.id.community_list)
    RefreshRecyclerView recyclerView;
    private CommunityListAdapter adapter;
    private CommunityModel model;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_found;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setUserVisibleHint(false);
        model = new CommunityModel();
        model.getCommunitiesList(this);
        adapter = new CommunityListAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        recyclerView.setNeedLoadMore(false);
        recyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                loadData();
            }
        });
    }

    private void loadData() {
        model.getCommunitiesList(this);
    }

    @Override
    public void onResponse(Call<CommunityResponse> call, Response<CommunityResponse> response) {
        recyclerView.dismissSwipeRefresh();
        if (response.body()!=null&&response.body().code==200){
            if (mContext==null)return;
            adapter.clear();
            adapter.addAll(response.body().data);
        }
    }

    @Override
    public void onFailure(Call<CommunityResponse> call, Throwable t) {
        ToastUtils.ToastMessage(getActivity(),R.string.connect_fail);
    }
}
