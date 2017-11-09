package com.gs.buluo.app.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ConferenceEquipment;
import com.gs.buluo.app.bean.ConferenceReservation;
import com.gs.buluo.app.bean.ConferenceReserveDetail;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.panel.SimpleChoosePanel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/10/20.
 */

public class BoardroomRecordDetailActivity extends BaseActivity implements View.OnClickListener, SimpleChoosePanel.OnSelectedFinished {
    @BindView(R.id.room_detail_number)
    TextView roomDetailNumber;
    @BindView(R.id.order_detail_status)
    TextView orderDetailStatus;
    @BindView(R.id.room_detail_spot)
    TextView roomDetailSpot;
    @BindView(R.id.room_detail_date)
    TextView roomDetailDate;
    @BindView(R.id.room_detail_time)
    TextView roomDetailTime;
    @BindView(R.id.room_detail_owner)
    TextView roomDetailOwner;
    @BindView(R.id.room_detail_phone)
    TextView roomDetailPhone;
    @BindView(R.id.room_detail_member1)
    TextView roomDetailMember1;
    @BindView(R.id.room_detail_phone1)
    TextView roomDetailPhone1;
    @BindView(R.id.room_detail_member2)
    TextView roomDetailMember2;
    @BindView(R.id.room_detail_phone2)
    TextView roomDetailPhone2;
    @BindView(R.id.room_detail_member3)
    TextView roomDetailMember3;
    @BindView(R.id.room_detail_phone3)
    TextView roomDetailPhone3;
    @BindView(R.id.room_detail_open_time)
    TextView roomDetailOpenTime;
    @BindView(R.id.room_detail_equip)
    TextView roomDetailEquip;
    @BindView(R.id.room_detail_capacity)
    TextView roomDetailCapacity;
    @BindView(R.id.room_detail_pay_way)
    TextView roomDetailPayWay;
    @BindView(R.id.room_detail_pay_account)
    TextView roomDetailPayAccount;
    @BindView(R.id.room_detail_create_time)
    TextView roomDetailCreateTime;
    @BindView(R.id.room_detail_subject)
    TextView roomDetailSubject;
    @BindView(R.id.room_detail_active_tinker)
    TextView tvTinker;
    @BindView(R.id.room_detail_active_text)
    TextView tvActionText;
    @BindView(R.id.room_detail_delay_time)
    TextView roomDetailDelayTime;
    @BindView(R.id.room_detail_negative_button)
    Button btNeg;
    @BindView(R.id.room_detail_more)
    TextView tvMore;
    @BindView(R.id.room_detail_active_button)
    View bottomView;

    private String rid;
    private int status = 0;  //修改  1：延时

    @Override
    protected void bindView(Bundle savedInstanceState) {
        bottomView.setOnClickListener(this);
        initData(getIntent());
    }

    private void initData(Intent intent) {
        rid = intent.getStringExtra(Constant.BOARD_RESERVE_ID);
        getReserveDetail(rid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
    }

    private void getReserveDetail(String rid) {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getRoomReserveDetail(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ConferenceReserveDetail>>() {
                    @Override
                    public void onNext(BaseResponse<ConferenceReserveDetail> baseResponse) {
                        setData(baseResponse.data);
                    }
                });
    }

    private ConferenceReserveDetail reserveDetail;

    public void setData(ConferenceReserveDetail conferenceRoom) {
        reserveDetail = conferenceRoom;
        roomDetailNumber.setText(conferenceRoom.reservationNum);
        roomDetailSpot.setText(conferenceRoom.name);
        Date date = new Date(conferenceRoom.conferenceBeginTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        roomDetailDate.setText(TribeDateUtils.dateFormat5(date) + " (" + CommonUtils.getWeekFromCalendar(i) + ")");
        long duration = conferenceRoom.planEndTime - conferenceRoom.conferenceBeginTime;
        String durationText = (duration * 10 / 3600000) / 10.0 + "";
        roomDetailTime.setText(TribeDateUtils.dateFormat6(new Date(conferenceRoom.conferenceBeginTime)) + "-" +
                TribeDateUtils.dateFormat6(new Date(conferenceRoom.planEndTime)) + " (" + durationText + "小时)");
        roomDetailOwner.setText(conferenceRoom.personName);
        roomDetailPhone.setText(conferenceRoom.personPhone);
        setParticipants(conferenceRoom.conferenceParticipants);
        String openTime = conferenceRoom.openTime / 3600 + ":" + formatMinute(conferenceRoom.openTime % 3600 * 60 / 3600);
        String closeTime = conferenceRoom.closeTime / 3600 + ":" + formatMinute(conferenceRoom.closeTime % 3600 * 60 / 3600);
        roomDetailOpenTime.setText(openTime + " - " + closeTime);
        roomDetailCapacity.setText("可容纳" + conferenceRoom.galleryful + "-" + conferenceRoom.maxGalleryful + "人");
        roomDetailPayAccount.setText("¥" + conferenceRoom.totalFee);
        roomDetailSubject.setText(conferenceRoom.subject);
        roomDetailCreateTime.setText(TribeDateUtils.dateFormat(new Date(conferenceRoom.createTime)));
        StringBuilder sb = new StringBuilder();
        for (ConferenceEquipment equipment : conferenceRoom.equipmentList) {
            sb.append(equipment.name).append("    ");
        }
        roomDetailEquip.setText(sb.toString());
        dealWithStatus(conferenceRoom);
    }

    private void dealWithStatus(ConferenceReserveDetail conferenceRoom) {
        long currentTime = System.currentTimeMillis();
        if (currentTime < conferenceRoom.conferenceBeginTime) {
            orderDetailStatus.setText(ConferenceReservation.BoardroomOrderStatus.RESERVED.status);
            tvActionText.setText(R.string.update);
            status = 0;
            if (currentTime > conferenceRoom.conferenceBeginTime - 30 * 60000) {
                btNeg.setEnabled(false);
                bottomView.setEnabled(false);
                bottomView.setOnClickListener(null);
            }
        } else if (currentTime > conferenceRoom.conferenceBeginTime && currentTime < conferenceRoom.conferenceEndTime) {
            startCounter((int) (conferenceRoom.conferenceEndTime / 1000 - currentTime / 1000));
            tvTinker.setVisibility(View.VISIBLE);
            orderDetailStatus.setText(ConferenceReservation.BoardroomOrderStatus.PROGRESS.status);
            tvActionText.setText(R.string.put_off);
            btNeg.setEnabled(false);
            status = 1;
        } else if (currentTime > conferenceRoom.conferenceEndTime) {
            orderDetailStatus.setText(ConferenceReservation.BoardroomOrderStatus.FINISHED.status);
            findViewById(R.id.room_detail_bottom).setVisibility(View.GONE);
        }

        if (conferenceRoom.conferenceEndTime > conferenceRoom.planEndTime) {
            findViewById(R.id.view7).setVisibility(View.VISIBLE);
            roomDetailDelayTime.setText(TribeDateUtils.dateFormat(new Date(conferenceRoom.conferenceEndTime)));
        }
        if (conferenceRoom.status == ConferenceReservation.BoardroomOrderStatus.CANCEL) {
            orderDetailStatus.setText(conferenceRoom.status.status);
            findViewById(R.id.room_detail_bottom).setVisibility(View.GONE);
        }
    }

    private String formatMinute(long time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return time + "";
        }
    }

    private Subscriber<Long> subscriber;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriber != null) subscriber.unsubscribe();
    }

    private void startCounter(final int leftTime) {
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                orderDetailStatus.setText(ConferenceReservation.BoardroomOrderStatus.FINISHED.status);
                findViewById(R.id.room_detail_bottom).setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Long aLong) {
                tvTinker.setText(aLong / 60 + " : " + (aLong % 60 < 10 ? "0" + aLong % 60 : aLong % 60));
            }
        };
        Observable.interval(0, 1, TimeUnit.SECONDS).take(leftTime)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long time) {
                        return leftTime - time;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_room_record_detail;
    }


    //取消会议室订单
    public void cancelOrder(View view) {
        if (reserveDetail == null) return;
        new CustomAlertDialog.Builder(this).setTitle("取消订单").setMessage("是否确认取消该会议室预定")
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doCancel();
                    }
                }).create().show();
    }

    private void doCancel() {
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).cancelReserveRoom(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), getString(R.string.cancel_order_success));
                        finish();
                    }
                });
    }

    //修改会议订单
    public void updateOrder() {
        if (reserveDetail == null) return;
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.equipments = reserveDetail.equipmentList;
        conferenceRoom.maxGalleryful = reserveDetail.maxGalleryful;
        conferenceRoom.galleryful = reserveDetail.galleryful;
        conferenceRoom.openTime = reserveDetail.openTime;
        conferenceRoom.closeTime = reserveDetail.closeTime;
        conferenceRoom.subject = reserveDetail.subject;
        conferenceRoom.pictures = reserveDetail.picture;
        conferenceRoom.fee = reserveDetail.fee;
        conferenceRoom.reminderTime = reserveDetail.reminderTime;
        conferenceRoom.conferenceParticipants = reserveDetail.conferenceParticipants;
        conferenceRoom.isUpdate = true;
        conferenceRoom.reservationId = reserveDetail.id;
        conferenceRoom.conferenceBeginTime = reserveDetail.conferenceBeginTime;
        conferenceRoom.id = reserveDetail.conferenceId;
        conferenceRoom.conferenceEndTime = reserveDetail.conferenceEndTime;

        Intent intent = new Intent(this, BoardroomReserveActivity.class);
        intent.putExtra(Constant.CONFERENCE_ROOM, conferenceRoom);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (status == 0) {
            updateOrder();
        } else {
            delayOrder();
        }
    }

    private void delayOrder() {
        if (reserveDetail == null) return;
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getAvailableDelayTime(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<Long>>() {
                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                    }

                    @Override
                    public void onNext(BaseResponse<Long> longBaseResponse) {
                        showAvailableTimePanel(longBaseResponse.data);
                    }
                });
    }

    private class TimeBean {
        public long time;
        public String date;

        public TimeBean(long desTime, String s) {
            time = desTime;
            date = s;
        }

        @Override
        public String toString() {
            return date;
        }
    }

    private void showAvailableTimePanel(Long data) {
        if (data == reserveDetail.conferenceEndTime) {
            ToastUtils.ToastMessage(getCtx(), "该会议室已被预订，无法延时");
            return;
        }
        ArrayList<TimeBean> dates = new ArrayList<>();
        long desTime = reserveDetail.conferenceEndTime;
        while (desTime < data) {
            desTime += 30 * 60 * 1000;
            dates.add(new TimeBean(desTime, TribeDateUtils.dateFormat(new Date(desTime))));
        }
        SimpleChoosePanel panel = new SimpleChoosePanel.Builder<TimeBean>(this, this)
                .setData(dates)
                .setTitle("选择时间")
                .build();
        panel.show();
    }

    @Override
    public void onSelected(Object o) {
        TimeBean bean = (TimeBean) o;
        TribeRetrofit.getInstance().createApi(BoardroomApis.class)
                .delayReserveRoom(rid, new ValueBody(bean.time + ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), "会议延时成功");
                        if (subscriber != null) subscriber.unsubscribe();
                        getReserveDetail(rid);
                    }
                });
    }

    public void setParticipants(final ArrayList<ContactsPersonEntity> participants) {
        if (participants.size() >= 1) {
            roomDetailMember1.setText(participants.get(0).name);
            roomDetailPhone1.setText(participants.get(0).phone);
            roomDetailMember1.setVisibility(View.VISIBLE);
            roomDetailPhone1.setVisibility(View.VISIBLE);
        }
        if (participants.size() >= 2) {
            roomDetailMember2.setText(participants.get(1).name);
            roomDetailPhone2.setText(participants.get(1).phone);
            roomDetailMember2.setVisibility(View.VISIBLE);
            roomDetailPhone2.setVisibility(View.VISIBLE);
        }
        if (participants.size() >= 3) {
            roomDetailMember3.setText(participants.get(2).name);
            roomDetailPhone3.setText(participants.get(2).phone);
            roomDetailMember3.setVisibility(View.VISIBLE);
            roomDetailPhone3.setVisibility(View.VISIBLE);
        }
        if (participants.size() > 3) {
            tvMore.setVisibility(View.VISIBLE);
        }
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), BoardroomParticipantActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, participants);
                intent.putExtra(Constant.CONTACT_FLAG, true);
                startActivity(intent);
            }
        });
    }
}
