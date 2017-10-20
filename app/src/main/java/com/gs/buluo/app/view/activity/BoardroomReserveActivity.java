package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.panel.DatePickPanel;

import butterknife.BindView;

/**
 * Created by Solang on 2017/10/19.
 */

public class BoardroomReserveActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_alert)
    TextView tvAlert;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_reserve;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvAlert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_date:
                initDatePickPanel(tvDate);
                break;
            case R.id.tv_time:
                intent.setClass(BoardroomReserveActivity.this, ReserveTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_alert:
                intent.setClass(BoardroomReserveActivity.this, BoardroomAlertActivity.class);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE_BOARDROOM_ALERT);
                break;
            default:
                break;
        }
    }

    private void initDatePickPanel(final TextView birthday) {
        DatePickPanel pickPanel = new DatePickPanel(this, new DatePickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(int year, int month, int day) {
                StringBuilder sb = new StringBuilder();
                sb.append(year).append("年").append(month).append("月").append(day).append("日");
                birthday.setText(sb.toString());
            }
        });
        pickPanel.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.ForIntent.REQUEST_CODE_BOARDROOM_ALERT) {
                tvAlert.setText(data.getStringExtra(Constant.BOARDROOM_ALERT_TIME));
            }
        }
    }
}
