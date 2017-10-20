package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BoardroomReserveTimeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2017/10/19.
 */

public class ReserveTimeActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_date_check1)
    View mTvDateCheck1;
    @BindView(R.id.tv_date1)
    TextView mTvDate1;
    @BindView(R.id.tv_date_check2)
    View mTvDateCheck2;
    @BindView(R.id.tv_date2)
    TextView mTvDate2;
    @BindView(R.id.tv_date_check3)
    View mTvDateCheck3;
    @BindView(R.id.tv_date3)
    TextView mTvDate3;
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
    private boolean isHaveBegin;
    private Map<Integer, TextView> mTimeViewMap = new HashMap<>();
    private Map<Integer, TextView> AvailableTimeViewMap = new HashMap<>();
    private int beginPosition;
    private int[] invalidTimeViewPositions;
    private ArrayList<BoardroomReserveTimeEntity> dates;
    private int middleDatePosition = 1;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_reserve_time;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        dates = new ArrayList<>();
        int[] invalidTimeViewPositions1 = new int[]{3, 8, 20};
        int[] invalidTimeViewPositions2 = new int[]{3};
        int[] invalidTimeViewPositions3 = new int[]{20};
        int[] invalidTimeViewPositions4 = new int[]{8, 20};
        BoardroomReserveTimeEntity entity1 = new BoardroomReserveTimeEntity();
        entity1.invalidPositions = invalidTimeViewPositions1;
        entity1.date = "2017-10-10";
        BoardroomReserveTimeEntity entity2 = new BoardroomReserveTimeEntity();
        entity2.invalidPositions = invalidTimeViewPositions2;
        entity2.date = "2017-10-11";
        BoardroomReserveTimeEntity entity3 = new BoardroomReserveTimeEntity();
        entity3.invalidPositions = invalidTimeViewPositions3;
        entity3.date = "2017-10-12";
        BoardroomReserveTimeEntity entity4 = new BoardroomReserveTimeEntity();
        entity4.invalidPositions = invalidTimeViewPositions4;
        entity4.date = "2017-10-13";
        entity4.checked = true;
        dates.add(entity1);
        dates.add(entity2);
        dates.add(entity3);
        dates.add(entity4);
        mTvDate1.setText(dates.get(middleDatePosition-1).date);
        setViewVisible(mTvDateCheck1,dates.get(middleDatePosition-1).checked);
        mTvDate2.setText(dates.get(middleDatePosition).date);
        setViewVisible(mTvDateCheck2,dates.get(middleDatePosition).checked);
        mTvDate3.setText(dates.get(middleDatePosition+1).date);
        setViewVisible(mTvDateCheck3,dates.get(middleDatePosition+1).checked);
        putTimeView();
        initAvailableTimeViewMap(dates.get(middleDatePosition).invalidPositions);

    }

    private void initAvailableTimeViewMap(int[] invalidTimeViewPositions) {
        AvailableTimeViewMap.clear();
        this.invalidTimeViewPositions = invalidTimeViewPositions;
        for (int i : mTimeViewMap.keySet()
                ) {
            mTimeViewMap.get(i).setBackgroundResource(R.mipmap.time_bac_white);
            boolean isInvalid = false;
            for (int j = 0; j < invalidTimeViewPositions.length; j++) {
                if (i == invalidTimeViewPositions[j]) {
                    mTimeViewMap.get(i).setBackgroundResource(R.mipmap.time_bac_gray);
                    isInvalid = true;
                    break;
                }
            }
            if (!isInvalid) {
                TextView view = mTimeViewMap.get(i);
                AvailableTimeViewMap.put(i, view);
            }
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


    @OnClick({R.id.tv_date_check1, R.id.tv_date1, R.id.tv_date_check2, R.id.tv_date2, R.id.tv_date_check3, R.id.tv_date3, R.id.tv_time1, R.id.tv_time2, R.id.tv_time3, R.id.tv_time4, R.id.tv_time5, R.id.tv_time6, R.id.tv_time7, R.id.tv_time8, R.id.tv_time9, R.id.tv_time10, R.id.tv_time11, R.id.tv_time12, R.id.tv_time13, R.id.tv_time14, R.id.tv_time15, R.id.tv_time16, R.id.tv_time17, R.id.tv_time18, R.id.tv_time19, R.id.tv_time20, R.id.tv_time21, R.id.tv_time22, R.id.tv_time23, R.id.tv_time24, R.id.tv_time25, R.id.tv_time26, R.id.tv_time27, R.id.tv_time28, R.id.tv_time29, R.id.tv_time30, R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date1:
                if (middleDatePosition == 0) {
                    Toast.makeText(this, "没有更早的了", Toast.LENGTH_SHORT).show();
                    return;
                }
                middleDatePosition = middleDatePosition - 1;
                initAvailableTimeViewMap(dates.get(middleDatePosition).invalidPositions);
                if (middleDatePosition == 0){
                    mTvDate1.setText("没有了");
                }else {
                    mTvDate1.setText(dates.get(middleDatePosition-1).date);
                    setViewVisible(mTvDateCheck1,dates.get(middleDatePosition-1).checked);
                }
                mTvDate2.setText(dates.get(middleDatePosition).date);
                setViewVisible(mTvDateCheck2,dates.get(middleDatePosition).checked);
                mTvDate3.setText(dates.get(middleDatePosition+1).date);
                setViewVisible(mTvDateCheck3,dates.get(middleDatePosition+1).checked);
                break;
            case R.id.tv_date2:
                break;
            case R.id.tv_date3:
                if (middleDatePosition == dates.size() - 1) {
                    Toast.makeText(this, "没有更晚的了", Toast.LENGTH_SHORT).show();
                    return;
                }
                middleDatePosition = middleDatePosition + 1;
                initAvailableTimeViewMap(dates.get(middleDatePosition).invalidPositions);
                if (middleDatePosition == dates.size() - 1){
                    mTvDate3.setText("没有了");
                    mTvDateCheck3.setVisibility(View.GONE);
                }else {
                    mTvDate3.setText(dates.get(middleDatePosition+1).date);
                    mTvDateCheck3.setVisibility(View.GONE);
                    setViewVisible(mTvDateCheck3,dates.get(middleDatePosition+1).checked);
                }
                mTvDate2.setText(dates.get(middleDatePosition).date);
                setViewVisible(mTvDateCheck2,dates.get(middleDatePosition).checked);
                mTvDate1.setText(dates.get(middleDatePosition-1).date);
                setViewVisible(mTvDateCheck1,dates.get(middleDatePosition-1).checked);
                break;
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
                break;
        }
    }

    private void setViewVisible(View tvDateCheck3, boolean checked) {
        if (checked){
            tvDateCheck3.setVisibility(View.VISIBLE);
        }else tvDateCheck3.setVisibility(View.GONE);
    }

    private void setTimeEvent(int position) {
        for (int i = 0; i < invalidTimeViewPositions.length; i++) {
            if (position == invalidTimeViewPositions[i]) return;
        }
        if (!isHaveBegin) {
            for (int i : AvailableTimeViewMap.keySet()
                    ) {
                AvailableTimeViewMap.get(i).setBackgroundResource(R.mipmap.time_bac_white);
            }
            beginPosition = position;
            AvailableTimeViewMap.get(position).setBackgroundResource(R.mipmap.time_bac_blue);
            isHaveBegin = true;
        } else {
            for (int i = 0; i < invalidTimeViewPositions.length; i++) {
                if ((position > invalidTimeViewPositions[i] && invalidTimeViewPositions[i] > beginPosition) ||
                        (position < invalidTimeViewPositions[i] && invalidTimeViewPositions[i] < beginPosition)) {
                    AvailableTimeViewMap.get(beginPosition).setBackgroundResource(R.mipmap.time_bac_white);
                    beginPosition = position;
                    AvailableTimeViewMap.get(position).setBackgroundResource(R.mipmap.time_bac_blue);
                    isHaveBegin = true;
                    return;
                }
            }
            if (position == beginPosition) {
                isHaveBegin = true;
                return;
            } else if (position > beginPosition) {
                for (int i : AvailableTimeViewMap.keySet()
                        ) {
                    if (i >= beginPosition && i <= position)
                        AvailableTimeViewMap.get(i).setBackgroundResource(R.mipmap.time_bac_blue);
                }
            } else {
                for (int i : AvailableTimeViewMap.keySet()
                        ) {
                    if (i <= beginPosition && i >= position)
                        AvailableTimeViewMap.get(i).setBackgroundResource(R.mipmap.time_bac_blue);
                }
            }
            isHaveBegin = false;
        }
    }
}
