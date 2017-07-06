package com.gs.buluo.app.view.fragment;

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
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.panel.Pay2mPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/6/21.
 */

public class RentPaymentFragment extends BaseFragment {
    @Bind(R.id.rent_source_num)
    TextView tvSourceNum;
    @Bind(R.id.rent_source_name)
    TextView tvSourceName;
    @Bind(R.id.rent_rent_time)
    TextView tvRentTime;
    @Bind(R.id.rent_pay_cycle)
    TextView tvPayCycle;
    @Bind(R.id.rent_pay_time)
    TextView tvPayTime;
    @Bind(R.id.rent_monthly_rent)
    MoneyTextView tvMonthlyRent;
    @Bind(R.id.btn_rent_confirm)
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
                Pay2mPanel payBoard = new Pay2mPanel(mContext, null);
                payBoard.setData(2,rentMoney, protocolId, rentNum,code,name);
                payBoard.show();
//                Intent intent = new Intent(getActivity(), RentPaySuccessActivity.class);
//                getActivity().startActivity(intent);
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
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).getRentPlanItem(protocolId,uid)
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

        tvRentTime.setText(TribeDateUtils.SDF5.format(data.startTime)+"至"+TribeDateUtils.SDF5.format(data.endTime));
        rentNum = data.num;
        tvPayCycle.setText(rentNum);
        tvPayTime.setText(TribeDateUtils.SDF5.format(data.plannedTime));
        rentMoney = data.plannedRental;
        tvMonthlyRent.setMoneyText(rentMoney);
    }
}
