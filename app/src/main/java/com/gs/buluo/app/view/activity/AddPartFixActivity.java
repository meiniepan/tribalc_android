package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.PropertyBeen;
import com.gs.buluo.app.bean.RequestBodyBean.CommitPropertyFixRequestBody;
import com.gs.buluo.app.bean.ResponseBody.PropertyFixResponse;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.app.network.PropertyService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.network.TribeUploader;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.ChoosePhotoPanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPartFixActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.add_part_image)
    ImageView mAddImageView;
    @Bind(R.id.add_fix_image_group)
    ViewGroup mViewGroup;
    @Bind(R.id.add_part_community_name)
    EditText mCommunityName;
    @Bind(R.id.add_part_company_name)
    EditText mCompanyName;
    @Bind(R.id.add_part_person)
    EditText mPerson;
    @Bind(R.id.add_part_time)
    EditText mTime;
    @Bind(R.id.add_part_question_desc)
    EditText mQuestionDesc;
    @Bind(R.id.add_part_floor)
    EditText mFloor;

    private static final String TAG = "AddPartFixActivity";
    Context mCtx;
    List<String> mImageURLList = new ArrayList<>();
    List<String> mWebUrlList = new ArrayList<>();
    private PropertyBeen mBeen;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mBeen = (PropertyBeen) getIntent().getExtras().getSerializable(Constant.PROPERTY_BEEN);
        mCompanyName.setText(mBeen.enterpriseID);
        mCommunityName.setText(mBeen.communityID);
        mPerson.setText(mBeen.name);

        findViewById(R.id.add_part_fix_back).setOnClickListener(this);
        findViewById(R.id.add_part_image).setOnClickListener(this);
        mCtx = this;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_part_fix;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_part_fix_back:
                finish();
                break;
            case R.id.add_part_image:
                ChoosePhotoPanel choosePhotoPanel = new ChoosePhotoPanel(this, new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String string) {
                        Log.d(TAG, "onSelected: " + string);
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
                });
                choosePhotoPanel.show();
                break;
            case R.id.add_part_submit:
                String communityName = mCommunityName.getText().toString();
                String company = mCompanyName.getText().toString();
                String person = mPerson.getText().toString();
                String time = mTime.getText().toString();
                String questionDesc = mQuestionDesc.getText().toString();
                checkIsEmpty(communityName, company, person, time, questionDesc);


                for (String url : mImageURLList) {
                    TribeUploader.getInstance().uploadFile(url, "", new File(url), new TribeUploader.UploadCallback() {
                        @Override
                        public void uploadSuccess(UploadAccessResponse.UploadResponseBody url) {
                            Log.d(TAG, "uploadSuccess: " + url.url);
                            mWebUrlList.add(url.url);
                        }

                        @Override
                        public void uploadFail() {
                            ToastUtils.ToastMessage(mCtx, "图片上传失败");
                        }
                    });
                }

                CommitPropertyFixRequestBody requestBody = new CommitPropertyFixRequestBody();
                requestBody.communityId=mBeen.communityID;
                requestBody.companyId=mBeen.enterpriseID;
                requestBody.floor=mFloor.getText().toString().trim();
                requestBody.appointTime=Long.getLong(mTime.getText().toString().trim());
                requestBody.problemDesc=mQuestionDesc.getText().toString().trim();
                requestBody.pictures.addAll(mWebUrlList);

                TribeRetrofit.getIntance().createApi(PropertyService.class)
                        .postFixOrder(TribeApplication.getInstance().getUserInfo().getId(),requestBody).enqueue(new Callback<PropertyFixResponse>() {
                    @Override
                    public void onResponse(Call<PropertyFixResponse> call, Response<PropertyFixResponse> response) {
                        if (response.body().code==201) {
                            ToastUtils.ToastMessage(mCtx,"提交成功");

                        }else if (response.body().code==507){
                            //服务器存储失败
                            ToastUtils.ToastMessage(mCtx,"服务器存储失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<PropertyFixResponse> call, Throwable t) {

                    }
                });

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

    private boolean checkIsEmpty(String a, String b, String c, String d, String e) {
        if (TextUtils.isEmpty(a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(c) || TextUtils.isEmpty(d) || TextUtils.isEmpty(e)) {
            ToastUtils.ToastMessage(mCtx, "信息填写不完整,请完善信息..");
            return true;
        }
        return false;
    }
}
