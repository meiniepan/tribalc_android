package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BoardroomFilterResultAdapter;
import com.gs.buluo.app.bean.BoardroomBean;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hjn on 2017/10/19.
 */

public class BoardroomFilterResultActivity extends BaseActivity {
    @BindView(R.id.room_filter_floor)
    TextView roomFilterFloor;
    @BindView(R.id.room_filter_person_number)
    TextView roomFilterPersonNumber;
    @BindView(R.id.room_filter_start_time)
    TextView roomFilterStartTime;
    @BindView(R.id.room_filter_end_time)
    TextView roomFilterEndTime;
    @BindView(R.id.room_filter_duration)
    TextView roomFilterDuration;
    @BindView(R.id.room_filter_equip)
    TextView roomFilterEquip;
    @BindView(R.id.room_filter_list)
    RecyclerView roomFilterList;
    @BindView(R.id.room_filter_status)
    StatusLayout roomFilterStatus;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.room_filter_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<BoardroomBean> list = new ArrayList<>();
        list.add(new BoardroomBean());
        list.add(new BoardroomBean());
        list.add(new BoardroomBean());
        list.add(new BoardroomBean());
        list.add(new BoardroomBean());
        list.add(new BoardroomBean());
        list.add(new BoardroomBean());
        list.add(new BoardroomBean());
        roomFilterList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomFilterList.setNestedScrollingEnabled(false);
        roomFilterList.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 15, getResources().getColor(R.color.tint_bg)));
        roomFilterList.setAdapter(new BoardroomFilterResultAdapter(R.layout.item_room_filter_result, list));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_room_filter_result;
    }

}
