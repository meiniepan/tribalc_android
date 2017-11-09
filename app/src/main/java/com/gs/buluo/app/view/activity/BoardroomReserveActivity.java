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
import com.gs.buluo.app.bean.RequestBodyBean.ConferenceReserveEntity;
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
        if (mConferenceRoom.isUpdate) {
            StringBuilder reserveTime = new StringBuilder().append(TribeDateUtils.dateFormat(new Date(mConferenceRoom.updateConferenceBeginTime)))
                    .append("-").append(TribeDateUtils.dateFormat6(new Date(mConferenceRoom.updateConferenceEndTime)));
            tvTime.setText(reserveTime);
            tvTheme.setText(mConferenceRoom.subject);
            for (int i = 0; i < alertData.length; i++) {
                if ((mConferenceRoom.reminderTime + "") .equals(alertData[i][0]) ) {
                    tvAlert.setText(alertData[i][1]);
                    alertTime = mConferenceRoom.reminderTime + "";
                }
            }
            contactsData = mConferenceRoom.conferenceParticipants;
            initParticipant();
        }
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
                intent.putExtra(Constant.CONFERENCE_ROOM, mConferenceRoom);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE_BOARDROOM_RESERVE_TIME);
                break;
            case R.id.tv_more_contacts:
            case R.id.tv_add_participant:
                intent.setClass(BoardroomReserveActivity.this, BoardroomParticipantActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, contactsData);
                startActivityForResult(intent, Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT);
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
        ConferenceReserveEntity entity = new ConferenceReserveEntity();
        entity.attendance = contactsData.size();
        entity.conferenceBeginTime = mConferenceRoom.conferenceBeginTime;
        entity.conferenceEndTime = mConferenceRoom.conferenceEndTime;
        entity.reminderTime = alertTime;
        entity.subject = tvTheme.getText().toString();
        entity.conferenceParticipants = contactsData;
        showLoadingDialog();
        if (mConferenceRoom.isUpdate)
            updateReserveInfo(entity);
        else createReserveInfo(entity);
    }

    private void createReserveInfo(ConferenceReserveEntity entity) {
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).createReserveInfo(mConferenceRoom.id, entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), "预订成功");
                        Intent intent = new Intent(getCtx(), BoardroomRecordActivity.class);
                        intent.putExtra(Constant.BOARD_RESERVE_FLAG, true);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void updateReserveInfo(ConferenceReserveEntity entity) {
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).updateReservation(mConferenceRoom.reservationId, entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), "修改成功");
                        Intent intent = new Intent(getCtx(), BoardroomRecordDetailActivity.class);
                        intent.putExtra(Constant.BOARD_RESERVE_ID, mConferenceRoom.reservationId);
                        startActivity(intent);
                        finish();
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
                mConferenceRoom.conferenceBeginTime = data.getLongExtra(Constant.BOARDROOM_BEGIN_TIME, 1);
                mConferenceRoom.conferenceEndTime = data.getLongExtra(Constant.BOARDROOM_END_TIME, 1);
                setTvReserveTime();
                Float totalFee = new Float(mConferenceRoom.conferenceEndTime - mConferenceRoom.conferenceBeginTime) / 3600 / 1000 * new Float(mConferenceRoom.fee);
                tvFee.setText( "¥"+totalFee);
            } else if (requestCode == Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT) {
                contactsData = data.getParcelableArrayListExtra(Constant.CONTACTS_DATA);
                initParticipant();
            }
        }
    }

    private void setTvReserveTime() {
        StringBuilder reserveTime = new StringBuilder().append(TribeDateUtils.dateFormat(new Date(mConferenceRoom.conferenceBeginTime)))
                .append("-").append(TribeDateUtils.dateFormat6(new Date(mConferenceRoom.conferenceEndTime)));
        tvTime.setText(reserveTime);
    }
}
