package com.gs.buluo.app.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.ResponseBody.PropertyFixListResponse;
import com.gs.buluo.app.network.PropertyService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyListActivity extends BaseActivity {


    private RefreshRecyclerView mRecyclerView;
    private List<ListPropertyManagement> mData;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.property_list_recycleView);
        initData();
    }

    private void initData() {
        TribeRetrofit.getIntance().createApi(PropertyService.class).getPropertyFixList(TribeApplication.getInstance().getUserInfo().getId(),null,20,null).enqueue(new Callback<PropertyFixListResponse>() {
            @Override
            public void onResponse(Call<PropertyFixListResponse> call, Response<PropertyFixListResponse> response) {
                if (response.body().code==200) {
                    mData = response.body().data;
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
}
