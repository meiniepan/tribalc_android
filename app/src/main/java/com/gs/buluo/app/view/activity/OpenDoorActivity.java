package com.gs.buluo.app.view.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
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
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CompanyPlate;
import com.gs.buluo.app.bean.RequestBodyBean.OpenDoorRequestBody;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.network.OpenDoorService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.BitmapUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.RippleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OpenDoorActivity extends BaseActivity implements RippleView.RippleStateListener, View.OnClickListener {

    @Bind(R.id.open_door_rippleView)
    RippleView mRippleView;
    @Bind(R.id.open_door_text)
    TextView mTextView;
    @Bind(R.id.open_door_down)
    ImageView mImageView;
    @Bind(R.id.open_door_lock)
    ImageView mLockImg;
    private View mRootView;
    private static final String TAG = "OpenDoorActivity";
    private Bitmap mBitmap;
    public Context mContext;
    public Handler mHandler=new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what==1) {
                mLockImg.setVisibility(View.INVISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                mRippleView.stopRipple();
            }
        }
    };

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRippleView.setRippleStateListener(this);
        mTextView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mRootView = getRootView();
        mContext=this;

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
                final OpenDoorRequestBody requestBody = new OpenDoorRequestBody();
                requestBody.value="gate_01@dyc.bj";
                TribeRetrofit.getInstance().createApi(OpenDoorService.class).postOpenDoor(TribeApplication.getInstance().getUserInfo().getId(),requestBody).enqueue(new Callback<SimpleCodeResponse>() {
                    @Override
                    public void onResponse(Call<SimpleCodeResponse> call, Response<SimpleCodeResponse> response) {
                        switch (response.body().code) {
                            case 200:
                                mLockImg.setVisibility(View.VISIBLE);
                                mTextView.setVisibility(View.INVISIBLE);

                                mHandler.sendEmptyMessageDelayed(1,3000);

                                break;

                            case 403:
                                mTextView.setText("开锁失败");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleCodeResponse> call, Throwable t) {
                        ToastUtils.ToastMessage(mContext,"网络请求失败");
                    }
                });
                break;
            case R.id.open_door_down:
                finish();
                overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
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
