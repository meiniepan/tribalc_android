package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CompanyPickAdapter;
import com.gs.buluo.app.bean.CompanyPlate;
import com.gs.buluo.app.bean.ResponseBody.CompanyResponse;
import com.gs.buluo.app.network.CompanyService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.wheel.CompanyPickPanel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PickCompanyActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "PickCompanyActivity";
    @Bind(R.id.pick_company_listView)
    ListView mListView;
    List mList=new ArrayList<CompanyPickPanel>();
    private CompanyPickAdapter mAdapter;
    private Context mContext;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String communityID = intent.getStringExtra(Constant.COMMUNITY_ID);
        mAdapter = new CompanyPickAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mContext=this;
        TribeRetrofit.getIntance().createApi(CompanyService.class).getCompaniesList(communityID).enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                List<CompanyPlate> data = response.body().data;
                mList.clear();
                mList.addAll(data);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                ToastUtils.ToastMessage(mContext,"获取公司列表失败,请检查网络");
            }
        });
        
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_pick;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String companyID = ((CompanyPlate) mList.get(position)).id;
        Log.d(TAG, "onItemClick: "+companyID);
    }

}
