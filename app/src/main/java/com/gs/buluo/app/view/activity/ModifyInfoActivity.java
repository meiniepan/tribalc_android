package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.SelfPresenter;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.impl.ISelfView;
import com.gs.buluo.app.view.widget.panel.AddressPickPanel;
import com.gs.buluo.app.view.widget.panel.DatePickPanel;
import com.gs.buluo.common.widget.LoadingDialog;
import java.util.Calendar;
import butterknife.BindView;

/**
 * Created by hjn on 2016/12/15.
 */
public class ModifyInfoActivity extends BaseActivity implements View.OnClickListener, ISelfView {
    @BindView(R.id.modify_save)
    TextView save;
    @BindView(R.id.modify_title)
    TextView title;
    private String info;

    private Intent intent;
    private String oldData = "1990-11-11";
    private Calendar date;
    private String addressResult;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        info = getIntent().getStringExtra(Constant.ForIntent.MODIFY);
        oldData = getIntent().getStringExtra(Constant.BIRTHDAY);
        intent = new Intent();
        findViewById(R.id.modify_back).setOnClickListener(this);
        initView(info);
    }

    private void initView(final String info) {
        switch (info) {
            case Constant.NICKNAME:
                View nameView = ((ViewStub) findViewById(R.id.modify_nickname)).inflate();
                final EditText name = (EditText) nameView.findViewById(R.id.modify_nickname_edit);
                title.setText(R.string.nickname);
                save.setVisibility(View.VISIBLE);
                showKeyBoard(name);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (name.length() == 0) return;
                        ((SelfPresenter) mPresenter).updateUser(Constant.NICKNAME, name.getText().toString().trim());
                    }
                });
                break;
            case Constant.SEX:
                View sexView = ((ViewStub) findViewById(R.id.modify_sex)).inflate();
                title.setText(R.string.sex);
                sexView.findViewById(R.id.modify_male).setOnClickListener(this);
                sexView.findViewById(R.id.modify_female).setOnClickListener(this);
                break;
            case Constant.BIRTHDAY:
                title.setText(R.string.birthday);
                View birthdayView = ((ViewStub) findViewById(R.id.modify_birthday)).inflate();
                final TextView birthday = (TextView) birthdayView.findViewById(R.id.modify_birthday_text);
                birthday.setText(oldData);
//                initBirthdayPicker(birthday);
                initDatePickPanel(birthday);
                save.setVisibility(View.VISIBLE);
                birthday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        initBirthdayPicker(birthday);
                        initDatePickPanel(birthday);
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (birthday.length() == 0 || date == null) return;
                        showLoadingDialog();
                        ((SelfPresenter) mPresenter).updateUser(Constant.BIRTHDAY, date.getTimeInMillis() + "");
                    }
                });
                break;
            case Constant.EMOTION:
                title.setText(R.string.motion);
                View motionView = ((ViewStub) findViewById(R.id.modify_motion)).inflate();
                motionView.findViewById(R.id.modify_single).setOnClickListener(this);
                motionView.findViewById(R.id.modify_married).setOnClickListener(this);
                motionView.findViewById(R.id.modify_loving).setOnClickListener(this);
                break;
            case Constant.ADDRESS:
                title.setText(R.string.address);
                final View addressView = ((ViewStub) findViewById(R.id.modify_address)).inflate();
                final TextView address = (TextView) addressView.findViewById(R.id.modify_address_layout);
                save.setVisibility(View.VISIBLE);
                address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initAddressPicker(address);
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (address.length() == 0 || addressResult == null) return;
                        showLoadingDialog();
                        ((SelfPresenter) mPresenter).updateUser(Constant.AREA, addressResult);
                    }
                });
                initAddressPicker(address);
                break;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.self_modification;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_male:
                updateSex(R.string.male);
                break;
            case R.id.modify_female:
                updateSex(R.string.female);
                break;
            case R.id.modify_single:
                updateMotion(R.string.single);
                break;
            case R.id.modify_loving:
                updateMotion(R.string.loving);
                break;
            case R.id.modify_married:
                updateMotion(R.string.married);
                break;
            case R.id.modify_back:
                finish();
                break;
        }
    }

    @Override
    public void updateSuccess(String key, String value) {
        LoadingDialog.getInstance().dismissDialog();
        switch (key) {
            case Constant.NICKNAME:
                intent.putExtra(Constant.NICKNAME, value);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case Constant.SEX:
                setSelfSex(value);
                break;
            case Constant.BIRTHDAY:
                intent.putExtra(Constant.BIRTHDAY, value);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case Constant.EMOTION:
                setSelfEmotion(value);
                break;
            case Constant.AREA:
                intent.putExtra(Constant.ADDRESS, value);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    public void showKeyBoard(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
            }
        }, 200);
    }

    @Override
    public void showError(int res) {
        dismissDialog();
        ToastUtils.ToastMessage(this, getString(res));
    }

    @Override
    protected BasePresenter getPresenter() {
        return new SelfPresenter();
    }

    private void updateSex(int sex) {
        String value;
        if (sex == R.string.male) {
            value = Constant.MALE;
        } else {
            value = Constant.FEMALE;
        }
        showLoadingDialog();
        ((SelfPresenter) mPresenter).updateUser(Constant.SEX, value);
    }

    private void initAddressPicker(final TextView address) {
        AddressPickPanel pickPanel = new AddressPickPanel(this, new AddressPickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String result) {
                addressResult = result;
                address.setText(addressResult);
            }
        });
        pickPanel.show();
    }

    //    private void initBirthdayPicker(final TextView birthday) {
//        new Handler().postDelayed(new TimerTask() {
//            @Override
//            public void run() {
//                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(ModifyInfoActivity.this, new DatePickerPopWin.OnDatePickedListener() {
//                    @Override
//                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//                        StringBuilder sb = new StringBuilder();
//                        month = month - 1;
//                        sb.append(year).append("年").append(month+1).append("月").append(day).append("日");
//                        date = Calendar.getInstance();
//                        date.set(Calendar.YEAR, year);
//                        date.set(Calendar.MONTH, month);
//                        date.set(Calendar.DAY_OF_MONTH, day);
//                        birthday.setText(sb.toString());
//                    }
//                }).textConfirm(getString(R.string.yes)) //text of confirm button
//                        .textCancel(getString(R.string.cancel)) //text of cancel button
//                        .btnTextSize(16) // button text size
//                        .viewTextSize(25) // pick view text size
//                        .colorCancel(Color.parseColor("#999999")) //color of cancel button
//                        .colorConfirm(Color.parseColor("#009900"))//color of confirm button
//                        .minYear(1970) //min year in loop
//                        .maxYear(2210) // max year in loop
//                        .dateChose(oldData) // date chose when init popwindow
//                        .build();
//                pickerPopWin.showPopWin(ModifyInfoActivity.this);
//            }
//        },300);
//    }
    private void initDatePickPanel(final TextView birthday) {
        DatePickPanel pickPanel = new DatePickPanel(this, new DatePickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(int year, int month, int day) {
                StringBuilder sb = new StringBuilder();
                sb.append(year).append("年").append(month).append("月").append(day).append("日");
                date = Calendar.getInstance();
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, month - 1);
                date.set(Calendar.DAY_OF_MONTH, day);
                birthday.setText(sb.toString());
            }
        });
        pickPanel.show();
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
        intent.putExtra(Constant.EMOTION, value);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setSelfSex(String value) {
        if (TextUtils.equals(value, "MALE")) {
            intent.putExtra(Constant.SEX, getString(R.string.male));
        } else {
            intent.putExtra(Constant.SEX, getString(R.string.female));
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateMotion(int motion) {
        showLoadingDialog();
        String emotion;
        if (motion == R.string.single) {
            emotion = Constant.SINGLE;
        } else if (motion == R.string.married) {
            emotion = Constant.MARRIED;
        } else {
            emotion = Constant.LOVE;
        }
        ((SelfPresenter) mPresenter).updateUser(Constant.EMOTION, emotion);
    }

}
