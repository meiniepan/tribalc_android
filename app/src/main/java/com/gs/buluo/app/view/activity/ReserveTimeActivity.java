package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ReserveDateAdapter;
import com.gs.buluo.app.bean.ConferenceReservationDateEntity;
import com.gs.buluo.app.bean.ConferenceReserveTimeEntity;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CustomWheelRecyclerView;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/10/19.
 */

public class ReserveTimeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_time1)
    TextView mTvTime1;
    @BindView(R.id.tv_time2)
    TextView mTvTime2;
    @BindView(R.id.tv_time3)
    TextView mTvTime3;
    @BindView(R.id.tv_time4)
    TextView mTvTime4;
    @BindView(R.id.tv_time5)
    TextView mTvTime5;
    @BindView(R.id.tv_time6)
    TextView mTvTime6;
    @BindView(R.id.tv_time7)
    TextView mTvTime7;
    @BindView(R.id.tv_time8)
    TextView mTvTime8;
    @BindView(R.id.tv_time9)
    TextView mTvTime9;
    @BindView(R.id.tv_time10)
    TextView mTvTime10;
    @BindView(R.id.tv_time11)
    TextView mTvTime11;
    @BindView(R.id.tv_time12)
    TextView mTvTime12;
    @BindView(R.id.tv_time13)
    TextView mTvTime13;
    @BindView(R.id.tv_time14)
    TextView mTvTime14;
    @BindView(R.id.tv_time15)
    TextView mTvTime15;
    @BindView(R.id.tv_time16)
    TextView mTvTime16;
    @BindView(R.id.tv_time17)
    TextView mTvTime17;
    @BindView(R.id.tv_time18)
    TextView mTvTime18;
    @BindView(R.id.tv_time19)
    TextView mTvTime19;
    @BindView(R.id.tv_time20)
    TextView mTvTime20;
    @BindView(R.id.tv_time21)
    TextView mTvTime21;
    @BindView(R.id.tv_time22)
    TextView mTvTime22;
    @BindView(R.id.tv_time23)
    TextView mTvTime23;
    @BindView(R.id.tv_time24)
    TextView mTvTime24;
    @BindView(R.id.tv_time25)
    TextView mTvTime25;
    @BindView(R.id.tv_time26)
    TextView mTvTime26;
    @BindView(R.id.tv_time27)
    TextView mTvTime27;
    @BindView(R.id.tv_time28)
    TextView mTvTime28;
    @BindView(R.id.tv_time29)
    TextView mTvTime29;
    @BindView(R.id.tv_time30)
    TextView mTvTime30;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.rv_date)
    CustomWheelRecyclerView mRecyclerView;
    private Map<Integer, TextView> mTimeViewMap = new HashMap<>();
    private Map<Integer, TextView> AvailableTimeViewMap = new HashMap<>();
    private List<Integer> invalidTimeViewPositions = new ArrayList<>();
    private ArrayList<ConferenceReserveTimeEntity> dates;
    private int currentPos = 0;
    ReserveDateAdapter adapter;
    private String roomId;
    private long startDate;
    private ConferenceRoom mRoom;
    Calendar calendar;
    private int pastTimeNum;
    private int width;
    private int checkedPosition = -1;
    List<Integer> updatePoints;
    private int updateStart;
    private int updateEnd;
    private int updateCheckedPosition;
    private boolean isConflict;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_reserve_time;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        initData();
        initRecyclerView();
        putTimeView();
        setCurrentView();
    }

    private void setCurrentView() {
        if (checkedPosition > -1) {
            mRecyclerView.scrollToPosition(checkedPosition);
            currentPos = checkedPosition;
        } else if (dates.size() > 2) {
            mRecyclerView.scrollToPosition(1);
            currentPos = 1;
        } else if (dates.size() > 0) {
            currentPos = 0;
        }
        searchReservationOfDate(dates.get(currentPos).date);
    }

    private void initData() {
        mRoom = getIntent().getParcelableExtra(Constant.CONFERENCE_ROOM);
        roomId = mRoom.id;
        dates = new ArrayList<>();
        int n;
        if (mRoom.isUpdate) {//更新预订信息时日期的设置，取今天至30天后及上次预定日期前后15天的交集
            long now = System.currentTimeMillis();
            long oldTime = mRoom.updateConferenceBeginTime;
            if (oldTime - 3600 * 24 * 1000 * 15 > now) {
                startDate = oldTime - 3600 * 24 * 1000 * 15;
                n = TribeDateUtils.getTimeIntervalByDay(now + 3600 * 24 * 1000 * 30, startDate);
            } else {
                startDate = now;
                n = TribeDateUtils.getTimeIntervalByDay(oldTime + 3600 * 24 * 1000 * 15, startDate);
            }
        } else {
            startDate = mRoom.startDate;
            n = TribeDateUtils.getTimeIntervalByDay(mRoom.endDate, startDate);
        }
        for (int i = 0; i <= Math.abs(n); i++) {
            ConferenceReserveTimeEntity entity = new ConferenceReserveTimeEntity(startDate + i * 3600 * 24 * 1000);
            if ((mRoom.updateConferenceBeginTime > 0 || mRoom.conferenceBeginTime > 0)) {
                long currentTime = startDate + i * 3600 * 24 * 1000;
                Calendar c1 = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c1.setTimeInMillis(currentTime);
                c2.setTimeInMillis(mRoom.updateConferenceBeginTime);
                if (mRoom.updateConferenceBeginTime > 0) {
                    if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                        updateCheckedPosition = i;
                        initCalendar(currentTime);
                        long baseSecond = calendar.getTimeInMillis() / 1000 - 1800;
                        updateStart = (int) ((mRoom.updateConferenceBeginTime - baseSecond * 1000) / 1800000);
                        updateEnd = (int) ((mRoom.updateConferenceEndTime - baseSecond * 1000) / 1800000 - 1);
                        if (!(mRoom.conferenceBeginTime > 0)) {
                            entity.start = updateStart;
                            entity.end = updateEnd;
                        }
                    }
                }
                if (mRoom.conferenceBeginTime > 0)
                    c2.setTimeInMillis(mRoom.conferenceBeginTime);
                if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                    checkedPosition = i;
                    initCalendar(currentTime);
                    long baseSecond = calendar.getTimeInMillis() / 1000 - 1800;
                    if (mRoom.conferenceBeginTime > 0) {
                        entity.start = (int) ((mRoom.conferenceBeginTime - baseSecond * 1000) / 1800000);
                        entity.end = (int) ((mRoom.conferenceEndTime - baseSecond * 1000) / 1800000 - 1);
                    }
                    entity.isHaveBegin = entity.end == entity.start;
                    entity.checked = true;
                }
            }
            dates.add(entity);
        }
        markUpdatePoints();
    }

    //标记修改预定时间时传过来的已选时间段
    private void markUpdatePoints() {
        updatePoints = new ArrayList<>();
        for (int i = updateStart; i <= updateEnd; i++) {
            updatePoints.add(i);
        }
    }

    private void initRecyclerView() {
        adapter = new ReserveDateAdapter(R.layout.icon_text, dates);
        initHeadFoot();
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int first = layoutManager.findFirstVisibleItemPosition() - 1;
                if (position == first) {
                    mRecyclerView.smoothScrollBy(-width, 0);
                } else if (position == first + 2 && position < dates.size()) {
                    mRecyclerView.smoothScrollBy(width, 0);
                }
            }
        });
        mRecyclerView.setOnSelectListener(new CustomWheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                currentPos = position;
                searchReservationOfDate(dates.get(currentPos).date);
            }
        });
    }

    private void initHeadFoot() {
        View head = LayoutInflater.from(getCtx()).inflate(R.layout.reserve_date_head, mRecyclerView, false);
        View foot = LayoutInflater.from(getCtx()).inflate(R.layout.reserve_date_head, mRecyclerView, false);
        width = CommonUtils.getScreenWidth(getCtx()) / 3;
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) head.getLayoutParams();
        params.width = width;
        head.setLayoutParams(params);
        RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) foot.getLayoutParams();
        params1.width = width;
        foot.setLayoutParams(params1);
        adapter.addHeaderView(head, 0, LinearLayout.HORIZONTAL);
        adapter.addFooterView(foot, 0, 0);
    }

    private void initCalendar(long date) {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }


    private void initAvailableTimeViewMap(List<Integer> invalidTimeViewPositions) {
        AvailableTimeViewMap.clear();
        isConflict = false;
        for (int i = 0; i < invalidTimeViewPositions.size(); i++) {
            if (dates.get(currentPos).checked) {
                int a = dates.get(currentPos).start;
                int b = dates.get(currentPos).end;
                if (a > b) {
                    int tmp;
                    tmp = a;
                    a = b;
                    b = tmp;
                }
                for (int k = a; k <= b; k++) {
                    if (invalidTimeViewPositions.get(i) == a) {
                        isConflict = true;
                    }
                }
            }
        }
        if (isConflict) {
            for (ConferenceReserveTimeEntity e : dates
                    ) {
                e.start = 0;
                e.end = 0;
                e.checked = false;
                e.isHaveBegin = false;
            }
            adapter.notifyDataSetChanged();
            ToastUtils.ToastMessage(getCtx(), "预定时间冲突，预定状态已重置");
        }
        for (int i : mTimeViewMap.keySet()
                ) {
            setBac(i, R.mipmap.time_bac_white);
            boolean isInvalid = false;
            for (int j = 0; j < invalidTimeViewPositions.size(); j++) {
                if (i == invalidTimeViewPositions.get(j)) {
                    setBac(i, R.mipmap.time_bac_gray);
                    isInvalid = true;
                    break;
                }
            }
            if (!isInvalid) {
                TextView view = mTimeViewMap.get(i);
                AvailableTimeViewMap.put(i, view);
            }
            if (dates.get(currentPos).start > 0 && i == dates.get(currentPos).start) {
                setBac(i, R.mipmap.time_bac_blue);
            }
            if (dates.get(currentPos).end > 0) {
                if (dates.get(currentPos).end > dates.get(currentPos).start && i > dates.get(currentPos).start && i <= dates.get(currentPos).end) {
                    setBac(i, R.mipmap.time_bac_blue);
                } else if (dates.get(currentPos).end < dates.get(currentPos).start && i < dates.get(currentPos).start && i >= dates.get(currentPos).end) {
                    setBac(i, R.mipmap.time_bac_blue);
                }
            }
        }
    }

    private void setBac(int i, int time_bac) {
        mTimeViewMap.get(i).setBackgroundResource(time_bac);
        switch (time_bac) {
            case R.mipmap.time_bac_white:
                mTimeViewMap.get(i).setTextColor(getResources().getColor(R.color.common_dark));
                break;
            case R.mipmap.time_bac_blue:
                mTimeViewMap.get(i).setTextColor(getResources().getColor(R.color.white));
                break;
            case R.mipmap.time_bac_gray:
                mTimeViewMap.get(i).setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                break;
        }
    }

    private void putTimeView() {
        mTimeViewMap.put(1, mTvTime1);
        mTimeViewMap.put(2, mTvTime2);
        mTimeViewMap.put(3, mTvTime3);
        mTimeViewMap.put(4, mTvTime4);
        mTimeViewMap.put(5, mTvTime5);
        mTimeViewMap.put(6, mTvTime6);
        mTimeViewMap.put(7, mTvTime7);
        mTimeViewMap.put(8, mTvTime8);
        mTimeViewMap.put(9, mTvTime9);
        mTimeViewMap.put(10, mTvTime10);
        mTimeViewMap.put(11, mTvTime11);
        mTimeViewMap.put(12, mTvTime12);
        mTimeViewMap.put(13, mTvTime13);
        mTimeViewMap.put(14, mTvTime14);
        mTimeViewMap.put(15, mTvTime15);
        mTimeViewMap.put(16, mTvTime16);
        mTimeViewMap.put(17, mTvTime17);
        mTimeViewMap.put(18, mTvTime18);
        mTimeViewMap.put(19, mTvTime19);
        mTimeViewMap.put(20, mTvTime20);
        mTimeViewMap.put(21, mTvTime21);
        mTimeViewMap.put(22, mTvTime22);
        mTimeViewMap.put(23, mTvTime23);
        mTimeViewMap.put(24, mTvTime24);
        mTimeViewMap.put(25, mTvTime25);
        mTimeViewMap.put(26, mTvTime26);
        mTimeViewMap.put(27, mTvTime27);
        mTimeViewMap.put(28, mTvTime28);
        mTimeViewMap.put(29, mTvTime29);
        mTimeViewMap.put(30, mTvTime30);
    }


    @OnClick({R.id.tv_time1, R.id.tv_time2, R.id.tv_time3, R.id.tv_time4, R.id.tv_time5, R.id.tv_time6, R.id.tv_time7, R.id.tv_time8, R.id.tv_time9, R.id.tv_time10, R.id.tv_time11, R.id.tv_time12, R.id.tv_time13, R.id.tv_time14, R.id.tv_time15, R.id.tv_time16, R.id.tv_time17, R.id.tv_time18, R.id.tv_time19, R.id.tv_time20, R.id.tv_time21, R.id.tv_time22, R.id.tv_time23, R.id.tv_time24, R.id.tv_time25, R.id.tv_time26, R.id.tv_time27, R.id.tv_time28, R.id.tv_time29, R.id.tv_time30, R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_time1:
                setTimeEvent(1);
                break;
            case R.id.tv_time2:
                setTimeEvent(2);
                break;
            case R.id.tv_time3:
                setTimeEvent(3);
                break;
            case R.id.tv_time4:
                setTimeEvent(4);
                break;
            case R.id.tv_time5:
                setTimeEvent(5);
                break;
            case R.id.tv_time6:
                setTimeEvent(6);
                break;
            case R.id.tv_time7:
                setTimeEvent(7);
                break;
            case R.id.tv_time8:
                setTimeEvent(8);
                break;
            case R.id.tv_time9:
                setTimeEvent(9);
                break;
            case R.id.tv_time10:
                setTimeEvent(10);
                break;
            case R.id.tv_time11:
                setTimeEvent(11);
                break;
            case R.id.tv_time12:
                setTimeEvent(12);
                break;
            case R.id.tv_time13:
                setTimeEvent(13);
                break;
            case R.id.tv_time14:
                setTimeEvent(14);
                break;
            case R.id.tv_time15:
                setTimeEvent(15);
                break;
            case R.id.tv_time16:
                setTimeEvent(16);
                break;
            case R.id.tv_time17:
                setTimeEvent(17);
                break;
            case R.id.tv_time18:
                setTimeEvent(18);
                break;
            case R.id.tv_time19:
                setTimeEvent(19);
                break;
            case R.id.tv_time20:
                setTimeEvent(20);
                break;
            case R.id.tv_time21:
                setTimeEvent(21);
                break;
            case R.id.tv_time22:
                setTimeEvent(22);
                break;
            case R.id.tv_time23:
                setTimeEvent(23);
                break;
            case R.id.tv_time24:
                setTimeEvent(24);
                break;
            case R.id.tv_time25:
                setTimeEvent(25);
                break;
            case R.id.tv_time26:
                setTimeEvent(26);
                break;
            case R.id.tv_time27:
                setTimeEvent(27);
                break;
            case R.id.tv_time28:
                setTimeEvent(28);
                break;
            case R.id.tv_time29:
                setTimeEvent(29);
                break;
            case R.id.tv_time30:
                setTimeEvent(30);
                break;
            case R.id.btn_confirm:
                Intent intent = new Intent(this, BoardroomReserveActivity.class);
                boolean isChecked = false;
                long beginTime = 0;
                long endTime = 0;

                for (ConferenceReserveTimeEntity e : dates
                        ) {
                    if (e.checked) {
                        isChecked = true;
                        initCalendar(e.date);
                        long baseSecond = calendar.getTimeInMillis() / 1000 - 1800;
                        int mStart = e.start;
                        int mEnd = e.end;
                        if (mEnd < mStart) {
                            int tmp;
                            tmp = mStart;
                            mStart = mEnd;
                            mEnd = tmp;
                        }
                        beginTime = baseSecond + mStart * 1800;
                        endTime = baseSecond + mEnd * 1800 + 1800;
                    }
                }
                if (isChecked) {
                    intent.putExtra(Constant.BOARDROOM_BEGIN_TIME, beginTime * 1000);
                    intent.putExtra(Constant.BOARDROOM_END_TIME, endTime * 1000);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.ToastMessage(getCtx(), "你还没选择时间");
                }

                break;
        }
    }

    private void setTimeEvent(int position) {
        for (int i = 0; i < invalidTimeViewPositions.size(); i++) {
            if (position == invalidTimeViewPositions.get(i)) return;
        }
        if (!dates.get(currentPos).isHaveBegin) {
            for (int i : AvailableTimeViewMap.keySet()
                    ) {
                setBac(i, R.mipmap.time_bac_white);
            }
            setBac(position, R.mipmap.time_bac_blue);

            for (ConferenceReserveTimeEntity e : dates
                    ) {
                e.checked = false;
                e.start = 0;
                e.end = 0;
                e.isHaveBegin = false;
            }
            dates.get(currentPos).isHaveBegin = true;
            dates.get(currentPos).checked = true;
            dates.get(currentPos).start = position;
            dates.get(currentPos).end = position;

            adapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < invalidTimeViewPositions.size(); i++) {
                if ((position > invalidTimeViewPositions.get(i) && invalidTimeViewPositions.get(i) > dates.get(currentPos).start) ||
                        (position < invalidTimeViewPositions.get(i) && invalidTimeViewPositions.get(i) < dates.get(currentPos).start)) {
                    setBac(dates.get(currentPos).start, R.mipmap.time_bac_white);
                    dates.get(currentPos).start = position;
                    setBac(position, R.mipmap.time_bac_blue);
                    dates.get(currentPos).isHaveBegin = true;
                    return;
                }
            }
            if (position == dates.get(currentPos).start) {
                dates.get(currentPos).isHaveBegin = true;
                return;
            } else if (position > dates.get(currentPos).start) {
                for (int i : AvailableTimeViewMap.keySet()
                        ) {
                    if (i >= dates.get(currentPos).start && i <= position)
                        setBac(i, R.mipmap.time_bac_blue);
                }
            } else {
                for (int i : AvailableTimeViewMap.keySet()
                        ) {
                    if (i <= dates.get(currentPos).start && i >= position)
                        setBac(i, R.mipmap.time_bac_blue);
                }
            }
            dates.get(currentPos).isHaveBegin = false;
            dates.get(currentPos).end = position;
        }
    }

    private void searchReservationOfDate(long date) {
        showLoadingDialog();
        pastTimeNum = 0;
        if (currentPos == 0) {
            long nowTime = System.currentTimeMillis();
            if (nowTime > date) {
                initPastTimeCalendar();
                pastTimeNum = (int) ((nowTime - calendar.getTimeInMillis()) / 1800000);
            }
        }
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).searchReservationOfDate(roomId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ConferenceReservationDateEntity>>() {
                    @Override
                    public void onNext(BaseResponse<ConferenceReservationDateEntity> response) {
                        initInvalidPoints(response.data);
                        initAvailableTimeViewMap(invalidTimeViewPositions);
                    }
                });
    }

    private void initPastTimeCalendar() {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dates.get(currentPos).date);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    private void initInvalidPoints(ConferenceReservationDateEntity data) {
        invalidTimeViewPositions.clear();

        if (data.t08A != null && data.t08A != "") invalidTimeViewPositions.add(1);
        if (data.t08B != null && data.t08B != "") invalidTimeViewPositions.add(2);
        if (data.t09A != null && data.t09A != "") invalidTimeViewPositions.add(3);
        if (data.t09B != null && data.t09B != "") invalidTimeViewPositions.add(4);
        if (data.t10A != null && data.t10A != "") invalidTimeViewPositions.add(5);
        if (data.t10B != null && data.t10B != "") invalidTimeViewPositions.add(6);
        if (data.t11A != null && data.t11A != "") invalidTimeViewPositions.add(7);
        if (data.t11B != null && data.t11B != "") invalidTimeViewPositions.add(8);
        if (data.t12A != null && data.t12A != "") invalidTimeViewPositions.add(9);
        if (data.t12B != null && data.t12B != "") invalidTimeViewPositions.add(10);
        if (data.t13A != null && data.t13A != "") invalidTimeViewPositions.add(11);
        if (data.t13B != null && data.t13B != "") invalidTimeViewPositions.add(12);
        if (data.t14A != null && data.t14A != "") invalidTimeViewPositions.add(13);
        if (data.t14B != null && data.t14B != "") invalidTimeViewPositions.add(14);
        if (data.t15A != null && data.t15A != "") invalidTimeViewPositions.add(15);
        if (data.t15B != null && data.t15B != "") invalidTimeViewPositions.add(16);
        if (data.t16A != null && data.t16A != "") invalidTimeViewPositions.add(17);
        if (data.t16B != null && data.t16B != "") invalidTimeViewPositions.add(18);
        if (data.t17A != null && data.t17A != "") invalidTimeViewPositions.add(19);
        if (data.t17B != null && data.t17B != "") invalidTimeViewPositions.add(20);
        if (data.t18A != null && data.t18A != "") invalidTimeViewPositions.add(21);
        if (data.t18B != null && data.t18B != "") invalidTimeViewPositions.add(22);
        if (data.t19A != null && data.t19A != "") invalidTimeViewPositions.add(23);
        if (data.t19B != null && data.t19B != "") invalidTimeViewPositions.add(24);
        if (data.t20A != null && data.t20A != "") invalidTimeViewPositions.add(25);
        if (data.t20B != null && data.t20B != "") invalidTimeViewPositions.add(26);
        if (data.t21A != null && data.t21A != "") invalidTimeViewPositions.add(27);
        if (data.t21B != null && data.t21B != "") invalidTimeViewPositions.add(28);
        if (data.t22A != null && data.t22A != "") invalidTimeViewPositions.add(29);
        if (data.t22B != null && data.t22B != "") invalidTimeViewPositions.add(30);
        if (currentPos == updateCheckedPosition) {
            invalidTimeViewPositions.removeAll(updatePoints);
        }
        if (pastTimeNum > 0) {
            for (int i = 1; i <= pastTimeNum; i++) {
                invalidTimeViewPositions.add(i);
            }
        }
    }

    private void swap(int a, int b) {
        if (a > b) {
            int tmp;
            tmp = a;
            a = b;
            b = tmp;
        }
    }
}
