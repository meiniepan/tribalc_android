package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.TagAdapter;
import com.gs.buluo.app.bean.ConferenceFilterBean;
import com.gs.buluo.app.bean.ConferenceEquipment;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.AutoLineFeedLayoutManager;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.addapp.pickers.picker.DatePicker;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.equip_status)
    StatusLayout statusLayout;
    private ArrayList<ConferenceEquipment> beanList = new ArrayList<>();
    private TagAdapter adapter;
    private DatePicker picker;
    private Calendar calendar;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        boardroomFilterStartTime.setOnClickListener(this);
        boardroomFilterEndTime.setOnClickListener(this);
        findViewById(R.id.boardroom_record).setOnClickListener(this);
        boardroomEquipmentList.setLayoutManager(new AutoLineFeedLayoutManager(true));
        statusLayout.setInfoContentViewMargin(0, 0, 0, 0);
        statusLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEquipList();
            }
        });
        getEquipList();
        adapter = new TagAdapter(R.layout.tag_item, beanList);
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
                beanList.get(position).isSelected = !beanList.get(position).isSelected;
                adapter.notifyItemChanged(position);
            }
        });
        initCalendar();
    }

    private void initCalendar() {
        calendar = Calendar.getInstance();
        currentDate = new Date(System.currentTimeMillis());
        startDate = currentDate;
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, 1);
        maxDate = calendar.getTime();
        currentMaxDate = maxDate;

        Calendar c2 = Calendar.getInstance();
        c2.setTime(startDate);
        c2.add(Calendar.WEEK_OF_MONTH, 1);
        endDate = c2.getTime();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_filter;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boardroom_filter_start_time:
                showTimePicker(0, boardroomFilterStartTime);
                break;
            case R.id.boardroom_filter_end_time:
                if (boardroomFilterStartTime.getText().length() == 0) {
                    ToastUtils.ToastMessage(getCtx(), "请先选择开始时间");
                    return;
                }
                showTimePicker(1, boardroomFilterEndTime);
                break;
            case R.id.boardroom_record:
                startActivity(new Intent(getCtx(), BoardroomRecordActivity.class));
                break;
        }
    }

    private Date maxDate; //滚轮可选的最大时间
    private Date currentDate;
    private Date currentMaxDate;//当前日期一个月后
    private Date endDate;//筛选会议结束时间
    private Date startDate;//筛选会议开始时间

    private void showTimePicker(final int flag, final TextView textView) {
        picker = new DatePicker(this);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setWeightEnable(true);

        final Calendar calendar = Calendar.getInstance();
        if (flag == 0) {
            calendar.setTime(currentDate);
            picker.setRangeStart(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.MONTH, 1);
            picker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        } else {
            calendar.setTime(startDate);
            picker.setRangeStart(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            calendar.setTime(maxDate);
            picker.setRangeEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        }
        picker.show();
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                if (flag == 0) {
                    boardroomFilterStartTime.setText(year + "-" + month + "-" + day);
                    calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                    startDate = calendar.getTime();
                    calendar.add(Calendar.WEEK_OF_MONTH, 1);
                    endDate = calendar.getTime();
                    if (endDate.before(currentMaxDate)) {
                        maxDate = endDate;
                    } else {
                        maxDate = currentMaxDate;
                        endDate = currentMaxDate;
                    }
                    boardroomFilterEndTime.setText("");
                    boardroomFilterEndTime.setHint("请选择结束日期");
                } else {
                    boardroomFilterEndTime.setText(year + "-" + month + "-" + day);
                    calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                    endDate = calendar.getTime();
                }
            }
        });
        picker.show();
    }

    //next
    public void doBoardroomFilter(View view) {
        ConferenceFilterBean bean = new ConferenceFilterBean();
        bean.startFloor = boardroomFilterStartFloor.getText().toString().trim();
        bean.endFloor = boardroomFilterEndFloor.getText().toString().trim();
        bean.attendance = boardroomFilterPerson.getText().toString().trim();
        bean.startDate = startDate.getTime() / 1000;
        bean.endDate = endDate.getTime() / 1000;
        String duration = boardroomFilterDuration.getText().toString().trim();

        if (!TextUtils.isEmpty(duration)) {
            bean.duration = Integer.parseInt(duration) + "";
        }

        bean.equipments = new ArrayList<>();
        for (ConferenceEquipment tag : beanList) {
            if (tag.isSelected) {
                bean.equipments.add(tag);
            }
        }

        Intent intent = new Intent(this, BoardroomFilterResultActivity.class);
        intent.putExtra(Constant.ROOM_FILTER, bean);
        startActivity(intent);
    }

    public void getEquipList() {
        statusLayout.showProgressView("获取可用设备信息中");
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getEquipments(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<ConferenceEquipment>>>() {
                    @Override
                    public void onNext(BaseResponse<List<ConferenceEquipment>> listBaseResponse) {
                        statusLayout.showContentView();
                        adapter.addData(listBaseResponse.data);
                        if (adapter.getData().size() == 0) {
                            statusLayout.showEmptyView("暂无可用设备");
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView("获取设备信息失败");
                    }
                });
    }
}
