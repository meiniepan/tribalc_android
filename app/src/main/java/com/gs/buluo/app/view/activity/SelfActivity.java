package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.FirstEvent;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.SelfPresenter;
import com.gs.buluo.app.view.impl.ISelfView;
import com.gs.buluo.app.widget.ChoosePhotoPanel;
import com.gs.buluo.app.widget.ModifyInfoPanel;
import com.gs.buluo.app.widget.PickPanel;

import butterknife.Bind;
import de.greenrobot.event.EventBus;


/**
 * Created by hjn on 2016/11/2.
 */
public class SelfActivity extends BaseActivity implements View.OnClickListener,ISelfView {
    @Bind(R.id.tv_birthday)
    TextView mBirthday;
    @Bind(R.id.tv_address)
    TextView mAddress;
    @Bind(R.id.self_iv_head)
    SimpleDraweeView header;
    @Bind(R.id.tv_sex)
    TextView mSex;
    @Bind(R.id.tv_motion)
    TextView mMotion;
    @Bind(R.id.tv_nickname)
    TextView mName;
    @Bind(R.id. tv_number)
    TextView mPhone;

    public final int addressCode = 200;
    private ModifyInfoPanel panel;

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
        EventBus.getDefault().register(this);
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
        switch (view.getId()){
            case R.id.ll_nickname:
                panel = new ModifyInfoPanel(this,ModifyInfoPanel.NAME, new ModifyInfoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String string) {
                        mName.setText(string);
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
                ChoosePhotoPanel window=new ChoosePhotoPanel(this, new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String path) {
                        setHeader(path);
                    }
                });
                window.show();
                break;
            case R.id.ll_number:
               startActivity(new Intent(this,PhoneVerifyActivity.class));
                break;
            case R.id.ll_sex:
                panel = new ModifyInfoPanel(this,ModifyInfoPanel.SEX, new ModifyInfoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String string) {
                        mSex.setText(string);
                    }
                });
                panel.show();
                break;
            case R.id.ll_motion:
               panel=new ModifyInfoPanel(this,ModifyInfoPanel.MOTION, new ModifyInfoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String string) {
                        mMotion.setText(string);
                    }
                });
                panel.show();
                break;
            case R.id.ll_detail_address:
                startActivityForResult(new Intent(this,DetailAddressActivity.class),addressCode);
                break;
            case R.id.self_back:
                finish();
                break;

        }
    }

    private void initAddressPicker() {
        PickPanel pickPanel=new PickPanel(this, new PickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String result) {
                mAddress.setText(result);
            }
        });
        pickPanel.show();
    }

    private void initBirthdayPicker() {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(SelfActivity.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                StringBuffer sb=new StringBuffer();
                sb.append(year).append("年").append(month).append("月").append(day).append("日");
                mBirthday.setText(sb.toString());
            }
        }).textConfirm(getString(R.string.yes)) //text of confirm button
                .textCancel(getString(R.string.no)) //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1990) //min year in loop
                .maxYear(2550) // max year in loop
                .dateChose("1990-11-11") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(this);
    }

    @Override
    public void showError() {

    }

    public void setHeader(String path) {
       header.setImageURI("file://"+path);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(FirstEvent event) {
        mPhone.setText(event.getMsg());
    }
}
