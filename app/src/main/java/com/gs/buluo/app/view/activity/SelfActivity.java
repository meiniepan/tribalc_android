package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.SelfPresenter;
import com.gs.buluo.app.view.impl.ISelfView;

import butterknife.Bind;
import me.leefeng.citypicker.CityPicker;
import me.leefeng.citypicker.CityPickerListener;


/**
 * Created by hjn on 2016/11/2.
 */
public class SelfActivity extends BaseActivity implements View.OnClickListener,ISelfView {
    @Bind(R.id.tv_birthday)
    TextView mBirthday;
    @Bind(R.id.tv_address)
    TextView mAddress;

    public final int addressCode = 200;

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
            case R.id.ll_address:
                initAddressPicker();
                break;
            case R.id.ll_birthday:
                initBirthdayPicker();
                break;
            case R.id.ll_head:

                break;
            case R.id.ll_number:

                break;
            case R.id.ll_sex:
                break;
            case R.id.ll_motion:
                break;
            case R.id.ll_detail_address:
                startActivityForResult(new Intent(this,DetailAddressActivity.class),addressCode);
                break;

        }
    }

    private void initAddressPicker() {
        final CityPicker cityPicker = new CityPicker(this, new CityPickerListener() {
            @Override
            public void getCity(String name) {
                mAddress.setText(name);
            }
        });
        cityPicker.show();

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
}
