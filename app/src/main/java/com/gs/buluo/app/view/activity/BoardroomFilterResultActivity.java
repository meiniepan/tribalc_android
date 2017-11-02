package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BoardroomFilterResultAdapter;
import com.gs.buluo.app.bean.BoardroomFilterBean;
import com.gs.buluo.app.bean.ConferenceEquipment;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    ArrayList<ConferenceRoom> list = new ArrayList<>();
    private BoardroomFilterResultAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.room_reserve_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(), BoardroomRecordActivity.class));
            }
        });
        roomFilterStatus.setInfoContentViewMargin(0, 0, 0, 0);
        findViewById(R.id.room_filter_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final BoardroomFilterBean bean = getIntent().getParcelableExtra(Constant.ROOM_FILTER);
        setData(bean);
        getFilterData(bean);
        roomFilterStatus.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterData(bean);
            }
        });
        roomFilterList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomFilterList.setNestedScrollingEnabled(false);
        roomFilterList.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.HORIZONTAL, 15, getResources().getColor(R.color.tint_bg)));
        adapter = new BoardroomFilterResultAdapter(R.layout.item_room_filter_result, list);
        roomFilterList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getCtx(), BoardroomReserveActivity.class);
                ConferenceRoom room = (ConferenceRoom) adapter.getData().get(position);
                room.startDate = bean.startDate;
                room.endDate = bean.endDate;
                intent.putExtra(Constant.CONFERENCE_ROOM, room);
                startActivity(intent);
            }
        });
    }

    private void getFilterData(BoardroomFilterBean bean) {
        roomFilterStatus.showProgressView();
        Map<String, String> keyMap = new HashMap<>();
        if (!TextUtils.isEmpty(bean.attendance)) keyMap.put("attendance", bean.attendance);
        if (!TextUtils.isEmpty(bean.startFloor)) keyMap.put("beginFloor", bean.startFloor);
        if (!TextUtils.isEmpty(bean.endFloor)) keyMap.put("endFloor", bean.endFloor);
        keyMap.put("searchBeginDate", bean.startDate + "");
        keyMap.put("searchEndDate", bean.endDate + "");
        if (!TextUtils.isEmpty(bean.duration))
            keyMap.put("duration", Integer.parseInt(bean.duration) * 3600 + "");

        String ids;
        StringBuilder sb = new StringBuilder();
        for (ConferenceEquipment equipment : bean.equipments) {
            sb.append(",").append(equipment.id);
        }
        if (sb.length() > 0) {
            ids = sb.toString().substring(1);
            keyMap.put("equipments", ids);
        }
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getBoardroomFilterList(keyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<ConferenceRoom>>>() {
                    @Override
                    public void onFail(ApiException e) {
                        roomFilterStatus.showErrorView(getString(R.string.connect_fail));
                    }

                    @Override
                    public void onNext(BaseResponse<List<ConferenceRoom>> conferenceRoomBaseResponse) {
                        roomFilterStatus.showContentView();
                        adapter.addData(conferenceRoomBaseResponse.data);
                        if (adapter.getData().size() == 0) {
                            roomFilterStatus.showEmptyView("抱歉，该条件下暂无可用会议室");
                        }
                    }
                });
    }

    private void setData(BoardroomFilterBean bean) {
        setFloor(bean);
        roomFilterPersonNumber.setText(bean.attendance);
        setDate(bean);
        roomFilterDuration.setText(bean.duration);
        StringBuilder sb = new StringBuilder();
        for (ConferenceEquipment equipment : bean.equipments) {
            sb.append(",").append(equipment.name);
        }
        String result;
        if (sb.length() > 0) {
            result = sb.toString().substring(1);
        } else {
            result = sb.toString();
        }
        roomFilterEquip.setText(result);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_room_filter_result;
    }

    public void setFloor(BoardroomFilterBean bean) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(bean.startFloor) && !TextUtils.isEmpty(bean.endFloor)) {
            sb.append(bean.endFloor).append("层以下");
        } else if (!TextUtils.isEmpty(bean.startFloor) && TextUtils.isEmpty(bean.endFloor)) {
            sb.append(bean.startFloor).append("层以上");
        } else if (!TextUtils.isEmpty(bean.startFloor) && !TextUtils.isEmpty(bean.endFloor)) {
            sb.append(bean.startFloor).append("-").append(bean.endFloor);
        }
        roomFilterFloor.setText(sb.toString());
    }

    public void setDate(BoardroomFilterBean bean) {
        String startDate;
        if (bean.startDate != 0) {
            startDate = TribeDateUtils.dateFormat5(new Date(bean.startDate));
        } else {
            startDate = TribeDateUtils.dateFormat5(new Date(System.currentTimeMillis()));
        }
        roomFilterStartTime.setText(startDate);
        String endDate;
        if (bean.endDate != 0) {
            endDate = TribeDateUtils.dateFormat5(new Date(bean.endDate));
        } else {
            endDate = TribeDateUtils.dateFormat5(new Date(System.currentTimeMillis() + 7 * 24 * 3600 * 1000));
        }
        roomFilterEndTime.setText(endDate);
    }
}
