package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

/**
 * Created by hjn on 2016/11/29.
 */
public class CommunityVisitActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.community_visit_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.community_visit_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_community_visit;
    }
}
