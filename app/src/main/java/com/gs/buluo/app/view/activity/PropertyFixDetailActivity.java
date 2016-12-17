package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.utils.DensityUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class PropertyFixDetailActivity extends BaseActivity {

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
    public Context mContext;
    private static final String TAG = "PropertyFixDetailActivity";
    private ListPropertyManagement mManagement;

    public String timeLongToString(long time){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return formatter.format(date);
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mContext=this;
        mManagement = ((ListPropertyManagement) getIntent().getExtras().getSerializable(Constant.PROPERTY_MANAGEMENT));
        mCommunity.setText(mManagement.communityName);
        mCompany.setText(mManagement.companyName);
        mPerson.setText(mManagement.applyPersonName);
        mTime.setText(timeLongToString(mManagement.appointTime));
        mFloor.setText(mManagement.floor);
        mQusetion.setText(mManagement.problemDesc);

        for (String picture : mManagement.pictures) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtils.dip2px(mContext, 100), DensityUtils.dip2px(mContext, 100));
            params.setMargins(DensityUtils.dip2px(mContext, 4), 0, DensityUtils.dip2px(mContext, 4), 0);
            imageView.setLayoutParams(params);
            mLinearLayout.addView(imageView);
            String url = Constant.BASE_IMG_URL + picture;
            Glide.with(mContext).load(url).into(imageView);
        }

        switch (mManagement.status) {
            case "ORDER_ACCEPT":
                mMasterInfo.setVisibility(View.GONE);
                break;
            case "TASK_CONFIRM":
                mMasterName.setText(mManagement.masterPersonName);
                mMasterTel.setText(mManagement.phone);
                mDoorTime.setText(timeLongToString(mManagement.doorTime));
                break;
            case "NOT_PAYING":
                mMasterName.setText(mManagement.masterPersonName);
                mMasterTel.setText(mManagement.phone);
                mDoorTime.setText(timeLongToString(mManagement.doorTime));
                break;
            case  "PAYED":
                mPay.setVisibility(View.GONE);
                break;

        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_property_fix_detail;
    }
}
