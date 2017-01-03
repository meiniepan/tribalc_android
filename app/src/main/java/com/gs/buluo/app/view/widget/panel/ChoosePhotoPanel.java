package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by jingnan on 2016/5/26.
 */

public class ChoosePhotoPanel extends Dialog implements View.OnClickListener {
    private static final int REQUEST_CODE_GALLERY = 20001;
    private static final int REQUEST_CODE_CROP = 20002;
    private static final int REQUEST_CODE_CAMERA = 20003;
    private OnSelectedFinished onSelectedFinished;

    public static final String IMAGE_FILE_DIR = Environment.getExternalStorageDirectory().getPath() + "/tribe/image_cache";
    @Bind(R.id.take_photo)
    TextView take;
    @Bind(R.id.choose_photo)
    TextView choose;
    @Bind(R.id.cancel)
    TextView cancel;
    private  Context mContext;
    private GalleryFinal.OnHanlderResultCallback onHanlderResultCallback;

    public ChoosePhotoPanel(Context context,OnSelectedFinished onSelectedFinished){
        super(context,R.style.my_dialog);
        this.onSelectedFinished=onSelectedFinished;
        mContext = context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.photo_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        take.setOnClickListener(this);
        choose.setOnClickListener(this);
        cancel.setOnClickListener(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        onHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode==REQUEST_CODE_GALLERY){
//                    GalleryFinal.openCrop(REQUEST_CODE_CROP, IMAGE_FILE_DIR,onHanlderResultCallback);
                    onSelectedFinished.onSelected(resultList.get(0).getPhotoPath());
                }else if (reqeustCode==REQUEST_CODE_CAMERA){
//                    GalleryFinal.openCrop(REQUEST_CODE_CROP, IMAGE_FILE_DIR,onHanlderResultCallback);
                    onSelectedFinished.onSelected(resultList.get(0).getPhotoPath());
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtils.ToastMessage(mContext,errorMsg);
            }
        };
    }

    @Override
    public void onClick(View v) {
        FunctionConfig config =new FunctionConfig.Builder()
                .setEnableCrop(true)
                        .setCropHeight(DensityUtils.dip2px(mContext,30))
                        .setCropWidth(DensityUtils.dip2px(mContext,30))
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
    public interface OnSelectedFinished{
        void onSelected(String string);
    }
}
