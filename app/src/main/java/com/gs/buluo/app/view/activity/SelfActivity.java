package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.eventbus.FirstEvent;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.eventbus.NameEvent;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.SelfPresenter;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.impl.ISelfView;
import com.gs.buluo.app.widget.ChoosePhotoPanel;
import com.gs.buluo.app.widget.LoadingDialog;
import com.gs.buluo.app.widget.ModifyInfoPanel;
import com.gs.buluo.app.widget.PickPanel;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import de.greenrobot.event.EventBus;


/**
 * Created by hjn on 2016/11/2.
 */
public class SelfActivity extends BaseActivity implements View.OnClickListener, ISelfView {
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
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);

        userInfo = TribeApplication.getInstance().getUserInfo();
        mCtx = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (null != userInfo) {
            FresoUtils.loadImage(userInfo.getPicture(), header);
            mName.setText(userInfo.getNickname());
            String value = userInfo.getSex();
            setSelfSex(value);
            setSelfEmotion(userInfo.getEmotion());
            UserSensitiveEntity first = new UserSensitiveDao().findFirst();
            mPhone.setText(first.getPhone());
            mAddress.setText(userInfo.getArea());

            String birthday = userInfo.getBirthday();
            if (birthday != null) {
                String text = TribeDateUtils.dateFormat5(new Date(Long.parseLong(birthday)));
                mBirthday.setText(text);
            }
            UserAddressEntity entity = new AddressInfoDao().find(userInfo.getId(), first.getAddressID());
            if (null!=entity){
                String defaultsAddress = entity.getArea()+entity.getDetailAddress();
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
        switch (view.getId()) {
            case R.id.ll_nickname:
                panel = new ModifyInfoPanel(this, ModifyInfoPanel.NAME, new ModifyInfoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String name) {
                        showDialog();
                        ((SelfPresenter) mPresenter).updateUser(Constant.NICKNAME, name);
                    }
                });
                panel.show();
                break;
            case R.id.ll_address:
                initAddressPicker();
                break;
            case R.id.ll_birthday:
                initBirthdayPicker();
                break;
            case R.id.ll_head:
                ChoosePhotoPanel window = new ChoosePhotoPanel(this, new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String path) {

                    }
                });
                window.show();
                break;
            case R.id.ll_number:
                startActivity(new Intent(this, PhoneVerifyActivity.class));
                break;
            case R.id.ll_sex:
                panel = new ModifyInfoPanel(this, ModifyInfoPanel.SEX, new ModifyInfoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String sex) {
                        showDialog();
                        String value;
                        if (TextUtils.equals(sex, getString(R.string.male))) {
                            value = Constant.MALE;
                        } else {
                            value = Constant.FEMALE;
                        }
                        ((SelfPresenter) mPresenter).updateUser(Constant.SEX, value);
                    }
                });
                panel.show();
                break;
            case R.id.ll_motion:
                panel = new ModifyInfoPanel(this, ModifyInfoPanel.MOTION, new ModifyInfoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String motion) {
                        showDialog();
                        String emotion;
                        if (TextUtils.equals(motion, getString(R.string.single))) {
                            emotion = Constant.SINGLE;
                        } else if (TextUtils.equals(motion, getString(R.string.married))) {
                            emotion = Constant.MARRIED;
                        } else {
                            emotion = Constant.LOVE;
                        }
                        ((SelfPresenter) mPresenter).updateUser(Constant.EMOTION, emotion);
                    }
                });
                panel.show();
                break;
            case R.id.ll_detail_address:
                startActivityForResult(new Intent(this, AddressListActivity.class), addressCode);
                break;
            case R.id.self_back:
                finish();
                break;
        }
    }

    private void initAddressPicker() {
        PickPanel pickPanel = new PickPanel(this, new PickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String result) {
                showDialog();
                ((SelfPresenter) mPresenter).updateUser(Constant.AREA, result);
            }
        });
        pickPanel.show();
    }

    private void initBirthdayPicker() {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(SelfActivity.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                showDialog();
                StringBuffer sb = new StringBuffer();
                sb.append(year).append("年").append(month).append("月").append(day).append("日");
                Calendar date = Calendar.getInstance();
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, month);
                date.set(Calendar.DAY_OF_MONTH, day);

                ((SelfPresenter) mPresenter).updateUser(Constant.BIRTHDAY, date.getTimeInMillis() + "");
            }
        }).textConfirm(getString(R.string.yes)) //text of confirm button
                .textCancel(getString(R.string.no)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1960) //min year in loop
                .maxYear(2210) // max year in loop
                .dateChose("1990-11-11") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(this);
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, getString(res));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(FirstEvent event) {
        mPhone.setText(event.getMsg());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void updateSuccess(String key, String value) {
        LoadingDialog.getInstance().dismissDialog();
        switch (key) {
            case Constant.PICTURE:
                break;
            case Constant.NICKNAME:
                mName.setText(value);
                userInfo.setNickname(value);
                EventBus.getDefault().post(new NameEvent(value));
                break;
            case Constant.SEX:
                setSelfSex(value);
                userInfo.setSex(value);
                break;
            case Constant.BIRTHDAY:
                mBirthday.setText(TribeDateUtils.dateFormat5(new Date(Long.parseLong(value))));
                userInfo.setBirthday(value);
                break;
            case Constant.EMOTION:
                setSelfEmotion(value);
                userInfo.setEmotion(value);
                break;
            case Constant.PHONE:
                mPhone.setText(new UserSensitiveDao().findFirst().getPhone());
                break;
            case Constant.AREA:
                userInfo.setArea(value);
                mAddress.setText(value);
                break;
        }
        new UserInfoDao().update(userInfo);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
