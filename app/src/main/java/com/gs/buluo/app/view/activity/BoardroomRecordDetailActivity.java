package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.R;

import butterknife.BindView;

/**
 * Created by hjn on 2017/10/20.
 */

public class BoardroomRecordDetailActivity extends BaseActivity {
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

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String s = "投影仪";
        String s2 = "窗户";
        String s3 = "床";
        String s4 = "矿泉水";
        String s5 = "白班";
        String s6 = "桌子";
        String s7 = "不知道什么东西";
        StringBuilder sb = new StringBuilder();
        sb.append(s).append("   ").append(s2).append("   ").append(s3).append("   ").append(s4).append("   ").append(s5).append("   ").append(s7).append("   ").append(s);
        roomDetailEquip.setText(sb.toString());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_room_record_detail;
    }


    //取消会议室订单
    public void cancelOrder(View view) {

    }


    //修改会议订单
    public void updateOrder(View view) {

    }
}
