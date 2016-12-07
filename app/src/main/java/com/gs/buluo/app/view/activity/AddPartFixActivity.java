package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.view.widget.ChoosePhotoPanel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class AddPartFixActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.add_part_image)
    ImageView mAddImageView;
    @Bind(R.id.add_fix_image_group)
    ViewGroup mViewGroup;

    private static final String TAG = "AddPartFixActivity";
    private int color = R.color.titlebar_background;
    int picCount=0;
    Context mCtx;
    List<ImageView> mImageList=new ArrayList<ImageView>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.add_part_fix_back).setOnClickListener(this);
        findViewById(R.id.add_part_image).setOnClickListener(this);
        mCtx=this;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_part_fix;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_part_fix_back:
                finish();
                break;
            case R.id.add_part_image:
                ChoosePhotoPanel choosePhotoPanel = new ChoosePhotoPanel(this, new ChoosePhotoPanel.OnSelectedFinished() {
                    @Override
                    public void onSelected(String string) {
                        Log.d(TAG, "onSelected: "+string);
                        picCount++;
                        if (mImageList.size()>=2){
                            mAddImageView.setVisibility(View.INVISIBLE);
                        }
                        FrameLayout frameLayout = new FrameLayout(mCtx);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dip2px(mCtx, 100), DensityUtils.dip2px(mCtx, 100));
                        lp.setMargins(DensityUtils.dip2px(mCtx,4),0,DensityUtils.dip2px(mCtx,4),0);
                        frameLayout.setLayoutParams(lp);
                        ImageView imageView = new ImageView(mCtx);
                        imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        Glide.with(mCtx).load(string).into(imageView);
                        frameLayout.addView(imageView);
                        frameLayout.setTag(imageView);

                        ImageView del = new ImageView(mCtx);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DensityUtils.dip2px(mCtx, 20), DensityUtils.dip2px(mCtx, 20));
                        params.setMargins(DensityUtils.dip2px(mCtx, 80),0,0,0);
                        del.setLayoutParams(params);
                        del.setImageResource(R.mipmap.del_pic);
                        del.setTag(frameLayout);
                        del.setOnClickListener((View.OnClickListener) mCtx);
                        frameLayout.addView(del);
                        
                        mImageList.add(imageView);
                        mViewGroup.addView(frameLayout);
                    }
                });
                choosePhotoPanel.show();
                break;
            default:
                if (view instanceof ImageView){
                    FrameLayout tag = (FrameLayout) view.getTag();
                    ImageView imgTag = (ImageView) tag.getTag();
                    mViewGroup.removeView(tag);
                    mImageList.remove(imgTag);
                    if (mImageList.size()<=2){
                        mAddImageView.setVisibility(View.VISIBLE);
                    }
                }
                break;

        }
    }
}
