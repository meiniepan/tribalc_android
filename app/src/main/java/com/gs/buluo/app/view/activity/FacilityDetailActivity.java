package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.FacilityAdapter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/2/20.
 */
public class FacilityDetailActivity extends BaseActivity{
    @Bind(R.id.facility_detail_list)
    GridView gridView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList<String> list = getIntent().getStringArrayListExtra(Constant.ForIntent.FANCILITY);
        gridView.setAdapter(new FacilityAdapter(this,list));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_facility;
    }
}
