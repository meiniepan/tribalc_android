package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.DiscountIntroAdapter;
import com.gs.buluo.app.bean.Privilege;
import com.gs.buluo.app.bean.PrivilegeResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.StoreApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/17.
 */

public class PrivilegeActivity extends BaseActivity {
    @BindView(R.id.strategy_introduction_list)
    ListView listView;
    @BindView(R.id.strategy_introduction_name)
    TextView tvName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String storeId = intent.getStringExtra(Constant.STORE_ID);
        String storeName = intent.getStringExtra(Constant.STORE_NAME);
        tvName.setText(storeName);
        TribeRetrofit.getInstance().createApi(StoreApis.class).getDiscountInfo(storeId, TribeApplication.getInstance().getUserInfo().getId(),true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<PrivilegeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<PrivilegeResponse> listBaseResponse) {
                        setData(listBaseResponse.data.privileges);
                    }
                });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_discount;
    }

    public void setData(List<Privilege> data) {
        listView.setAdapter(new DiscountIntroAdapter(this, data));
    }
}
