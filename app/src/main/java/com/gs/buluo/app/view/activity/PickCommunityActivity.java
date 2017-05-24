package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CommunityPickAdapter;
import com.gs.buluo.app.bean.CommunityPlate;
import com.gs.buluo.app.bean.ResponseBody.CommunityResponse;
import com.gs.buluo.app.network.CommunityApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PickCommunityActivity extends BaseActivity implements  AdapterView.OnItemClickListener {
    private List<CommunityPlate> mList=new ArrayList<>();
    @Bind(R.id.pick_community_listview)
    ListView mListView;
    private CommunityPickAdapter mAdapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(CommunityApis.class).getCommunitiesList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<CommunityResponse>() {
                    @Override
                    public void onNext(CommunityResponse response) {
                        mList.clear();
                        mList.addAll(response.data);
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mAdapter = new CommunityPickAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_pick_company;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,ChooseCompanyActivity.class);
        intent.putExtra(Constant.COMMUNITY_ID,mList.get(position).id);
        startActivity(intent);
        finish();
    }
}
