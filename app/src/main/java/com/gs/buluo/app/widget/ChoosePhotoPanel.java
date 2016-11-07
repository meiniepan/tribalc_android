package com.gs.buluo.app.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.SelfActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by jingnan on 2016/5/26.
 */

public class ChoosePhotoPanel extends PopupWindow implements View.OnClickListener {
    private static final int REQUEST_CODE_GALLERY = 20001;
    private static final int REQUEST_CODE_CROP = 20002;
    private static final int REQUEST_CODE_CAMERA = 20003;

    public static final String IMAGE_FILE_DIR = Environment.getExternalStorageDirectory().getPath() + "/tribe/image_cache";
    @Bind(R.id.take_photo)
    TextView take;
    @Bind(R.id.choose_photo)
    TextView choose;
    @Bind(R.id.cancel)
    TextView cancel;
    private  SelfActivity mActivity;
    private GalleryFinal.OnHanlderResultCallback onHanlderResultCallback;

    public ChoosePhotoPanel(SelfActivity activity){
        mActivity = activity;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.photo_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setTouchable(true);
        take.setOnClickListener(this);
        choose.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ColorDrawable dw = new ColorDrawable(0x0b000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        setAnimationStyle(R.style.take_photo_anim);

        onHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode==REQUEST_CODE_GALLERY){
//                    GalleryFinal.openCrop(REQUEST_CODE_CROP, IMAGE_FILE_DIR,onHanlderResultCallback);
                    mActivity.setHeader(resultList.get(0).getPhotoPath());
                }else if (reqeustCode==REQUEST_CODE_CAMERA){
//                    GalleryFinal.openCrop(REQUEST_CODE_CROP, IMAGE_FILE_DIR,onHanlderResultCallback);
                    mActivity.setHeader(resultList.get(0).getPhotoPath());
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtils.ToastMessage(mActivity,errorMsg);
            }
        };
    }

    @Override
    public void onClick(View v) {
        FunctionConfig config =new FunctionConfig.Builder()
                .setEnableCrop(true)
                        .setCropHeight(DensityUtils.dip2px(mActivity,30))
                        .setCropWidth(DensityUtils.dip2px(mActivity,30))
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .setEnablePreview(true)
                .build();
        switch (v.getId()){
            case R.id.take_photo:
                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, onHanlderResultCallback);
                dismiss();
                break;
            case R.id.choose_photo:
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,onHanlderResultCallback);
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

}
