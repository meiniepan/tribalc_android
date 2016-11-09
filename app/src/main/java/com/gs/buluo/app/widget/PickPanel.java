package com.gs.buluo.app.widget;

import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.BaseActivity;
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

public class PickPanel extends PopupWindow implements View.OnClickListener {

    private final BaseActivity mActivity;

    public PickPanel(SelfActivity activity){
        mActivity = activity;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.picker_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x0b000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        setAnimationStyle(R.style.take_photo_anim);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_photo:
                dismiss();
                break;
            case R.id.choose_photo:
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    private void setStyle(WheelPicker picker){

    }
}
