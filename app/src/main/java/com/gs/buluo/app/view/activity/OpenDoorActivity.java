package com.gs.buluo.app.view.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.util.LogTime;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CompanyPlate;
import com.gs.buluo.app.utils.BitmapUtils;
import com.gs.buluo.app.view.widget.RippleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.Bind;

public class OpenDoorActivity extends BaseActivity implements RippleView.RippleStateListener, View.OnClickListener {

    @Bind(R.id.open_door_rippleView)
    RippleView mRippleView;
    @Bind(R.id.open_door_text)
    TextView mTextView;
    @Bind(R.id.open_door_down)
    ImageView mImageView;
    private View mRootView;
    private static final String TAG = "OpenDoorActivity";
    private Bitmap mBitmap;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRippleView.setRippleStateListener(this);
        mTextView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mRootView = getRootView();

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra(Constant.PICTURE);
        mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mRootView.setBackground(new BitmapDrawable(mBitmap));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
