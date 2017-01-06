package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.network.PropertyService;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.widget.panel.PayPanel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import retrofit2.Response;

public class PropertyFixDetailActivity extends BaseActivity implements View.OnClickListener, PayPanel.OnPayPanelDismissListener {
    @Bind(R.id.fix_detail_community_name)
    TextView mCommunity;
    @Bind(R.id.fix_detail_company_name)
    TextView mCompany;
    @Bind(R.id.fix_detail_person)
    TextView mPerson;
    @Bind(R.id.fix_detail_floor)
    TextView mFloor;
    @Bind(R.id.fix_detail_time)
    TextView mTime;
    @Bind(R.id.fix_detail_question_desc)
    TextView mQusetion;
    @Bind(R.id.fix_detail_image_group)
    LinearLayout mLinearLayout;
    @Bind(R.id.fix_detail_pay)
    TextView mPay;
    @Bind(R.id.master_info)
    LinearLayout mMasterInfo;
    @Bind(R.id.master_name)
    TextView mMasterName;
    @Bind(R.id.master_doorTime)
    TextView mDoorTime;
    @Bind(R.id.master_tel)
    TextView mMasterTel;
    @Bind(R.id.property_detail_cancel)
    TextView mCancel;
    @Bind(R.id.master_price)
    TextView mPrice;
    public Context mContext;
    private ListPropertyManagement mManagement;

    public String timeLongToString(long time){
        if (time ==0)return "";
        return  TribeDateUtils.dateFormat7(new Date(time));
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext=this;
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

        for (String picture : mManagement.pictures) {
            if (TextUtils.isEmpty(picture)){
                continue;
            }
            picture = FrescoImageLoader.formatImageUrl(picture);
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dip2px(mContext, 80), DensityUtils.dip2px(mContext, 80));
            params.setMargins(DensityUtils.dip2px(mContext, 4), 0, DensityUtils.dip2px(mContext, 4), 0);
            imageView.setLayoutParams(params);
            mLinearLayout.addView(imageView);

            Glide.with(this).load(picture).placeholder(R.mipmap.default_pic).into(imageView);
        }
        switch (mManagement.status) {
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
            case  "PAY_ED":
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
                if (mManagement.masterPersonName==null){
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
                showPayBoard();
                break;
            case R.id.property_detail_cancel:
                cancelProperty(mManagement.id);
                break;
        }
    }

    private void showPayBoard() {
        ArrayList<String> list=new ArrayList<>();
        list.add(mManagement.id);
        PayPanel payBoard=new PayPanel(this,this);
        payBoard.setData(mManagement.totalFee,list,"maintain");
        payBoard.show();
    }

    private void cancelProperty(String id) {
        TribeRetrofit.getInstance().createApi(PropertyService.class).cancelPropertyFixList(id).enqueue(new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseCodeResponse<CodeResponse>> response) {
                ToastUtils.ToastMessage(mContext,R.string.cancel_success);
                finish();
                startActivity(new Intent(mContext,PropertyListActivity.class));
            }

            @Override
            public void onFail(int responseCode, BaseCodeResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(mContext,R.string.connect_fail);
            }
        });
    }

    @Override
    public void onPayPanelDismiss() {
        startActivity(new Intent(this,PropertyListActivity.class));
        finish();
    }
}
