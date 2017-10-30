package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;

import butterknife.BindView;

/**
 * Created by Solang on 2017/10/20.
 */

public class BoardroomAlertActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.v_check1)
    View vCheck1;
    @BindView(R.id.v_check2)
    View vCheck2;
    @BindView(R.id.v_check3)
    View vCheck3;
    @BindView(R.id.v_check4)
    View vCheck4;
    @BindView(R.id.v_check5)
    View vCheck5;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_alert;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.ll_alert1).setOnClickListener(this);
        findViewById(R.id.ll_alert2).setOnClickListener(this);
        findViewById(R.id.ll_alert3).setOnClickListener(this);
        findViewById(R.id.ll_alert4).setOnClickListener(this);
        findViewById(R.id.ll_alert5).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_alert1:
                setAllCheckGone();
                vCheck1.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_alert2:
                setAllCheckGone();
                vCheck2.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_alert3:
                setAllCheckGone();
                vCheck3.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_alert4:
                setAllCheckGone();
                vCheck4.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_alert5:
                setAllCheckGone();
                vCheck5.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_confirm:
                Intent intent = new Intent(this,BoardroomReserveActivity.class);
                setData(intent);
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void setData(Intent intent) {
        int data = 1;
        if (vCheck1.getVisibility() == View.VISIBLE){
            data = 1;
        }else if (vCheck2.getVisibility() == View.VISIBLE){
            data = 2;
        }
        else if (vCheck3.getVisibility() == View.VISIBLE){
            data =3;
        }
        else if (vCheck4.getVisibility() == View.VISIBLE){
            data = 4;
        }
        else if (vCheck5.getVisibility() == View.VISIBLE){
            data = 5;
        }
        intent.putExtra(Constant.BOARDROOM_ALERT_TIME, data);
    }

    private void setAllCheckGone() {
        vCheck1.setVisibility(View.GONE);
        vCheck2.setVisibility(View.GONE);
        vCheck3.setVisibility(View.GONE);
        vCheck4.setVisibility(View.GONE);
        vCheck5.setVisibility(View.GONE);
    }
}
