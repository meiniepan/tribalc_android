package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.SignRecordResponse;
import com.gs.buluo.app.bean.SignRecord;
import com.gs.buluo.app.bean.SignResponse;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.sch.calendar.CalendarView;
import com.sch.calendar.adapter.VagueAdapter;
import com.sch.calendar.annotation.DayOfMonth;
import com.sch.calendar.annotation.Month;
import com.sch.calendar.entity.Date;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/5/19.
 */

public class SignActivity extends BaseActivity {
    @Bind(R.id.calendar_view)
    CalendarView calendarView;
    @Bind(R.id.sign_icon)
    SimpleDraweeView icon;
    @Bind(R.id.sign_continuation)
    TextView tvContinuation;
    @Bind(R.id.sign_name)
    TextView tvName;
    @Bind(R.id.sign_up)
    Button btSign;
    private MyVagueAdapter vagueAdapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        FresoUtils.loadImage(TribeApplication.getInstance().getUserInfo().getPicture(), icon);
        tvName.setText(TribeApplication.getInstance().getUserInfo().getNickname());
        getData();
        initCalendarView();
    }

    private void initCalendarView() {
        calendarView.setCanDrag(false); // 不可拖动
        calendarView.setScaleEnable(false); // 可伸缩
        calendarView.setLastMonthButtonVisibility(View.GONE);
        calendarView.setNextMonthButtonVisibility(View.GONE);
        calendarView.setShowOverflowDate(false); // 不显示溢出的日期

        // 数据适配器
        vagueAdapter = new MyVagueAdapter(R.layout.layout_checkin_calendar_item);
        calendarView.setVagueAdapter(vagueAdapter);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign;
    }

    public void getData() {
        TribeRetrofit.getInstance().createApi(MainApis.class).getSignRecord(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<SignRecordResponse>>() {
                    @Override
                    public void onNext(BaseResponse<SignRecordResponse> response) {
                        setData(response.data);
                    }
                });
    }

    public void setData(SignRecordResponse data) {
        tvContinuation.setText(data.continuityDays);
        vagueAdapter.setData(data.monthRecords);
        vagueAdapter.notifyDataSetChanged();
    }

    private class MyVagueAdapter extends VagueAdapter<List<SignRecord>> {

        MyVagueAdapter(@LayoutRes int dayLayout) {
            super(dayLayout);
        }

        @Override
        public void onBindVague(View itemView, int year, @Month int month, @DayOfMonth int dayOfMonth) {
            TextView ivCheckinAlready = (TextView) itemView.findViewById(R.id.tv_day_of_month);
            if (data == null) return;
            for (int i = 0; i < data.size(); i++) {
                if (dayOfMonth == data.get(i).dayNumber) {
                    ivCheckinAlready.setBackgroundResource(R.mipmap.sign_already);
                }
            }
        }

        @Override
        public void flagToday(View todayView) {
            TextView tvDayView = (TextView) todayView.findViewById(R.id.tv_day_of_month);
            tvDayView.setTextColor(getResources().getColor(R.color.white));
            String currentTime = TribeDateUtils.dateFormat5(new java.util.Date(System.currentTimeMillis()));
            String lastTime = SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).getStringValue(Constant.SIGN_IN);
            if (TextUtils.equals(currentTime, lastTime)) {
                tvDayView.setBackgroundResource(R.mipmap.sign_today);
            } else {
                tvDayView.setBackgroundColor(getResources().getColor(R.color.custom_sign));
            }
        }

        @Override
        public void flagNotToday(View dayView, Date date) {
            TextView tvDayView = (TextView) dayView.findViewById(R.id.tv_day_of_month);
            tvDayView.setTextColor(getResources().getColor(R.color.common_dark));
        }
    }

    private String lastTime;
    @Override
    public void onResume() {
        super.onResume();
        String currentTime = TribeDateUtils.dateFormat5(new java.util.Date(System.currentTimeMillis()));
        if (lastTime == null) {
            lastTime = SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).getStringValue(Constant.SIGN_IN);
        }
        if (TextUtils.equals(currentTime, lastTime)) {
            btSign.setText(R.string.already_sign_in);
            btSign.setEnabled(false);
        } else {
            btSign.setText(R.string.sign_in);
            btSign.setEnabled(true);
        }
    }

    public void signUp(View view) {
        btSign.setText(R.string.signing_in);
        TribeRetrofit.getInstance().createApi(MainApis.class).signIn(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<BaseResponse<SignResponse>>() {
                    @Override
                    public void call(BaseResponse<SignResponse> baseResponse) {
                        lastTime = TribeDateUtils.dateFormat5(new java.util.Date(baseResponse.data.lastTimestamp));
                        SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).setValue(Constant.SIGN_IN, lastTime);
                        getData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        btSign.setText(R.string.already_sign_in);
                        btSign.setEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        btSign.setText(R.string.sign_in);
                    }
                });
    }
}
