package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.HousePaymentFragmentAdapter;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;
import com.gs.buluo.app.bean.RequestBodyBean.RentProtocolWithholdInfo;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/6/21.
 */

public class HousePaymentActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.house_payment_pager)
    ViewPager pager;
    @Bind(R.id.tv_addWithhold)
    TextView tvAddWithhold;
    @Bind(R.id.ll_house_all_pay_plan)
    LinearLayout llAllPlan;
    @Bind(R.id.ll_house_withhold_detail)
    LinearLayout llWithholdDetail;
    @Bind(R.id.tv_house_bank_name)
    TextView tvBankName;
    @Bind(R.id.tv_house_bank_num)
    TextView tvBankNum;
    @Bind(R.id.iv_house_bank_icon)
    ImageView ivBankIcon;
    @Bind(R.id.ll_house_bank_background)
    LinearLayout llBankGround;
    @Bind(R.id.iv_house_edit)
    ImageView ivBankEdit;
    @Bind(R.id.tv_house_rent)
    TextView tvHouseRent;
    @Bind(R.id.tv_house_life)
    TextView tvHouseLife;
    @Bind(R.id.sl_house_payment)
    StatusLayout mStatusLayout;

    private Context mContext;
    private RentProtocolWithholdInfo mWithholdInfo;
    private String protocolId;
    private String apartmentCode;
    private String apartmentName;
    private boolean allFinished = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_house_payment;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = this;
        protocolId = getIntent().getStringExtra(Constant.RENT_PROTOCOL_ID);
        apartmentCode = getIntent().getStringExtra(Constant.RENT_APARTMENT_CODE);
        apartmentName = getIntent().getStringExtra(Constant.RENT_APARTMENT_NAME);
//        setAllpayedView();
//        setFragmentAdapter();
        tvAddWithhold.setOnClickListener(this);
        llAllPlan.setOnClickListener(this);
        ivBankEdit.setOnClickListener(this);
        tvHouseRent.setOnClickListener(this);
        tvHouseLife.setOnClickListener(this);
    }

    private void setAllpayedView() {
        tvAddWithhold.setVisibility(View.GONE);
        llAllPlan.setVisibility(View.GONE);
        llWithholdDetail.setVisibility(View.GONE);
    }

    private void setFragmentAdapter() {
        String uid = TribeApplication.getInstance().getUserInfo().getId();
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).getRentPlanItems(getIntent().getStringExtra(Constant.RENT_PROTOCOL_ID), uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<RentPlanItem>>>() {
                    @Override
                    public void onNext(BaseResponse<List<RentPlanItem>> listBaseResponse) {
                        mStatusLayout.showContentView();
                        if (listBaseResponse.data.get(listBaseResponse.data.size() - 1).finished)
                            allFinished = true;
                        initAdapter();
                        initWithholdInfoDetail();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        mStatusLayout.showErrorView();
                    }
                });

    }

    private void initAdapter() {
        String[] titles = new String[]{getString(R.string.payment_rent), getString(R.string.payment_life)};
        HousePaymentFragmentAdapter adapter =
                new HousePaymentFragmentAdapter(allFinished, protocolId, apartmentCode,
                        apartmentName, getSupportFragmentManager(), Arrays.asList(titles));
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addWithhold:
                Intent intent = new Intent(mContext, AddRentWithholdActivity.class);
                intent.putExtra(Constant.RENT_PROTOCOL_ID, protocolId);
                startActivity(intent);
                break;
            case R.id.iv_house_edit:
                Intent intent2 = new Intent(mContext, AddRentWithholdActivity.class);
                intent2.putExtra("from", Constant.WITHHOLD_UPDATE);//2为修改页面，区别于新加页面
                intent2.putExtra("protocolId", getIntent().getStringExtra("protocolId"));
                intent2.putExtra("bankCardNum", mWithholdInfo.bankCardNum);
                intent2.putExtra("bankName", mWithholdInfo.bankName);
                intent2.putExtra("userName", mWithholdInfo.userName);
                intent2.putExtra("phone", mWithholdInfo.phone);
                intent2.putExtra("idNo", mWithholdInfo.idNo);
                intent2.putExtra("bankCode", mWithholdInfo.bankCode);

                startActivity(intent2);
                break;
            case R.id.ll_house_all_pay_plan:
                Intent intent3 = new Intent(mContext, RentPaymentPlanActivity.class);
                intent3.putExtra(Constant.RENT_PROTOCOL_ID, protocolId);
                intent3.putExtra(Constant.RENT_APARTMENT_CODE, apartmentCode);
                intent3.putExtra(Constant.RENT_APARTMENT_NAME, apartmentName);
                startActivity(intent3);
                break;
            case R.id.tv_house_rent:
                pager.setCurrentItem(0);
                tvHouseRent.setTextColor(getResources().getColor(R.color.black));
                tvHouseLife.setTextColor(getResources().getColor(R.color.common_gray));
                break;
            case R.id.tv_house_life:
                pager.setCurrentItem(1);
                tvHouseLife.setTextColor(getResources().getColor(R.color.black));
                tvHouseRent.setTextColor(getResources().getColor(R.color.common_gray));
                break;

        }
    }

    private void setWithHoldViewGone() {
        tvAddWithhold.setVisibility(View.GONE);
        llAllPlan.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragmentAdapter();
    }

    private void setWithholdInfo(RentProtocolWithholdInfo data) {
        tvBankName.setText(data.bankName);
        if (data.bankCardNum.length() > 4)
            tvBankNum.setText(data.bankCardNum.substring(data.bankCardNum.length() - 4, data.bankCardNum.length()));
        if (data.bankCode == null) {
            ivBankIcon.setImageResource(R.mipmap.bank_logo_default);
            llBankGround.setBackgroundResource(R.mipmap.bank_bg_default);
        }else {
            switch (data.bankCode) {
                case "BOB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_bob);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_02);
                    break;
                case "ICBC":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_icbc);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_02);
                    break;
                case "ABC":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_abc);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_03);
                    break;
                case "CCB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_ccb);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_01);
                    break;
                case "BOC":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_boc);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_02);
                    break;
                case "BCOM":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_bcom);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_01);
                    break;
                case "CIB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_cib);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_01);
                    break;
                case "CITIC":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_citic);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_02);
                    break;
                case "CEB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_ceb);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_04);
                    break;
                case "PAB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_pab);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_02);
                    break;
                case "PSBC":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_psbc);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_03);
                    break;
                case "SHB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_shb);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_01);
                    break;
                case "SPDB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_spdb);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_01);
                    break;
                case "CMB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_cmb);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_02);
                    break;
                case "GDB":
                    ivBankIcon.setImageResource(R.mipmap.bank_logo_gdb);
                    llBankGround.setBackgroundResource(R.mipmap.bank_bg_01);
                    break;
            }
        }
    }

    private void initWithholdInfoDetail() {
        if (!allFinished) {
            showLoadingDialog();
            TribeRetrofit.getInstance().createApi(DepartmentApi.class).
                    getWithholdInfo(getIntent().getStringExtra("protocolId"), TribeApplication.getInstance().getUserInfo().getId()).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<RentProtocolWithholdInfo>>() {
                                   @Override
                                   public void onNext(BaseResponse<RentProtocolWithholdInfo> response) {
                                       if (response.data == null) {
                                           tvAddWithhold.setVisibility(View.VISIBLE);
                                           llWithholdDetail.setVisibility(View.GONE);
                                       } else {
                                           mWithholdInfo = response.data;
                                           tvAddWithhold.setVisibility(View.GONE);
                                           llWithholdDetail.setVisibility(View.VISIBLE);
                                           setWithholdInfo(response.data);
                                       }
                                   }

                                   @Override
                                   public void onFail(ApiException e) {
                                       ToastUtils.ToastMessage(mContext, R.string.get_withhold_info_error);
                                   }
                               }
                    );
        }
    }
}
