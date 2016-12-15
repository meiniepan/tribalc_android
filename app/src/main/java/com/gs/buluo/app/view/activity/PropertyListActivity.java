package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.PropertyFixListAdapter;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.ResponseBody.PropertyFixListResponse;
import com.gs.buluo.app.network.PropertyService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;


import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyListActivity extends BaseActivity implements View.OnClickListener {


    private RefreshRecyclerView mRecyclerView;
    private List<ListPropertyManagement> mData;
    private Context mContext;
    private static final String TAG = "PropertyListActivity";
    private PropertyFixListAdapter mAdapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext=this;
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.property_list_recycleView);
        findViewById(R.id.property_list_back).setOnClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PropertyFixListAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        initData();
    }

    private void initData() {

        TribeRetrofit.getIntance().createApi(PropertyService.class).getPropertyFixList(TribeApplication.getInstance().getUserInfo().getId()).enqueue(new Callback<PropertyFixListResponse>() {
            @Override
            public void onResponse(Call<PropertyFixListResponse> call, Response<PropertyFixListResponse> response) {
                if (response.body().code==200) {
                    mData = response.body().data.content;
                    mAdapter.addAll(mData);
                }
            }

            @Override
            public void onFailure(Call<PropertyFixListResponse> call, Throwable t) {
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_property_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.property_list_back:
                finish();
                break;
        }
    }


}
