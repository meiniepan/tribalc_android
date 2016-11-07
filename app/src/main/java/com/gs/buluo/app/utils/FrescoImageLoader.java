package com.gs.buluo.app.utils;

import android.content.Context;
import android.widget.ImageView;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by hjn on 2016/11/2.
 */
public class FrescoImageLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setBackgroundResource((int)path);
    }
}
