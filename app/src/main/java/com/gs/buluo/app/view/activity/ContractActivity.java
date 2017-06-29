package com.gs.buluo.app.view.activity;

import android.os.Bundle;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;

/**
 * Created by hjn on 2017/6/27.
 */

public class ContractActivity extends BaseActivity {
    @Bind(R.id.contract_list)
    Banner banner;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList<String> datas = new ArrayList<>();
        String data = getIntent().getStringExtra(Constant.CONTRACT);
        if (data == null) return;
        if (data.contains(",")) {
            String[] arrs = data.split(",");
            Collections.addAll(datas, arrs);
        } else {
            datas.add(data);
        }
        banner.setImageLoader(new FrescoImageLoader(false));
        banner.isAutoPlay(false);
        banner.setDelayTime(2000);
        banner.start();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_contract;
    }
}
