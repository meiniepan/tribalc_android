package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.BaseActivity;
import com.gs.buluo.common.widget.wheel.WheelView;
import com.gs.buluo.common.widget.wheel.adapters.ArrayWheelAdapter;

import butterknife.ButterKnife;

/**
 * Created by Solang on 2017/6/16.
 */

public class DatePickPanel extends Dialog implements View.OnClickListener {

    private final BaseActivity mActivity;
    private WheelView mViewYear;
    private WheelView mViewMonth;
    private WheelView mViewDay;
    private TextView mBtnConfirm;
    private OnSelectedFinished onSelectedFinished;
    /**
     * 初始化日期
     */
    protected String[] mYears = new String[200];
    protected String[] mMonths = new String[12];
    protected String[] mDays = new String[31];

    private View rootView;

    public DatePickPanel(BaseActivity activity, OnSelectedFinished onSelectedFinished) {
        super(activity, R.style.my_dialog);
        mActivity = activity;
        this.onSelectedFinished = onSelectedFinished;
        initData();
        initView();
        setUpViews();
        setUpData();
    }

    private void initData() {
        for (int i = 0; i < 200; i++) {
            mYears[i] = 1900 + i + "";
        }
        for (int i = 0; i < 12; i++) {
            mMonths[i] = 1 + i + "";
        }
        for (int i = 0; i < 31; i++) {
            mDays[i] = 1 + i + "";
        }
    }

    private void initView() {
        rootView = LayoutInflater.from(mActivity).inflate(R.layout.picker_board, null);
        setContentView(rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                onSelectedFinished.onSelected(Integer.parseInt(mYears[mViewYear.getCurrentItem()]),
                        Integer.parseInt(mMonths[mViewMonth.getCurrentItem()]),
                        Integer.parseInt(mDays[mViewDay.getCurrentItem()])
                        );
                dismiss();
                break;
        }
    }

    private void setUpViews() {
        mViewYear = (WheelView) rootView.findViewById(R.id.id_province);
        mViewMonth = (WheelView) rootView.findViewById(R.id.id_city);
        mViewDay = (WheelView) rootView.findViewById(R.id.id_district);
        mBtnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);
        ((TextView)rootView.findViewById(R.id.tv_choose_info)).setText(R.string.choose_date);
    }


    private void setUpData() {
        mViewYear.setViewAdapter(new ArrayWheelAdapter<>(mActivity, mYears));
        mViewMonth.setViewAdapter(new ArrayWheelAdapter<>(mActivity, mMonths));
        mViewDay.setViewAdapter(new ArrayWheelAdapter<>(mActivity, mDays));
        mViewYear.setCurrentItem(70);
        mViewYear.setVisibleItems(5);
        mViewMonth.setVisibleItems(5);
        mViewDay.setVisibleItems(5);

    }


    public interface OnSelectedFinished {
        void onSelected(int year,int month,int day);
    }
}
