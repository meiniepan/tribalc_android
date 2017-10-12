package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/21.
 */
public class BillDetailActivity extends BaseActivity {
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.bill_logo)
    SimpleDraweeView ivLogo;
    @Bind(R.id.tv_amount)
    TextView tvAmount;
    @Bind(R.id.tv_pay_channel)
    TextView tvPayChannel;
    @Bind(R.id.tv_trading_type)
    TextView tvTradingType;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_note)
    TextView tvNote;
    @Bind(R.id.tv_display_name)
    TextView tvDisplayName;

    @Bind(R.id.bill_store_name)
    TextView tvName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        BillEntity entity = (BillEntity) getIntent().getSerializableExtra(Constant.BILL);
        if (entity != null) {          //账单
            tvNumber.setText(entity.id);
            String amount = entity.amount;
            tvAmount.setText(amount);
            tvTradingType.setText(entity.tradingType.status);
            tvName.setText(entity.anotherId);
            tvPayChannel.setText(entity.payChannel.value);
            tvDisplayName.setText(entity.displayName);
            tvTitle.setText(entity.title);
            tvNote.setText(entity.note);
            long createTime = Long.parseLong(entity.createTime);
            Date date = new Date(createTime);
            tvTime.setText(TribeDateUtils.dateFormat9(date));
            String url = Constant.Base.BASE_ALI_URL + entity.anotherId + "/icon.jpg";
            FresoUtils.loadImage(url, ivLogo);
        } else {             //提现记录
//            tvNumber.setText(bill.id);
//            String amount = bill.amount;
//            if (amount.contains("-")) {
//                tvMoney.setMoneyText("支出" + amount.substring(1, amount.length()));
//            } else {
//                tvMoney.setMoneyText("收入" + amount);
//            }
//
//            tvStatus.setText(bill.status.toString());
//
//            Date date = new Date(bill.createTime);
//            tvTime.setText(TribeDateUtils.dateFormat9(date));
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }
}
