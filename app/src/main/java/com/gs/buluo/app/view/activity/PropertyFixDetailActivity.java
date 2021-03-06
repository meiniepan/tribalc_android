package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.HorPictureAdapter;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.network.PropertyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.widget.panel.PayPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PropertyFixDetailActivity extends BaseActivity implements View.OnClickListener, PayPanel.OnPayPanelDismissListener {
    @BindView(R.id.fix_detail_community_name)
    TextView mCommunity;
    @BindView(R.id.fix_detail_company_name)
    TextView mCompany;
    @BindView(R.id.fix_detail_person)
    TextView mPerson;
    @BindView(R.id.fix_detail_floor)
    TextView mFloor;
    @BindView(R.id.fix_detail_time)
    TextView mTime;
    @BindView(R.id.fix_detail_question_desc)
    TextView mQusetion;
    @BindView(R.id.fix_detail_image_group)
    RecyclerView mImgGroup;
    @BindView(R.id.fix_detail_pay)
    TextView mPay;
    @BindView(R.id.master_info)
    LinearLayout mMasterInfo;
    @BindView(R.id.master_name)
    TextView mMasterName;
    @BindView(R.id.master_doorTime)
    TextView mDoorTime;
    @BindView(R.id.master_tel)
    TextView mMasterTel;
    @BindView(R.id.property_detail_cancel)
    TextView mCancel;
    @BindView(R.id.master_price)
    TextView mPrice;
    public Context mContext;
    private ListPropertyManagement mManagement;

    public String timeLongToString(long time) {
        if (time == 0) return "";
        return TribeDateUtils.dateFormat7(new Date(time));
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext = this;
        findViewById(R.id.property_detail_back).setOnClickListener(this);
        findViewById(R.id.property_detail_cancel).setOnClickListener(this);
        mManagement = (getIntent().getExtras().getParcelable(Constant.PROPERTY_MANAGEMENT));
        mCommunity.setText(mManagement.communityName);
        mCompany.setText(mManagement.companyName);
        mPerson.setText(mManagement.applyPersonName);
        mTime.setText(timeLongToString(mManagement.appointTime));
        mFloor.setText(mManagement.floor);
        mQusetion.setText(mManagement.problemDesc);
        mPay.setOnClickListener(this);
        if (mManagement.pictures != null && mManagement.pictures.size() > 0) {
            mImgGroup.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mImgGroup.setAdapter(new HorPictureAdapter(this, mManagement.pictures));
        }
        switch (mManagement.status)
        {
            case "ORDER_ACCEPT":
                mMasterInfo.setVisibility(View.GONE);
                mPay.setVisibility(View.GONE);
                break;
            case "TASK_CONFIRM":
                mMasterName.setText(mManagement.masterPersonName);
                mMasterTel.setText(mManagement.phone);
                mDoorTime.setText(timeLongToString(mManagement.doorTime));
                mPay.setVisibility(View.GONE);
                break;
            case "TO_FIX":
                mMasterName.setText(mManagement.masterPersonName);
                mMasterTel.setText(mManagement.phone);
                mDoorTime.setText(timeLongToString(mManagement.doorTime));
                mPay.setVisibility(View.VISIBLE);
                mPay.setText("待维修");
                mPay.setClickable(false);
                mCancel.setVisibility(View.GONE);
                break;
            case "TO_PAYING":
                mMasterName.setText(mManagement.masterPersonName);
                mMasterTel.setText(mManagement.phone);
                mDoorTime.setText(timeLongToString(mManagement.doorTime));
                findViewById(R.id.master_price_area).setVisibility(View.VISIBLE);
                mPrice.setText(mManagement.totalFee);
                mPay.setText("去付款");
                mCancel.setVisibility(View.GONE);
                break;
            case "PAY_ED":
                mMasterName.setText(mManagement.masterPersonName);
                mMasterTel.setText(mManagement.phone);
                mDoorTime.setText(timeLongToString(mManagement.doorTime));
                findViewById(R.id.master_price_area).setVisibility(View.VISIBLE);
                mPrice.setText(mManagement.totalFee);
                mPay.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                break;
            case "CANCEL":
                mMasterName.setText(mManagement.masterPersonName);
                mMasterTel.setText(mManagement.phone);
                mDoorTime.setText(timeLongToString(mManagement.doorTime));
                mPay.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                if (mManagement.masterPersonName == null) {
                    mMasterInfo.setVisibility(View.GONE);
                }
                break;
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_property_fix_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.property_detail_back:
                finish();
                break;
            case R.id.fix_detail_pay:
                if (TextUtils.equals(mManagement.status, "TO_PAYING"))
                    showPayBoard();
                break;
            case R.id.property_detail_cancel:
                cancelProperty(mManagement.id);
                break;
        }
    }

    private void showPayBoard() {
        ArrayList<String> list = new ArrayList<>();
        list.add(mManagement.id);
        PayPanel payBoard = new PayPanel(this, this);
        payBoard.setData(mManagement.totalFee, list, "maintain");
        payBoard.show();
    }

    private void cancelProperty(String id) {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(PropertyApis.class).cancelPropertyFixList(id, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        ToastUtils.ToastMessage(mContext, R.string.cancel_success);
                        finish();
                        startActivity(new Intent(mContext, PropertyListActivity.class));
                    }
                });
    }

    @Override
    public void onPayPanelDismiss() {
        startActivity(new Intent(this, PropertyListActivity.class));
        finish();
    }
}
