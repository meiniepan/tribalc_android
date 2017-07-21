package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.StoreTagsAdapter;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/19.
 */

public class StoreInfoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.recycle_store_tag)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_store_name)
    TextView tvName;
    @Bind(R.id.tv_store_category)
    TextView tvCategory;
    @Bind(R.id.tv_store_mark_place)
    TextView tvPlace;@Bind(R.id.tv_store_desc)
    TextView tvDesc;@Bind(R.id.buy)
    Button buy;
    private String storeId;
    private ArrayList dataTags;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_store_info;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        storeId = getIntent().getStringExtra(Constant.STORE_ID);
        getData();
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(8, StaggeredGridLayoutManager.VERTICAL));
        StoreTagsAdapter adapter = new StoreTagsAdapter(getCtx(), dataTags);
        mRecyclerView.setAdapter(adapter);
    }

    private void getData() {
        LoadingDialog.getInstance().show(getCtx(), "", true);
        TribeRetrofit.getInstance().createApi(StoreApis.class).getStoreInfo(storeId, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<StoreInfo>>() {
                    @Override
                    public void onNext(BaseResponse<StoreInfo> response) {
                        setData(response.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private void setData(StoreInfo data) {
        tvName.setText(data.name);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buy:

                break;
        }
    }
}
