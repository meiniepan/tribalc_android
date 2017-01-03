package com.gs.buluo.app.view.widget.panel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.platform.comapi.map.A;
import com.facebook.common.time.Clock;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.widget.wheel.OnWheelChangedListener;
import com.gs.buluo.app.view.widget.wheel.WheelView;
import com.gs.buluo.app.view.widget.wheel.adapters.ArrayWheelAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/12/1.
 */
public class DatePickerPanel extends Dialog {

    private OnSelectedFinished onSelectedFinished;
    private View rootView;
    private Activity mActivity;
    private WheelView mDate;
    private WheelView mTime;
    private TextView mBtnConfirm;
    private List<Long> timeList;
    private List<Long> dateList;
    private long newDate;
    private long newTime;

    public DatePickerPanel(Activity context, OnSelectedFinished onSelectedFinished) {
        super(context, R.style.my_dialog);
        mActivity = context;
        this.onSelectedFinished = onSelectedFinished;
        initView();
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void setUpData() {
        initDateData();
        initTimeData();

        mDate.setVisibleItems(5);
        mTime.setVisibleItems(5);
    }

    private void initTimeData() {
        newTime = 3 * 3600 * 1000;
        List<String> list = new ArrayList<>();
        for (int i = 11; i < 23; i++) {
            list.add(i + ":00");
            list.add(i + ":30");
        }
        long time = 3 * 3600 * 1000;
        timeList = new ArrayList<>();
        timeList.add(time);
        for (int i = 1; i <= 12; i++) {
            timeList.add(time + 1800 * 1000 * i);
            timeList.add(time + 3600 * 1000 * i);
        }
        mTime.setViewAdapter(new ArrayWheelAdapter<>(mActivity, list.toArray()));
    }

    private void initDateData() {
        List<String> list = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        dateList = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        String c = TribeDateUtils.dateFormat5(date);
        String[] arr = c.split("-");
        instance.set(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])-1, Integer.parseInt(arr[2]),8,0,0);
        long entityTime = instance.getTimeInMillis();//获取当日的8点时间
        newDate =entityTime;
        String d;
        int w;
        String wwk;

        instance.setTime(date);
        for (int i = 0; i < 7; i++) {
            date = new Date(entityTime);
            instance.setTime(date);
            w = instance.get(Calendar.DAY_OF_WEEK);
            wwk = switchToCh(w);
            d = TribeDateUtils.dateFormat2(date);
            dateList.add(entityTime);
            list.add(d + " " + wwk);

            entityTime += 86400000;
        }
        mDate.setViewAdapter(new ArrayWheelAdapter<>(mActivity, list.toArray()));
    }

    private String switchToCh(int w) {
        if (w == 1) {
            return "周一";
        } else if (w == 2) {
            return "周二";
        } else if (w == 3) {
            return "周三";
        } else if (w == 4) {
            return "周四";
        } else if (w == 5) {
            return "周五";
        } else if (w == 6) {
            return "周六";
        } else {
            return "周日";
        }
    }

    private void setUpListener() {
        mDate.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                newDate = dateList.get(mDate.getCurrentItem());
            }
        });
        mTime.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                newTime = timeList.get(mTime.getCurrentItem());
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date=new Date(newDate);
                Date date1=new Date(newTime);

                onSelectedFinished.onSelected(newDate + newTime);
                dismiss();
            }
        });
    }

    private void setUpViews() {
        mDate = (WheelView) rootView.findViewById(R.id.id_date);
        mTime = (WheelView) rootView.findViewById(R.id.id_time);
        mBtnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
    }

    private void initView() {
        rootView = LayoutInflater.from(mActivity).inflate(R.layout.time_picker_board, null);
        setContentView(rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        ButterKnife.bind(this, rootView);
    }


    public interface OnSelectedFinished {
        void onSelected(long time);
    }
}
