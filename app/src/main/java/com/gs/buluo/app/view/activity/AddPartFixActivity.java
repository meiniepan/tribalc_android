package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.bean.PropertyBeen;
import com.gs.buluo.app.bean.RequestBodyBean.CommitPropertyFixRequestBody;
import com.gs.buluo.app.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.app.network.PropertyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.network.TribeUploader;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.panel.ChoosePhotoPanel;
import com.gs.buluo.app.view.widget.panel.DatePickerPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.panel.SimpleChoosePanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddPartFixActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.add_part_image)
    ImageView mAddImageView;
    @Bind(R.id.add_fix_image_group)
    ViewGroup mViewGroup;
    @Bind(R.id.add_part_community_name)
    TextView mCommunityName;
    @Bind(R.id.add_part_company_name)
    TextView mCompanyName;
    @Bind(R.id.add_part_person)
    TextView mPerson;
    @Bind(R.id.add_part_time)
    TextView mTime;
    @Bind(R.id.add_part_question_desc)
    EditText mQuestionDesc;
    @Bind(R.id.add_part_floor)
    TextView mFloor;

    Context mCtx;
    List<String> mImageURLList = new ArrayList<>();
    List<String> mWebUrlList = new Vector<>();
    private long mTimeInMillis = -1;
    private int total = 0;
    private int temp;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        PropertyBeen mBeen = getIntent().getParcelableExtra(Constant.ForIntent.PROPERTY_BEEN);
        setData(mBeen);
        findViewById(R.id.add_part_fix_back).setOnClickListener(this);
        findViewById(R.id.add_part_image).setOnClickListener(this);
        findViewById(R.id.add_part_submit).setOnClickListener(this);
        findViewById(R.id.add_part_floor).setOnClickListener(this);
        mTime.setOnClickListener(this);
        mCtx = this;
    }

    private void setData(PropertyBeen been) {
        mCompanyName.setText(been.enterpriseName);
        mPerson.setText(been.name);
        mCommunityName.setText(been.communityName);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_part_fix;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_part_floor:
                initFloorChoose(view);
                break;
            case R.id.add_part_time:
                initBirthdayPicker((TextView) view);
                break;
            case R.id.add_part_fix_back:
                finish();
                break;
            case R.id.add_part_image:
                ChoosePhotoPanel choosePhotoPanel = new ChoosePhotoPanel(this, new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String string) {
                        showChoosePic(string);
                    }
                });
                choosePhotoPanel.show();
                break;
            case R.id.add_part_submit:
                showLoadingDialog();
                String communityName = mCommunityName.getText().toString();
                String company = mCompanyName.getText().toString();
                String person = mPerson.getText().toString();
                String time = mTime.getText().toString();
                String questionDesc = mQuestionDesc.getText().toString();
                if (!checkIsEmpty(communityName, company, person, time, questionDesc)) {
                    upLoadPicture();
                }
                break;
            default:
                if (view instanceof ImageView) {
                    FrameLayout tag = (FrameLayout) view.getTag();
                    String url = (String) tag.getTag();
                    mViewGroup.removeView(tag);
                    mImageURLList.remove(url);
                    if (mImageURLList.size() <= 2) {
                        mAddImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    public void showChoosePic(String string) {
        mImageURLList.add(string);
        if (mImageURLList.size() >= 3) {
            mAddImageView.setVisibility(View.INVISIBLE);
        }
        FrameLayout frameLayout = new FrameLayout(mCtx);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dip2px(mCtx, 100), DensityUtils.dip2px(mCtx, 100));
        lp.setMargins(DensityUtils.dip2px(mCtx, 4), 0, DensityUtils.dip2px(mCtx, 4), 0);
        frameLayout.setLayoutParams(lp);
        ImageView imageView = new ImageView(mCtx);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(mCtx).load(string).into(imageView);
        frameLayout.addView(imageView);
        frameLayout.setTag(string);

        ImageView del = new ImageView(mCtx);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DensityUtils.dip2px(mCtx, 20), DensityUtils.dip2px(mCtx, 20));
        params.setMargins(DensityUtils.dip2px(mCtx, 80), 0, 0, 0);
        del.setLayoutParams(params);
        del.setImageResource(R.mipmap.del_pic);
        del.setTag(frameLayout);
        del.setOnClickListener((View.OnClickListener) mCtx);
        frameLayout.addView(del);

        mViewGroup.addView(frameLayout);
    }

    private void initFloorChoose(final View view) {
        SimpleChoosePanel.Builder<Integer> builder = new SimpleChoosePanel.Builder<>(mCtx, new SimpleChoosePanel.OnSelectedFinished<Integer>() {
            @Override
            public void onSelected(Integer string) {
                ((TextView) view).setText(string + "");
            }
        });
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            list.add(i);
        }
        SimpleChoosePanel simpleChoosePanel = builder.setData(list).setTitle("请选择楼层").build();
        simpleChoosePanel.show();
    }

    private void upLoadPicture() {
        total = mImageURLList.size();
        temp = 0;
        if (mImageURLList.size() == 0) {
            doSubmit();
            return;
        }
        for (int i = 0; i < mImageURLList.size(); i++) {
            TribeUploader.getInstance().uploadFile("property" + i, "", mImageURLList.get(i),
                    new TribeUploader.UploadCallback() {
                        @Override
                        public void uploadSuccess(UploadResponseBody url) {
                            temp += 1;
                            mWebUrlList.add(url.objectKey);
                            if (temp == total) {
                                doSubmit();
                            }
                        }

                        @Override
                        public void uploadFail() {
                            dismissDialog();
                            ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                        }
                    });
        }
    }

    private void doSubmit() {
        CommitPropertyFixRequestBody requestBody = new CommitPropertyFixRequestBody();
        requestBody.floor = mFloor.getText().toString().trim();
        requestBody.appointTime = mTimeInMillis;
        requestBody.problemDesc = mQuestionDesc.getText().toString().trim();
        requestBody.pictures = mWebUrlList;
        requestBody.fixProject = "PIPE_FIX";
        LoadingDialog.getInstance().show(this, "", true);
        TribeRetrofit.getInstance().createApi(PropertyApis.class)
                .postFixOrder(TribeApplication.getInstance().getUserInfo().getId(), requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListPropertyManagement>>() {
                    @Override
                    public void onNext(BaseResponse<ListPropertyManagement> response) {
                        ToastUtils.ToastMessage(mCtx, "提交成功,等待维修师傅接单");
                        finish();
                    }
                });
    }

    private boolean checkIsEmpty(String a, String b, String c, String d, String e) {
        if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(c) || TextUtils.isEmpty(d) || TextUtils.isEmpty(e)) {
            ToastUtils.ToastMessage(mCtx, "信息填写不完整,请完善信息..");
            return true;
        }
        return false;
    }

    private void initBirthdayPicker(final TextView birthday) {
        DatePickerPanel pickerPanel = new DatePickerPanel(this, new DatePickerPanel.OnSelectedFinished() {
            @Override
            public void onSelected(long time) {
                mTimeInMillis = time;
                birthday.setText(TribeDateUtils.dateFormat3(new Date(time)));
            }
        });
        pickerPanel.show();
    }

}
