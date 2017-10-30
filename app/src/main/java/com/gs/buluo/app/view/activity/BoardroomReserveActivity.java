package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.ContactsPersonEntity;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ContactsAdapter;
import com.gs.buluo.app.view.widget.panel.DatePickPanel;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.rv_contacts)
    RecyclerView rvContacts;
    @BindView(R.id.tv_more_contacts)
    TextView tvMoreContacts;
    @BindView(R.id.tv_add_participant)
    TextView tvAddParticipant;
    @BindView(R.id.space_participant)
    View spacePar;
    ArrayList<ContactsPersonEntity> contactsData;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_reserve;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvAlert.setOnClickListener(this);
        tvMoreContacts.setOnClickListener(this);
        tvAddParticipant.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        contactsData = getIntent().getParcelableArrayListExtra(Constant.CONTACTS_DATA);
        if (contactsData != null) {
            initParticipant();
        }
    }

    private void initParticipant() {
        if (contactsData.size() == 0) {
            tvAddParticipant.setVisibility(View.VISIBLE);
            spacePar.setVisibility(View.VISIBLE);
            rvContacts.setVisibility(View.GONE);
            tvMoreContacts.setVisibility(View.GONE);
        } else {
            spacePar.setVisibility(View.GONE);
            tvAddParticipant.setVisibility(View.GONE);
            rvContacts.setVisibility(View.VISIBLE);
            tvMoreContacts.setVisibility(View.VISIBLE);
            rvContacts.setLayoutManager(new LinearLayoutManager(getCtx()));
            ContactsAdapter adapter = new ContactsAdapter(R.layout.contacts_show_item, contactsData);
            rvContacts.setAdapter(adapter);
        }
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
            case R.id.tv_more_contacts:
                intent.setClass(BoardroomReserveActivity.this, BoardroomParticipantActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, contactsData);
                startActivity(intent);
                break;
            case R.id.tv_alert:
                intent.setClass(BoardroomReserveActivity.this, BoardroomAlertActivity.class);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE_BOARDROOM_ALERT);
                break;
            case R.id.tv_add_participant:
                intent.setClass(BoardroomReserveActivity.this, BoardroomParticipantNoneActivity.class);
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
