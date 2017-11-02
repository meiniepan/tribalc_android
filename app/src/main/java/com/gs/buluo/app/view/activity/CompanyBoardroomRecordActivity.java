package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CompanyBoardroomRecordAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by hjn on 2017/10/31.
 */

public class CompanyBoardroomRecordActivity extends BaseActivity{
    @BindView(R.id.company_boardroom_record)
    RecyclerView recyclerView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList list = new ArrayList();
        recyclerView.setAdapter(new CompanyBoardroomRecordAdapter(R.layout.item_company_boardroom_record,list));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_boardroom_record;
    }
}
