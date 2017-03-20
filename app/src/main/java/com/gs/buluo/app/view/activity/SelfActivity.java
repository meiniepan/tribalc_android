package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.SelfEvent;
import com.gs.buluo.app.network.TribeUploader;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.SelfPresenter;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.impl.ISelfView;
import com.gs.buluo.app.view.widget.panel.ChoosePhotoPanel;
import com.gs.buluo.app.view.widget.panel.ModifyInfoPanel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Date;

import butterknife.Bind;



/**
 * Created by hjn on 2016/11/2.
 */
public class SelfActivity extends BaseActivity implements View.OnClickListener,ISelfView{
    @Bind(R.id.tv_birthday)
    TextView mBirthday;
    @Bind(R.id.tv_address)
    TextView mAddress;
    @Bind(R.id.tv_detail_address)
    TextView mDetailAddress;
    @Bind(R.id.self_iv_head)
    SimpleDraweeView header;
    @Bind(R.id.tv_sex)
    TextView mSex;
    @Bind(R.id.tv_motion)
    TextView mMotion;
    @Bind(R.id.tv_nickname)
    TextView mName;
    @Bind(R.id.tv_number)
    TextView mPhone;

    Context mCtx;
    public final int addressCode = 200;
    private ModifyInfoPanel panel;
    private UserInfoEntity userInfo;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.ll_birthday).setOnClickListener(this);
        findViewById(R.id.ll_head).setOnClickListener(this);
        findViewById(R.id.ll_address).setOnClickListener(this);
        findViewById(R.id.ll_detail_address).setOnClickListener(this);
        findViewById(R.id.ll_motion).setOnClickListener(this);
        findViewById(R.id.ll_sex).setOnClickListener(this);
        findViewById(R.id.ll_number).setOnClickListener(this);
        findViewById(R.id.ll_nickname).setOnClickListener(this);
        findViewById(R.id.self_back).setOnClickListener(this);

        mCtx = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        userInfo = TribeApplication.getInstance().getUserInfo();
        if (null != userInfo) {
            FresoUtils.loadImage(userInfo.getPicture(), header);
            mName.setText(userInfo.getNickname());
            String value = userInfo.getSex();
            setSelfSex(value);
            setSelfEmotion(userInfo.getEmotion());
            mPhone.setText(userInfo.getPhone());
            mAddress.setText(userInfo.getArea());

            String birthday = userInfo.getBirthday();
            if (birthday != null) {
                String text = TribeDateUtils.dateFormat5(new Date(Long.parseLong(birthday)));
                mBirthday.setText(text);
            }
            UserAddressEntity entity = new AddressInfoDao().find(userInfo.getId(), userInfo.getAddressID());
            if (null!=entity){
                String defaultsAddress = entity.getArea()+entity.getAddress();
                mDetailAddress.setText(defaultsAddress);
            }
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_self;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new SelfPresenter();
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(mCtx,ModifyInfoActivity.class);
        switch (view.getId()) {
            case R.id.ll_head:
                ChoosePhotoPanel window = new ChoosePhotoPanel(this, new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String path) {
                        showLoadingDialog();
                        TribeUploader.getInstance().uploadFile("head", "", path, new TribeUploader.UploadCallback() {
                            @Override
                            public void uploadSuccess(final UploadAccessResponse.UploadResponseBody data) {
                                ((SelfPresenter) mPresenter).updateUser(Constant.PICTURE,data.objectKey);
                            }
                            @Override
                            public void uploadFail() {
                                ToastUtils.ToastMessage(mCtx,R.string.connect_fail);
                            }
                        });
                    }
                });
                window.show();
                break;
            case R.id.ll_nickname:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.NICKNAME);
                startActivityForResult(intent,201);
                break;
            case R.id.ll_sex:
                if (TribeApplication.getInstance().getUserInfo().getIdNo()!=null){
                    ToastUtils.ToastMessage(getCtx(),"您已身份认证，无法修改性别");
                    return;
                }
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.SEX);
                startActivityForResult(intent,202);
                break;
            case R.id.ll_birthday:
                if (TribeApplication.getInstance().getUserInfo().getIdNo()!=null){
                    ToastUtils.ToastMessage(getCtx(),"您已身份认证，无法修改出生日期");
                    return;
                }
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.BIRTHDAY);
                intent.putExtra(Constant.BIRTHDAY,mBirthday.getText().toString().trim());
                startActivityForResult(intent,203);
                break;
            case R.id.ll_motion:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.EMOTION);
                startActivityForResult(intent,204);
                break;
            case R.id.ll_number:
                startActivity(new Intent(this, PhoneVerifyActivity.class));
                break;
            case R.id.ll_address:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.ADDRESS);
                startActivityForResult(intent,205);
                break;

            case R.id.ll_detail_address:
                startActivityForResult(new Intent(this, AddressListActivity.class), addressCode);
                break;
            case R.id.self_back:
                finish();
                break;
        }
    }


    private void setSelfEmotion(String value) {
        if (TextUtils.equals(value, "SINGLE")) {
            value = getString(R.string.single);
        } else if (TextUtils.equals(value, "LOVE")) {
            value = getString(R.string.loving);
        } else if (TextUtils.equals(value, "MARRIED")) {
            value = getString(R.string.married);
        } else {
            value = "";
        }
        mMotion.setText(value);
    }

    private void setSelfSex(String value) {
        if (TextUtils.equals(value, "MALE")) {
            value = getString(R.string.male);
        } else if (TextUtils.equals(value, "FEMALE")) {
            value = getString(R.string.female);
        } else {
            value = "";
        }
        mSex.setText(value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 201:
                    mName.setText(data.getStringExtra(Constant.NICKNAME));
                    break;
                case 202:
                    mSex.setText(data.getStringExtra(Constant.SEX));
                    break;
                case 203:
                    String value = data.getStringExtra(Constant.BIRTHDAY);
                    if (value!=null)mBirthday.setText(TribeDateUtils.dateFormat5(new Date(Long.parseLong(value))));
                    break;
                case 204:
                    mMotion.setText(data.getStringExtra(Constant.EMOTION));
                    break;
                case 205:
                    mAddress.setText(data.getStringExtra(Constant.ADDRESS));
                    break;
            }
        }
    }

    @Override
    public void updateSuccess(String key, String value) {  //本页只能修改头像，其余在SelfPresenter中
        userInfo.setPicture(value);
        SelfEvent event = new SelfEvent();
        event.head = value;
        EventBus.getDefault().post(event);
        FresoUtils.loadImage(event.head,header);
        new UserInfoDao().update(userInfo);
        dismissDialog();
    }

    @Override
    public void showError(int res) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
