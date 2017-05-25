package com.gs.buluo.app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.UploadAccessBody;
import com.gs.buluo.app.bean.ResponseBody.UploadResponseBody;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.common.network.BaseResponse;

import org.xutils.common.util.MD5;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/24.
 */
public class TribeUploader {
    private static TribeUploader uploader;
    private Handler handler = new Handler();

    private TribeUploader() {
    }

    public static TribeUploader getInstance() {
        if (uploader == null) {
            uploader = new TribeUploader();
        }
        return uploader;
    }

    private List<File> vector = new Vector<>();

    public void uploadFile(final String name, String fileType, final String file, final UploadCallback callback) {  //压缩
        fileType = "image/jpeg";
        final String finalFileType = fileType;
        Observable.just(file)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<BaseResponse<UploadResponseBody>>>() {
                    @Override
                    public Observable<BaseResponse<UploadResponseBody>> call(String file) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file);
                        Bitmap newB = CommonUtils.compressBitmap(bitmap);
                        File picture = CommonUtils.saveBitmap2file(newB, "picture" + System.currentTimeMillis());
                        vector.add(picture);
                        UploadAccessBody body = new UploadAccessBody();
                        body.key = name;
                        body.contentType = finalFileType;
                        try {
                            body.contentMD5 = MD5.md5(picture);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return TribeRetrofit.getInstance().createApi(MainApis.class).
                                getUploadUrl(TribeApplication.getInstance().getUserInfo().getId(), body);
                    }
                })
                .subscribe(new Subscriber<BaseResponse<UploadResponseBody>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        vector.remove(vector.size() - 1);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.uploadFail();
                            }
                        });
                    }

                    @Override
                    public void onNext(final BaseResponse<UploadResponseBody> response) {
                        response.data.objectKey = "oss://" + response.data.objectKey;
                        putFile(response.data, vector.get(vector.size() - 1), callback);
                        vector.remove(vector.size() - 1);
                    }
                });
    }

    private void putFile(final UploadResponseBody data, File file, final UploadCallback callback) {
        try {
            URL url = new URL(data.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("PUT");  //请求方式
            conn.setRequestProperty("Content-Type", "image/jpeg");
//            conn.setRequestProperty("Content-MD5", MD5.md5(file));
            conn.connect();

            if (file != null) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.flush();
                int res = conn.getResponseCode();
                if (res == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.uploadSuccess(data);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.uploadFail();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface UploadCallback {
        void uploadSuccess(UploadResponseBody url);

        void uploadFail();
    }
}
