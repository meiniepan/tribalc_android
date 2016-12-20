package com.gs.buluo.app.view.activity;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.RippleView;

import butterknife.Bind;

public class OpenDoorActivity extends BaseActivity implements RippleView.RippleStateListener, View.OnClickListener {

    @Bind(R.id.open_door_rippleView)
    RippleView mRippleView;
    @Bind(R.id.open_door_text)
    TextView mTextView;
    @Bind(R.id.open_door_down)
    ImageView mImageView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRippleView.setRippleStateListener(this);
        mTextView.setOnClickListener(this);
        mImageView.setOnClickListener(this);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_open_door;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_door_text:
                mRippleView.startRipple();
                break;
            case R.id.open_door_down:
                finish();
                break;

        }
    }

    @Override
    public void startRipple() {

    }

    @Override
    public void stopRipple() {

    }

    @Override
    public void onRippleUpdate(ValueAnimator animation) {

    }

}
