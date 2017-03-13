package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.RequestBodyBean.LockRequest;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.widget.panel.DoubleTimePicker;
import com.gs.buluo.app.view.widget.panel.SimpleChoosePanel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import retrofit2.Response;

/**
 * Created by hjn on 2017/3/10.
 */

public class AddVisitorActivity extends BaseActivity implements View.OnClickListener, DoubleTimePicker.OnSelectedFinished {
    @Bind(R.id.info_time_start)
    TextView tvStart;
    @Bind(R.id.info_time_finish)
    TextView tvFinish;
    @Bind(R.id.add_visitor_choose_door)
    TextView tvDoor;
    @Bind(R.id.add_visitor_name)
    EditText etName;
    @Bind(R.id.add_visitor_phone)
    EditText etPhone;


    private long date;
    private int year;
    private String beginTime;
    private long endTime;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        date = System.currentTimeMillis();
        beginTime = TribeDateUtils.dateFormat10(date);
        tvStart.setText(beginTime);      //01月01日12时

        findViewById(R.id.add_visitor_choose_door).setOnClickListener(this);
        findViewById(R.id.add_visitor_finish).setOnClickListener(this);
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        long newDate = date;
        year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        ArrayList<String> dayList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.setTime(new Date(newDate + i * 24 * 3600 * 1000));
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            dayList.add(month + "月" + day + "日");
        }
        ArrayList<String> hourList = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            hourList.add(i + "时");
        }

        new DoubleTimePicker.Builder<String>(this, this)
                .setFirstData(dayList)
                .setSecondData(hourList)
                .build();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_visitor;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_visitor_choose_door:
                initChoosePanel();
                break;
            case R.id.add_visitor_finish:
                if (tvDoor.getText().toString().contains("请选择") || etName.length() == 0 || etPhone.length() == 0 || tvFinish.getText().toString().contains("结束时间")) {
                    ToastUtils.ToastMessage(getCtx(), R.string.not_empty);
                    return;
                }
                createVisitor();
                break;
        }
    }

    private void createVisitor() {
        LockRequest request = new LockRequest();
        request.beginTime = Long.parseLong(beginTime);
        request.endTime = endTime;
        request.equipId = "";
        request.name = etName.getText().toString().trim();
        request.phone = etPhone.getText().toString().trim();
        TribeRetrofit.getInstance().createApi(DoorApis.class).getLockKey(TribeApplication.getInstance().getUserInfo().getId(), request)
                .enqueue(new TribeCallback<LockKey>() {
            @Override
            public void onSuccess(Response<BaseResponse<LockKey>> response) {
                openDoor(response.body().data);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<LockKey> body) {
                ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
            }
        });
    }

    private void openDoor(LockKey data) {
        Intent intent = new Intent(getCtx(), OpenDoorActivity.class);
        startActivity(intent);
        finish();
    }

    private void initChoosePanel() {
        SimpleChoosePanel.Builder<String> builder = new SimpleChoosePanel.Builder<>(this, new SimpleChoosePanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                tvDoor.setText(string);
            }
        });
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            list.add("大门" + i);
        }
        SimpleChoosePanel simpleChoosePanel = builder.setData(list).setTitle("请选择门锁").build();
        simpleChoosePanel.show();
    }

    @Override
    public void onSelected(String result) {
        String[] arr = result.split("-");
        String[] arr1 = arr[0].split("月");
        String month = arr1[0];
        String day = arr1[1].split("日")[0];

        String hour = arr[1].substring(0, arr[1].length() - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(hour), 0);
        long time = calendar.getTimeInMillis();
        if (time < date) {
            ToastUtils.ToastMessage(getCtx(), "结束时间需大于当前时间");
            return;
        }
        endTime = time;
        tvFinish.setText(TribeDateUtils.dateFormat10(time));
    }
}
