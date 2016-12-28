package com.gs.buluo.app.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by hjn on 2016/11/2.
 */
public class FrescoImageLoader extends ImageLoader {
    private  boolean isLocal=false;

    public FrescoImageLoader(boolean isLocal) {
        this.isLocal=isLocal;
    }

    public FrescoImageLoader() {
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (isLocal){
            imageView.setBackgroundResource((int)path);
            return;
        }
        if (path==null)return;
        String url=path.toString();
        if (!url.contains("://")) {
            url = Constant.Base.BASE_IMG_URL+url;
        }else {
            url = transformUrl(url);
        }

        Uri uri = Uri.parse(url);
        imageView.setImageURI(uri);
    }

    @Override
    public ImageView createImageView(Context context) {
        SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
        return simpleDraweeView;
    }

    public  String transformUrl(String url) {
        String[] arrs = url.split("\\://");
        String head = arrs[0];
        String body = arrs[1];
        switch (head){
            case "oss":
                return Constant.Base.BASE_ALI_URL+body;
            default:
                return Constant.Base.BASE_IMG_URL+body;
        }
    }


    public static String formatImageUrl(String url){
        String ret = url;
        if (url.contains("://")){
            String[] arrs = url.split("\\://");
            String head = arrs[0];
            String body = arrs[1];
            switch (head){
                case "oss":
                    ret= Constant.Base.BASE_ALI_URL+body;
                    break;
                default:
                    ret= Constant.Base.BASE_IMG_URL+body;
            }
        }else {
            ret=Constant.Base.BASE_IMG_URL+url;
        }

        return ret;
    }
}
