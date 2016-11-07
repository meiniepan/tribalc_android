package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BaseRecycleAdapter;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.view.holder.AddressHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by hjn on 2016/11/2.
 */
public class DetailAddressActivity extends BaseActivity{
    @Bind(R.id.address_list)
    RecyclerView mRecView;

    List mDatas=new ArrayList();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.address_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mDatas.add("something");
        BaseRecycleAdapter adapter=new BaseRecycleAdapter(mDatas,R.layout.address_item,AddressHolder.class);
        adapter.setmDatas(mDatas);
        mRecView.setAdapter(adapter);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_detail_address;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

}
