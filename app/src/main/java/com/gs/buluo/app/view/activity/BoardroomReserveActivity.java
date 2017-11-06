package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.ContactsAdapter;
import com.gs.buluo.app.bean.ConferenceEquipment;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.bean.RequestBodyBean.BoardroomReserveEntity;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.ArrayList;
import java.util.Date;
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
    @BindView(R.id.tv_open_time)
    TextView tvOpenTime;
    @BindView(R.id.tv_config)
    TextView tvConfig;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_capacity)
    TextView tvCapacity;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_reserve_person)
    TextView tvReservePerson;
    @BindView(R.id.space_participant)
    View spacePar;
    @BindView(R.id.btn_next)
    Button btnNext;
    ArrayList<ContactsPersonEntity> contactsData = new ArrayList<>();
    String[][] alertData = {{"0", "无需提醒"}, {"300", "提前5分钟"}, {"900", "提前15分钟"}, {"1800", "提前30分钟"}, {"3600", "提前一小时"}};
    private int alertInt;
    private String alertTime = "0";
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
        String companyName = TribeApplication.getInstance().getUserInfo().getCompanyName();
        StringBuilder openTime = new StringBuilder().append(mConferenceRoom.openTime / 3600).append(":00-").append(mConferenceRoom.closeTime / 3600).append(":00 开放");
        StringBuilder config = new StringBuilder();
        for (ConferenceEquipment e : mConferenceRoom.equipments
                ) {
            config.append(e.name).append("  ");
        }
        StringBuilder capacity = new StringBuilder().append("可容纳").append(mConferenceRoom.galleryful).append("-").append(mConferenceRoom.maxGalleryful).append("人");
        StringBuilder fee = new StringBuilder("¥").append(mConferenceRoom.fee);
        tvName.setText(mConferenceRoom.name);
        tvOpenTime.setText(openTime);
        tvConfig.setText(config);
        tvCapacity.setText(capacity);
        tvCompanyName.setText(companyName);
        tvReservePerson.setText(reserveName + "  " + reservePhone);
        tvFee.setText(fee);
        tvTime.setOnClickListener(this);
        tvAlert.setOnClickListener(this);
        tvMoreContacts.setOnClickListener(this);
        tvAddParticipant.setOnClickListener(this);
        btnNext.setOnClickListener(this);
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
            case R.id.tv_time:
                intent.setClass(BoardroomReserveActivity.this, ReserveTimeActivity.class);
                intent.putExtra(Constant.CONFERENCE_ROOM,mConferenceRoom);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE_BOARDROOM_RESERVE_TIME);
                break;
            case R.id.tv_more_contacts:
            case R.id.tv_add_participant:
                intent.setClass(BoardroomReserveActivity.this, BoardroomParticipantActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, contactsData);
                startActivityForResult(intent,Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT);
                break;
            case R.id.tv_alert:
                intent.setClass(BoardroomReserveActivity.this, BoardroomAlertActivity.class);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE_BOARDROOM_ALERT);
                break;
            case R.id.btn_next:
                checkInputInfo();
                break;
            default:
                break;
        }
    }

    private void checkInputInfo() {
        if (contactsData == null || contactsData.size() == 0) {
            ToastUtils.ToastMessage(getCtx(), "请添加参会人");
            return;
        }
        createReserveInfo();
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
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).createReserveInfo(mConferenceRoom.id, entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(),"预订成功");
                        startActivity(new Intent(getCtx(), BoardroomRecordActivity.class));
                    }
                });
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
                StringBuilder reserveTime = new StringBuilder().append(TribeDateUtils.dateFormat(new Date(beginTime)))
                        .append("-").append(TribeDateUtils.dateFormat6(new Date(endTime)));
                tvTime.setText(reserveTime);
            }else if (requestCode == Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT) {
                contactsData = data.getParcelableArrayListExtra(Constant.CONTACTS_DATA);
                    initParticipant();
            }
        }
    }
}
