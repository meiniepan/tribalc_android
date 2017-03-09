package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.DoorListAdapter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2017/3/9.
 */
public class DoorListActivity extends BaseActivity{
    @Bind(R.id.door_list)
    ListView listView;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        final ArrayList list = new ArrayList();
        list.add("一楼门锁");
        list.add("儿楼门锁");
        list.add("三楼门锁");
        list.add("死楼门锁");
        listView.setAdapter(new DoorListAdapter(getCtx(),list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getCtx(),QRShowActivity.class);
                intent.putExtra(Constant.QR_CODE,Constant.APK_URL);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_door_list;
    }
}
