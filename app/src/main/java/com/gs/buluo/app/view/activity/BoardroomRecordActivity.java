package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BoardroomRecordAdapter;
import com.gs.buluo.app.bean.BoardroomBean;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.app.view.widget.recyclerHelper.NewRefreshRecyclerView;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by hjn on 2017/10/20.
 */

public class BoardroomRecordActivity extends BaseActivity {
    @BindView(R.id.room_record_list)
    NewRefreshRecyclerView recyclerView;
    @BindView(R.id.room_record_status)
    StatusLayout statusLayout;

    ArrayList<BoardroomBean> data = new ArrayList<>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        data.add(new BoardroomBean());
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        BoardroomRecordAdapter adapter = new BoardroomRecordAdapter(R.layout.item_room_record, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getCtx(), BoardroomRecordDetailActivity.class));
            }
        });

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_record;
    }
}
