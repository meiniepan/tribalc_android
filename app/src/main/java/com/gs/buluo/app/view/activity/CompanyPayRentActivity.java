package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.PayRentEvent;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;
import com.gs.buluo.app.network.CompanyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.panel.CompanyRentPlanItem;
import com.gs.buluo.app.view.widget.panel.NewPayPanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.StatusLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/8/8.
 */

public class CompanyPayRentActivity extends BaseActivity implements View.OnClickListener, NewPayPanel.OnPayFinishListener {
    @BindView(R.id.sl_company_pay_rent)
    StatusLayout mStatusLayout;
    @BindView(R.id.tv_source_name)
    TextView tvName;
    @BindView(R.id.tv_rent_time)
    TextView tvRentTime;
    @BindView(R.id.tv_pay_cycle)
    TextView tvPayCycle;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_monthly_rent)
    MoneyTextView tvMoney;
    @BindView(R.id.ll_have_active)
    LinearLayout llHaveActive;
    @BindView(R.id.layout_all_payed)
    View layoutAllPayed;
    @BindView(R.id.btn_rent_confirm)
    Button btnConfirm;
    private Context mCtx;
    private String rental;
    private String protocolId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_pay_rent;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = getCtx();
        btnConfirm.setOnClickListener(this);
        findViewById(R.id.ll_company_all_pay_plan).setOnClickListener(this);
        findViewById(R.id.rent_all_plan).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        String companyId = TribeApplication.getInstance().getUserInfo().getCompanyID();
        String uId = TribeApplication.getInstance().getUserInfo().getId();
        mStatusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(CompanyApis.class).getCompanyActiveRentPlan(companyId, uId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<CompanyRentPlanItem>>>() {
                    @Override
                    public void onNext(BaseResponse<List<CompanyRentPlanItem>> response) {
                        mStatusLayout.showContentView();
                        CompanyRentPlanItem data = response.data.get(0);
                        protocolId = data.protocolId;
                        if (data.rentPlanItem != null) {
                            llHaveActive.setVisibility(View.VISIBLE);
                            btnConfirm.setEnabled(true);
                            layoutAllPayed.setVisibility(View.GONE);
                            setText(data.rentPlanItem);
                        } else {
                            llHaveActive.setVisibility(View.GONE);
                            layoutAllPayed.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        btnConfirm.setEnabled(false);
                        mStatusLayout.showErrorView();
                    }
                });
    }

    private void setText(RentPlanItem data) {
        tvName.setText(TribeApplication.getInstance().getUserInfo().getCompanyName());
        tvRentTime.setText(TribeDateUtils.SDF5.format(data.startTime) + " 至 " + TribeDateUtils.SDF5.format(data.endTime));
        tvPayCycle.setText(data.num);
        tvPayTime.setText(TribeDateUtils.SDF5.format(data.plannedTime));
        rental = data.plannedRental;
        tvMoney.setMoneyText(data.plannedRental);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_rent_confirm:
                NewPayPanel payPanel = new NewPayPanel(mCtx, this);
                payPanel.setData(rental, protocolId, "rent",true);
                payPanel.show();
                break;
            case R.id.ll_company_all_pay_plan:
            case R.id.rent_all_plan:
                intent.setClass(mCtx, CompanyPayRentPlanActivity.class);
                intent.putExtra(Constant.RENT_PROTOCOL_ID, protocolId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPaySuccess() {
        EventBus.getDefault().post(new PayRentEvent());
        finish();
    }

    @Override
    public void onPayFail(ApiException e) {
        ToastUtils.ToastMessage(getCtx(), "付款失败，错误码 " + e.getCode());
    }

}
