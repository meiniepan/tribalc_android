package com.gs.buluo.app.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.RequestBodyBean.MultiLockRequest;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/3/6.
 */

public class OpenDoorActivity extends BaseActivity implements View.OnClickListener {
    private int QR_WIDTH = 0;
    private int QR_HEIGHT = 0;
    private CountDownTimer countDownTimer;

    ImageView image, ivRefresh, ivOpenDoorBack;
    TextView tvDoor, tvDeadLine, tvTimeOverTips;
    LinearLayout ll_open_door, ll_door_name;
    @Bind(R.id.door_mine)
    ViewStub mineView;
    @Bind(R.id.door_visitor)
    ViewStub visitorView;
    @Bind(R.id.door_end_time)
    TextView endTime;

    private Bitmap bitmap;
    private static IWXAPI msgApi = null;
    private String code;
    private List<String> equips;
    private long second;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        msgApi = WXAPIFactory.createWXAPI(TribeApplication.getInstance().getApplicationContext(), null);
        // 将该app注册到微信
        msgApi.registerApp(Constant.Base.WX_ID);
        QR_WIDTH = DensityUtils.dip2px(this, 300);
        QR_HEIGHT = DensityUtils.dip2px(this, 300);

        LockKey key = getIntent().getParcelableExtra(Constant.DOOR);

        equips = getIntent().getStringArrayListExtra(Constant.EQUIP_LIST);
        if (key.phone == null) {       //本人开锁
            initView(key);
        } else {
            initVisitorView(key);
            findViewById(R.id.door_end_area).setVisibility(View.VISIBLE);
            endTime.setText(TribeDateUtils.dateFormat9(new Date(key.endTime)));
        }
        createQRImage(code);
    }

    private void initVisitorView(LockKey lockKey) {
        View view = visitorView.inflate();
        image = (ImageView) view.findViewById(R.id.qr_image);
        tvDoor = (TextView) view.findViewById(R.id.door_name);
        TextView tvName = (TextView) view.findViewById(R.id.door_visitor_name);
        TextView tvPhone = (TextView) view.findViewById(R.id.door_visitor_phone);
        tvDoor = (TextView) view.findViewById(R.id.door_name);
        view.findViewById(R.id.share_wx).setOnClickListener(this);
        view.findViewById(R.id.share_msg).setOnClickListener(this);

//        tvDoor.setText(lockKey.equipName);
        tvName.setText(lockKey.name);
        tvPhone.setText(lockKey.phone);
        code = lockKey.key;
    }

    private void initView(LockKey key) {
        View view = mineView.inflate();
        image = (ImageView) view.findViewById(R.id.qr_image);
        tvTimeOverTips = (TextView) view.findViewById(R.id.tv_time_over_tips);
        tvDoor = (TextView) view.findViewById(R.id.door_name);
//        tvDoor.setText(key.equipName);
        tvDeadLine = (TextView) view.findViewById(R.id.door_dead_line);
        ivRefresh = (ImageView) view.findViewById(R.id.iv_open_door_refresh);
        ivOpenDoorBack = (ImageView) view.findViewById(R.id.iv_open_door_back);
        ll_open_door = (LinearLayout) view.findViewById(R.id.ll_open_door);
        ll_door_name = (LinearLayout) view.findViewById(R.id.ll_door_name);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getKeyAgain();
            }
        });
        code = key.key;

        second = (key.endTime - System.currentTimeMillis())/1000;
        if (second<=0){
            tvDeadLine.setText(0 + "");
            ivOpenDoorBack.setVisibility(View.INVISIBLE);
            ivRefresh.setVisibility(View.VISIBLE);
            ll_door_name.setVisibility(View.GONE);
            tvTimeOverTips.setVisibility(View.VISIBLE);
            image.setAlpha(.3f);
        }

        countDownTimer = new CountDownTimer(second*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvDeadLine.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                tvDeadLine.setText(0 + "");
                ivOpenDoorBack.setVisibility(View.INVISIBLE);
                ivRefresh.setVisibility(View.VISIBLE);
                ll_door_name.setVisibility(View.GONE);
                tvTimeOverTips.setVisibility(View.VISIBLE);
                image.setAlpha(.3f);

            }
        };
        countDownTimer.start();
    }

    public void createQRImage(String url) {
        try {
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            image.setImageBitmap(bitmap);
            CommonUtils.saveBitmap2file(bitmap, "qr_image");
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_qr_show;
    }


    @Override
    public void onClick(View v) {
        if (bitmap == null) return;
        switch (v.getId()) {
            case R.id.share_wx:
                shareWeChat(Constant.DIR_PATH + "qr_image");
                break;
            case R.id.share_msg:
                shareMsg(Constant.DIR_PATH + "qr_image");
                break;
        }
    }

    private void shareMsg(String path) {
        File file = new File(path);
        Uri uriToImage = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    private void shareWeChat(String path) {
        if (!CommonUtils.checkInstallation("com.tencent.mm")) {
            ToastUtils.ToastMessage(getCtx(), "您当前未安装微信");
            return;
        }
        File file = new File(path);
        Uri uriToImage = Uri.fromFile(file);
        Intent shareIntent = new Intent();
        //发送图片到朋友圈
        //ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        //发送图片给好友。
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        shareIntent.setComponent(comp);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    public void getKeyAgain() {
        MultiLockRequest request = new MultiLockRequest();
        if (equips!=null){
            request.equipIds = equips;
        }
        TribeRetrofit.getInstance().createApi(DoorApis.class).getMultiKey(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<LockKey>>() {
                    @Override
                    public void onNext(BaseResponse<LockKey> response) {
                        createQRImage(response.data.key);
                        ivOpenDoorBack.setVisibility(View.VISIBLE);
                        ivRefresh.setVisibility(View.GONE);
                        ll_door_name.setVisibility(View.VISIBLE);
                        tvTimeOverTips.setVisibility(View.GONE);
                        long l = response.data.endTime - System.currentTimeMillis();
                        second = l /1000;
                        countDownTimer.start();
                        image.setAlpha(1f);
                    }
                });
    }
}
