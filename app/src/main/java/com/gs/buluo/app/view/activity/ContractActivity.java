package com.gs.buluo.app.view.activity;

import android.os.Bundle;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/6/27.
 */

public class ContractActivity extends BaseActivity {
    @Bind(R.id.contract_list)
    Banner banner;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList<String> datas = getIntent().getStringArrayListExtra(Constant.CONTRACT);
        banner.setImageLoader(new FrescoImageLoader(false));
        banner.setImages(datas);
        banner.isAutoPlay(false);
        banner.setDelayTime(2000);
        banner.start();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_contract;
    }
}
