package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.DoorListAdapter;
import com.gs.buluo.app.adapter.VisitorListAdapter;
import com.gs.buluo.app.bean.VisitorBean;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListActivity extends BaseActivity{
    @Bind(R.id.visitor_list)
    ListView listView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        final ArrayList<VisitorBean> list = new ArrayList();
        list.add(new VisitorBean("张三","123456","d大门"));
        list.add(new VisitorBean("张三22","12412421","梵蒂冈"));
        list.add(new VisitorBean("张三333","241545","大概是 "));
        list.add(new VisitorBean("张三444","3464363","阿凡达"));
        listView.setAdapter(new VisitorListAdapter(getCtx(),list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getCtx(),QRShowActivity.class);
                intent.putExtra(Constant.VISITOR,list.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_visitor_list;
    }
}
