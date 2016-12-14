package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CommunityPickAdapter;
import com.gs.buluo.app.bean.CommunityPlate;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.network.AddressService;
import com.gs.buluo.app.network.CommunityService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Response;

public class PickCommunityActivity extends BaseActivity implements retrofit2.Callback<CommunityResponse>, AdapterView.OnItemClickListener {
    private static final String TAG = "PickCommunityActivity";
    private List<CommunityPlate> mList=new ArrayList<>();
    @Bind(R.id.pick_community_listview)
    ListView mListView;
    private CommunityPickAdapter mAdapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        TribeRetrofit.getIntance().createApi(CommunityService.class).getCommunitiesList().enqueue(this);
        mAdapter = new CommunityPickAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pick_company;
    }

    @Override
    public void onResponse(Call<CommunityResponse> call, Response<CommunityResponse> response) {
        if (response.body().code==200) {
            mList.clear();
            mList.addAll(response.body().data);
            Log.d(TAG, "onResponse: "+mList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(Call<CommunityResponse> call, Throwable t) {
        ToastUtils.ToastMessage(this,"获取社区列表失败");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ChooseCompanyActivity.class);
        intent.putExtra(Constant.COMMUNITY_ID,mList.get(position).id);
        startActivity(intent);
        finish();
    }
}
