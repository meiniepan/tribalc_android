package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.DepartmentListAdapter;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/6/23.
 */

public class DepartmentActivity extends BaseActivity  {
    @Bind(R.id.department_status)
    StatusLayout statusLayout;
    @Bind(R.id.department_list)
    ListView listView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList list = new ArrayList();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        listView.setAdapter(new DepartmentListAdapter(this,list));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_department;
    }
}
