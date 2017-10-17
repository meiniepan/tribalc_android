package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.activity.RentPaySuccessActivity;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.panel.NewPayPanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/6/21.
 */

public class RentPaymentFragment extends BaseFragment {
    @BindView(R.id.rent_source_num)
    TextView tvSourceNum;
    @BindView(R.id.rent_source_name)
    TextView tvSourceName;
    @BindView(R.id.rent_rent_time)
    TextView tvRentTime;
    @BindView(R.id.rent_pay_cycle)
    TextView tvPayCycle;
    @BindView(R.id.rent_pay_time)
    TextView tvPayTime;
    @BindView(R.id.rent_monthly_rent)
    MoneyTextView tvMonthlyRent;
    @BindView(R.id.btn_rent_confirm)
    Button btnConfirm;
    private String protocolId;
    private String code;
    private String name;
    private String rentMoney;
    private String rentNum;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_rent_payment;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        getData();
        initEvent();
    }

    private void initEvent() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPayPanel payBoard = new NewPayPanel(mContext, new NewPayPanel.OnPayFinishListener() {
                    @Override
                    public void onPaySuccess() {
                        Intent intent = new Intent(mContext, RentPaySuccessActivity.class);
                        intent.putExtra(Constant.RENT_PAYED_NUM, rentNum);
                        intent.putExtra(Constant.RENT_APARTMENT_CODE, code);
                        intent.putExtra(Constant.RENT_APARTMENT_NAME, name);
                        intent.putExtra(Constant.RENT_PROTOCOL_ID, protocolId);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onPayFail(ApiException e) {
                        ToastUtils.ToastMessage(getContext(), e.getDisplayMessage());
                    }
                });
                payBoard.setData(rentMoney, protocolId, "rent");
                payBoard.show();
            }
        });
    }


    private void getData() {
        protocolId = getArguments().getString(Constant.RENT_PROTOCOL_ID);
        code = getArguments().getString(Constant.RENT_APARTMENT_CODE);
        name = getArguments().getString(Constant.RENT_APARTMENT_NAME);
        tvSourceNum.setText(code);
        tvSourceName.setText(name);
        String uid = TribeApplication.getInstance().getUserInfo().getId();
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).getRentPlanItem(protocolId, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<RentPlanItem>>() {
                    @Override
                    public void onNext(BaseResponse<RentPlanItem> response) {
                        setText(response.data);
                    }
                });
    }

    private void setText(RentPlanItem data) {

        tvRentTime.setText(TribeDateUtils.SDF5.format(data.startTime) + " 至 " + TribeDateUtils.SDF5.format(data.endTime));
        rentNum = data.num;
        tvPayCycle.setText(rentNum);
        tvPayTime.setText(TribeDateUtils.SDF5.format(data.plannedTime));
        rentMoney = data.plannedRental;
        tvMonthlyRent.setMoneyText(rentMoney);
    }
}
