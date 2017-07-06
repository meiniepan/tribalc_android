package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.RentPaymentPlanActivity;

import butterknife.Bind;

/**
 * Created by Solang on 2017/6/21.
 */

public class RentPaymentAllPayedFragment extends BaseFragment {
    @Bind(R.id.rent_all_plan)
    LinearLayout tvAllPlan;
    private String protocolId;
    private String apartmentCode;
    private String apartmentName;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_rent_payment_all_payed;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        protocolId = getArguments().getString(Constant.RENT_PROTOCOL_ID);
        apartmentCode = getArguments().getString(Constant.RENT_APARTMENT_CODE);
        apartmentName = getArguments().getString(Constant.RENT_APARTMENT_NAME);
        getActivity().findViewById(R.id.ll_house_all_pay_plan).setVisibility(View.GONE);
        getActivity().findViewById(R.id.ll_house_withhold_detail).setVisibility(View.GONE);
        getActivity().findViewById(R.id.tv_addWithhold).setVisibility(View.GONE);
        tvAllPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RentPaymentPlanActivity.class);
                intent.putExtra(Constant.RENT_PROTOCOL_ID, protocolId);
                intent.putExtra(Constant.RENT_APARTMENT_CODE, apartmentCode);
                intent.putExtra(Constant.RENT_APARTMENT_NAME, apartmentName);
                getActivity().startActivity(intent);
            }
        });
    }
}
