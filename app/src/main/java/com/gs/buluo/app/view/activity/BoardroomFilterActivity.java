package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.TagAdapter;
import com.gs.buluo.app.bean.TagSelectBean;
import com.gs.buluo.app.utils.AutoLineFeedLayoutManager;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.view.widget.panel.DatePickPanel;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by hjn on 2017/10/19.
 */

public class BoardroomFilterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.boardroom_filter_start_floor)
    EditText boardroomFilterStartFloor;
    @BindView(R.id.boardroom_filter_end_floor)
    EditText boardroomFilterEndFloor;
    @BindView(R.id.boardroom_filter_person)
    EditText boardroomFilterPerson;
    @BindView(R.id.boardroom_filter_duration)
    EditText boardroomFilterDuration;
    @BindView(R.id.boardroom_filter_start_time)
    TextView boardroomFilterStartTime;
    @BindView(R.id.boardroom_filter_end_time)
    TextView boardroomFilterEndTime;
    @BindView(R.id.boardroom_equipment_list)
    RecyclerView boardroomEquipmentList;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        boardroomFilterStartTime.setOnClickListener(this);
        boardroomFilterEndTime.setOnClickListener(this);

        boardroomEquipmentList.setLayoutManager(new AutoLineFeedLayoutManager(true));
        final ArrayList<TagSelectBean> list = new ArrayList<>();
        list.add(new TagSelectBean("窗户"));
        list.add(new TagSelectBean("窗户3"));
        list.add(new TagSelectBean("窗户44"));
        list.add(new TagSelectBean("窗户1231"));
        list.add(new TagSelectBean("窗户41"));
        list.add(new TagSelectBean("窗户545"));
        list.add(new TagSelectBean("窗户536"));
        list.add(new TagSelectBean("窗户675"));
        list.add(new TagSelectBean("窗户453"));
        list.add(new TagSelectBean("窗户123"));
        list.add(new TagSelectBean("窗户333"));
        list.add(new TagSelectBean("窗户666"));
        list.add(new TagSelectBean("窗户777"));
        list.add(new TagSelectBean("窗户7778"));
        list.add(new TagSelectBean("窗户888"));

        TagAdapter adapter = new TagAdapter(R.layout.tag_item, list);
        boardroomEquipmentList.setAdapter(adapter);
        boardroomEquipmentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = DensityUtils.dip2px(getCtx(), 10);
                outRect.top = DensityUtils.dip2px(getCtx(), 10);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                list.get(position).isSelected = !list.get(position).isSelected;
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_filter;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boardroom_filter_start_time:
                showTimePicker(boardroomFilterStartTime);
                break;
            case R.id.boardroom_filter_end_time:
                showTimePicker(boardroomFilterEndTime);
                break;
        }
    }

    private void showTimePicker(final TextView textView) {
        DatePickPanel pickPanel = new DatePickPanel(this, new DatePickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(int year, int month, int day) {
                StringBuilder sb = new StringBuilder();
                sb.append(year).append("年").append(month).append("月").append(day).append("日");
                textView.setText(sb.toString());
            }
        });
        pickPanel.setCurrentDate("2017", "10", "19");
        pickPanel.show();
    }


    //next
    public void doBoardroomFilter(View view) {
        Intent intent= new Intent(this,BoardroomFilterResultActivity.class);
        startActivity(intent);
    }
}
