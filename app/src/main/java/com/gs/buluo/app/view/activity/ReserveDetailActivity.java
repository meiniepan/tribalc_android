package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.ListReservation;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.DetailReservationPresenter;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.impl.IDetailReserveView;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/30.
 */
public class ReserveDetailActivity extends BaseActivity implements IDetailReserveView {
    @Bind(R.id.reserve_detail_title)
    TextView tvTitle;
    @Bind(R.id.reserve_detail_time)
    TextView tvTime;
    @Bind(R.id.reserve_detail_count)
    TextView tvCount;
    @Bind(R.id.reserve_detail_title_describe)
    TextView tvDescribe;
    @Bind(R.id.reserve_detail_item_name)
    TextView tvItemName;
    @Bind(R.id.reserve_detail_item_time)
    TextView tvItemTime;
    @Bind(R.id.reserve_detail_item_people_count)
    TextView tvItemCount;
    @Bind(R.id.reserve_detail_item_tags)
    TextView tvTags;
    @Bind(R.id.reserve_detail_restaurant)
    TextView tvRestaurant;
    @Bind(R.id.reserve_detail_spot)
    TextView tvAddress;
    @Bind(R.id.reserve_detail_phone)
    TextView tvPhone;
    @Bind(R.id.reserve_detail_contact)
    TextView tvContact;
    @Bind(R.id.reserve_detail_sign)
    ImageView sign;
    @Bind(R.id.reserve_detail_item_picture)
    SimpleDraweeView picture;

    @Bind(R.id.reserve_detail_finish)
    TextView tvFinish;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        final ListReservation reservation = getIntent().getParcelableExtra(Constant.SERVE_ID);
        setStatus(reservation);
        tvItemName.setText(reservation.storeName);
        tvRestaurant.setText(reservation.storeName);
        tvTags.setText(reservation.markPlace);
        if (reservation.tags!=null&&reservation.tags.size()>0){
            tvTags.setText(reservation.tags.get(0)+"  |  "+reservation.markPlace);
        }
        tvTime.setText(TribeDateUtils.dateFormat9(new Date(reservation.appointTime)));
        tvCount.setText(reservation.personNum);
        tvItemCount.setText(reservation.personNum);
        tvItemTime.setText(TribeDateUtils.dateFormat9(new Date(reservation.appointTime)));
        setDescription(reservation.status);
        FresoUtils.loadImage(reservation.mainPicture,picture);

        ((DetailReservationPresenter)mPresenter).getReserveDetail(reservation.id);

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailReservationPresenter)mPresenter).cancelReserve(reservation.id,ListReservation.ReserveStatus.CANCEL.toString());
            }
        });
        findViewById(R.id.reserve_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setStatus(ListReservation entity) {
        if (entity.status== ListReservation.ReserveStatus.PROCESSING){
            tvTitle.setText(R.string.reserve_processing);
            tvFinish.setText(R.string.cancel_order);
        }else if (entity.status== ListReservation.ReserveStatus.SUCCEED){
            tvTitle.setText(R.string.reserve_success);
            tvFinish.setText(R.string.cancel_order);
        }else {
            tvTitle.setText(R.string.reserve_fail);
            tvFinish.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_reserve_detail;
    }

    public void setDescription(ListReservation.ReserveStatus description) {
        if (description == ListReservation.ReserveStatus.PROCESSING) {
            tvDescribe.setText(R.string.reserve_processing_comment);
            sign.setImageResource(R.mipmap.reserve_process);
        } else if (description == ListReservation.ReserveStatus.SUCCEED) {
            sign.setImageResource(R.mipmap.reserve_success);
            tvDescribe.setText(R.string.reserve_success_comment);
        } else {
            sign.setImageResource(R.mipmap.reserve_fail);
            tvDescribe.setText(R.string.reserve_fail_comment);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new DetailReservationPresenter();
    }

    @Override
    public void getDetailSuccess(DetailReservation reservation) {
        tvPhone.setText(reservation.phone);
        tvContact.setText(reservation.linkman);
        tvAddress.setText(reservation.address);
    }

    @Override
    public void cancelSuccess() {
        ToastUtils.ToastMessage(this,"取消订单成功");
        startActivity(new Intent(this,ReserveActivity.class));
        //TODO add intent  ListDetailReservation
    }

    @Override
    public void cancelFailure() {
        ToastUtils.ToastMessage(this,"取消订单失败");
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(res));
    }
}
