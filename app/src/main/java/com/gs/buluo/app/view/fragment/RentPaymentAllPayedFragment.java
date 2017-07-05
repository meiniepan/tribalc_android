package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.RentPaymentPlanActivity;

import butterknife.Bind;

/**
 * Created by Solang on 2017/6/21.
 */

public class RentPaymentAllPayedFragment extends BaseFragment {
    @Bind(R.id.rent_all_plan)
    LinearLayout tvAllPlan;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_rent_payment_all_payed;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvAllPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RentPaymentPlanActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
