package com.gs.buluo.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gs.buluo.app.Constant;

import java.io.File;

/**
 * Created by hjn on 2016/11/1.
 */
public class FresoUtils {
    public static void loadImage(String url, SimpleDraweeView imageView, ControllerListener listener) {
        if (TextUtils.isEmpty(url)) {
            url = "http://";
        }
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
//                . // other setters
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setControllerListener(listener)
//                . // other setters
                .build();
        imageView.setController(controller);
    }

    public static void loadImage(String url, SimpleDraweeView imageView) {
        if (url==null)return;
        if (!url.contains("://")) {
            url = Constant.Base.BASE_IMG_URL+url;
        }else {
            url = transformUrl(url);
        }

        Uri uri = Uri.parse(url);
        imageView.setImageURI(uri);
    }

    private static String transformUrl(String url) {
        String[] arrs = url.split("://");
        String head = arrs[0];
        String body = arrs[1];
        switch (head){
            case "oss":
                return Constant.Base.BASE_ALI_URL+body;
            default:
                return Constant.Base.BASE_IMG_URL+body;
        }
    }


    /**
     * 获取适用于Fresco本地图片资源Url
     */
    public static String getFrescoLocalResUrl(Context mContext, int resId) {
        return "res://" + mContext.getPackageName() + "/" + resId;
    }

    /**
     * 获取适用于Fresco本地文件图片资源
     */
    public static String getFrescoLocalFile(File file) {
        return "file://" + file.getPath();
    }


    public static void clear() {
        ImagePipeline mImagePipeline = Fresco.getImagePipeline();
        mImagePipeline.clearMemoryCaches();
        mImagePipeline.clearDiskCaches();
        mImagePipeline.clearCaches();
    }

    /**
     * 拿到指定宽高，并经过Processor处理的bitmap
     * @param url
     * @param context
     * @param width
     * @param height
     * @param processor 后处理器,可为null
     * @param listener
     *
     */
    public static Bitmap getBitmapWithProcessor(String url, Context context, int width, int height,
                                                BasePostprocessor processor, final BitmapListener listener){

        ResizeOptions resizeOptions = null;
        if (width !=0 && height != 0 ){
            resizeOptions = new ResizeOptions(width, height);
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(false)
                .setPostprocessor(processor)
                .setResizeOptions(resizeOptions)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                listener.onSuccess(bitmap);
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                listener.onFail();
            }
        }, CallerThreadExecutor.getInstance());
        return null;
    }


    public static void setCircle( SimpleDraweeView draweeView,int bgColor){
        RoundingParams roundingParams = RoundingParams.asCircle();//这个方法在某些情况下无法成圆,比如gif
        roundingParams.setOverlayColor(bgColor);//加一层遮罩,这个是关键方法
        draweeView.getHierarchy().setRoundingParams(roundingParams);
    }

    public interface BitmapListener {
        void onSuccess(Bitmap bitmap);

        void onFail();
    }
}
