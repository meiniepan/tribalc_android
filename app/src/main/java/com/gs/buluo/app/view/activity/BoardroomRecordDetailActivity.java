package com.gs.buluo.app.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ConferenceEquipment;
import com.gs.buluo.app.bean.ConferenceReserveDetail;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.Date;
import java.util.List;
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

public class BoardroomRecordDetailActivity extends BaseActivity implements View.OnClickListener {
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
    @BindView(R.id.room_detail_negative_button)
    Button btNeg;
    @BindView(R.id.room_detail_more)
    TextView tvMore;

    private String rid;
    private int status = 0;  //修改  1：延时

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String rid = getIntent().getStringExtra(Constant.BOARD_RESERVE_ID);
        getReserveDetail(rid);
        findViewById(R.id.room_detail_active_button).setOnClickListener(this);
    }

    private void getReserveDetail(String rid) {
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getRoomReserveDetail(rid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ConferenceReserveDetail>>() {
                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<ConferenceReserveDetail> baseResponse) {
                        setData(baseResponse.data);
                    }
                });
    }

    public void setData(ConferenceReserveDetail conferenceRoom) {
        rid = conferenceRoom.id;
        roomDetailNumber.setText(conferenceRoom.reservationNum);
        roomDetailSpot.setText(conferenceRoom.name);
        roomDetailDate.setText(TribeDateUtils.dateFormat5(new Date(conferenceRoom.conferenceBeginTime)));
        roomDetailTime.setText(TribeDateUtils.dateFormat6(new Date(conferenceRoom.conferenceBeginTime)) + "-" + TribeDateUtils.dateFormat6(new Date(conferenceRoom.conferenceEndTime)));
        roomDetailOwner.setText(conferenceRoom.personName);
        roomDetailPhone.setText(conferenceRoom.personPhone);
        setParticipants(conferenceRoom.conferenceParticipants);
        String openTime = conferenceRoom.openTime/3600+":"+conferenceRoom.openTime%3600*60/3600;
        String closeTime =  conferenceRoom.closeTime/3600+":"+conferenceRoom.closeTime%3600*60/3600;
        roomDetailOpenTime.setText(openTime+" - "+closeTime);
        roomDetailCapacity.setText("可容纳"+conferenceRoom.galleryful+"-"+conferenceRoom.maxGalleryful+"人");
        roomDetailPayAccount.setText("¥"+conferenceRoom.totalFee);
        roomDetailSubject.setText(conferenceRoom.subject);
        StringBuilder sb = new StringBuilder();
        for (ConferenceEquipment equipment : conferenceRoom.equipmentList) {
            sb.append(equipment.name).append("    ");
        }
        roomDetailEquip.setText(sb.toString());
        long currentTime = System.currentTimeMillis();
        if (currentTime < conferenceRoom.conferenceBeginTime) {
            orderDetailStatus.setText("已预定");
            tvActionText.setText("修改");
            status = 0;
            if (currentTime > conferenceRoom.conferenceBeginTime - 30 * 60000) {
                btNeg.setEnabled(false);
            }
        } else if (currentTime > conferenceRoom.conferenceBeginTime && currentTime < conferenceRoom.conferenceEndTime) {
            startCounter((int) (conferenceRoom.conferenceEndTime / 1000 - currentTime / 1000));
            tvTinker.setVisibility(View.VISIBLE);
            orderDetailStatus.setText("已开始");
            tvActionText.setText("延期");
            btNeg.setEnabled(false);
            status = 1;
        } else if (currentTime > conferenceRoom.conferenceEndTime) {
            orderDetailStatus.setText("已结束");
            findViewById(R.id.room_detail_bottom).setVisibility(View.GONE);
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
                orderDetailStatus.setText("已结束");
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
        new CustomAlertDialog.Builder(this).setTitle("取消订单").setMessage("是否确认取消该会议室预定")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                        ToastUtils.ToastMessage(getCtx(), "订单取消成功");
                        finish();
                    }
                });
    }


    //修改会议订单
    public void updateOrder() {

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


    }

    public void setParticipants(List<ContactsPersonEntity> participants) {
            if (participants.size()==1){
                roomDetailMember1.setText(participants.get(0).name);
                roomDetailPhone1.setText(participants.get(0).phone);
                roomDetailMember1.setVisibility(View.VISIBLE);
                roomDetailPhone1.setVisibility(View.VISIBLE);
            }else if (participants.size()==2){
                roomDetailMember2.setText(participants.get(1).name);
                roomDetailPhone2.setText(participants.get(1).phone);
                roomDetailMember2.setVisibility(View.VISIBLE);
                roomDetailPhone2.setVisibility(View.VISIBLE);
            }else if (participants.size()==3){
                roomDetailMember3.setText(participants.get(2).name);
                roomDetailPhone3.setText(participants.get(2).phone);
                roomDetailMember3.setVisibility(View.VISIBLE);
                roomDetailPhone3.setVisibility(View.VISIBLE);
            }
            if (participants.size()>3){
                tvMore.setVisibility(View.VISIBLE);
            }
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
    }
}
