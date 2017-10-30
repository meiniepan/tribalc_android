package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.ContactsAdapter;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.bean.RequestBodyBean.BoardroomReserveEntity;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.panel.DatePickPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/10/19.
 */

public class BoardroomReserveActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_theme)
    EditText tvTheme;
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
    @BindView(R.id.tv_reserve_person)
    TextView tvReservePerson;
    @BindView(R.id.space_participant)
    View spacePar;
    @BindView(R.id.btn_next)
    Button btnNext;
    ArrayList<ContactsPersonEntity> contactsData;
    String[][] alertData = {{"0", "无需提醒"}, {"300", "提前5分钟"}, {"900", "提前15分钟"}, {"1800", "提前30分钟"}, {"3600", "提前一小时"}};
    private int alertInt;
    private String alertTime;
    private long beginTime;
    private long endTime;
private ConferenceRoom mConferenceRoom;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_reserve;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mConferenceRoom = getIntent().getParcelableExtra(Constant.CONFERENCE_ROOM);
        String reserveName = TribeApplication.getInstance().getUserInfo().getName();
        String reservePhone = TribeApplication.getInstance().getUserInfo().getPhone();
        tvReservePerson.setText(reserveName + "  " + reservePhone);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvAlert.setOnClickListener(this);
        tvMoreContacts.setOnClickListener(this);
        tvAddParticipant.setOnClickListener(this);
        btnNext.setOnClickListener(this);
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
            List tmpData;
            if (contactsData.size() > 3) tmpData = contactsData.subList(0, 3);
            else tmpData = contactsData;
            ContactsAdapter adapter = new ContactsAdapter(R.layout.contacts_show_item, tmpData);
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
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE_BOARDROOM_RESERVE_TIME);
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
                startActivity(intent);
                break;
            case R.id.btn_next:
                createReserveInfo();
                break;
            default:
                break;
        }
    }

    private void createReserveInfo() {
        BoardroomReserveEntity entity = new BoardroomReserveEntity();
        entity.attendance = contactsData.size();
        entity.conferenceBeginTime = beginTime;
        entity.conferenceEndTime = endTime;
        entity.reminderTime = alertTime;
        entity.subject = tvTheme.getText().toString();
        entity.conferenceParticipants = contactsData;
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).createReserveInfo(mConferenceRoom.id,entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        Toast.makeText(BoardroomReserveActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    }
                });
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
                alertInt = data.getIntExtra(Constant.BOARDROOM_ALERT_TIME, 1);
                alertTime = alertData[alertInt][0];
                tvAlert.setText(alertData[alertInt][1]);
            } else if (requestCode == Constant.ForIntent.REQUEST_CODE_BOARDROOM_RESERVE_TIME) {
                beginTime = data.getLongExtra(Constant.BOARDROOM_BEGIN_TIME, 1);
                endTime = data.getLongExtra(Constant.BOARDROOM_END_TIME, 1);
            }
        }
    }
}
