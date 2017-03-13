package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
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
    ExpandableListView listView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        final ArrayList<VisitorBean> list = new ArrayList();
        ArrayList<String> childList = new ArrayList<>();
        childList.add("d大门");
        childList.add("梵蒂冈");
        childList.add("大概是 ");
        childList.add("阿凡达");
        list.add(new VisitorBean("张三","123456",childList));
        list.add(new VisitorBean("张是的","12412421",childList));
        list.add(new VisitorBean("张偶","241545",childList));
        list.add(new VisitorBean("张恩恩","3464363",childList));
        list.add(new VisitorBean("李元芳","3464363",childList));
        listView.setAdapter(new VisitorListAdapter(getCtx(),list));
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent=new Intent(getCtx(),QRShowActivity.class);
                intent.putExtra(Constant.VISITOR,list.get(groupPosition).door.get(childPosition));
                startActivity(intent);
                return false;
            }
        });
        findViewById(R.id.add_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(),AddVisitorActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_visitor_list;
    }
}
