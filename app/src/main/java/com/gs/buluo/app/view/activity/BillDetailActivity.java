package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.WelfareMemberAdapter;
import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.bean.WelfareEntity;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.Date;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/21.
 */
public class BillDetailActivity extends BaseActivity {
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.bill_logo)
    SimpleDraweeView ivLogo;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_pay_channel)
    TextView tvPayChannel;
    @BindView(R.id.tv_trading_type)
    TextView tvTradingType;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_display_name)
    TextView tvDisplayName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        BillEntity entity = (BillEntity) intent.getSerializableExtra(Constant.BILL);
        String welfareId = intent.getStringExtra(Constant.WARFARE);
        if (entity != null) {          //账单
            tvNumber.setText(entity.id);
            String amount = entity.amount;
            tvAmount.setText(amount);
            tvTradingType.setText(entity.tradingType.status);
            tvPayChannel.setText(entity.payChannel.value);
            tvDisplayName.setText(entity.displayName);
            tvTitle.setText(entity.title);
            tvNote.setText(entity.note);
            long createTime = Long.parseLong(entity.createTime);
            Date date = new Date(createTime);
            tvTime.setText(TribeDateUtils.dateFormat9(date));
            String url = Constant.Base.BASE_ALI_URL + entity.anotherId + "/icon.jpg";
            FresoUtils.loadImage(url, ivLogo);
        } else if (welfareId != null) {             //公司福利
            TribeRetrofit.getInstance().createApi(MoneyApis.class).getCompanyWelfareDetail(TribeApplication.getInstance().getUserInfo().getCompanyID(), welfareId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<WelfareEntity>>() {
                        @Override
                        public void onNext(BaseResponse<WelfareEntity> welfareEntityBaseResponse) {
                            setData(welfareEntityBaseResponse.data);
                        }
                    });
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }


    @BindView(R.id.welfare_list)
    ListView listView;

    public void setData(WelfareEntity entity) {
        tvNumber.setText(entity.transSerialNo);
        String amount = entity.totalAmount;
        tvAmount.setText(amount);
        tvTradingType.setText("公司福利发放");
        tvPayChannel.setText(entity.payChannel.value);
        tvDisplayName.setText(entity.ownerName);
        Date date = new Date(entity.payDate);
        tvTime.setText(TribeDateUtils.dateFormat9(date));
        String url = Constant.Base.BASE_ALI_URL + entity.ownerId + "/icon.jpg";
        FresoUtils.loadImage(url, ivLogo);
        listView.setAdapter(new WelfareMemberAdapter(this, entity.paymentRouterViews));
    }
}
