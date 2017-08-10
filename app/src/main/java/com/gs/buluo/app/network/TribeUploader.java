package com.gs.buluo.app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

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
    private static final String TAG = "TribeUploader";
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

    public void uploadPicture(final String name, String fileType, final String file, final UploadCallback callback) {  //压缩
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
                            onFail(callback);
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
                        vector.remove(0);
                        onFail(callback);
                    }

                    @Override
                    public void onNext(final BaseResponse<UploadResponseBody> response) {
                        response.data.objectKey = "oss://" + response.data.objectKey;
                        synchronized (TribeUploader.this) {
                            putFile(response.data, vector.get(0), finalFileType, callback);
                            vector.remove(0);
                        }
                    }
                });
    }

    public void uploadFile(final String name, final String fileType, final File file, final UploadCallback callback) {  //压缩
        Observable.just(file)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<BaseResponse<UploadResponseBody>>>() {
                    @Override
                    public Observable<BaseResponse<UploadResponseBody>> call(File file) {
                        vector.add(file);
                        UploadAccessBody body = new UploadAccessBody();
                        body.key = name;
                        body.contentType = fileType;
                        try {
                            body.contentMD5 = MD5.md5(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            onFail(callback);
                        }
                        return TribeRetrofit.getInstance().createApi(MainApis.class).
                                getUploadFileUrl(TribeApplication.getInstance().getUserInfo().getId(), body);
                    }
                })
                .subscribe(new Subscriber<BaseResponse<UploadResponseBody>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        vector.remove(vector.size() - 1);
                        onFail(callback);
                    }

                    @Override
                    public void onNext(final BaseResponse<UploadResponseBody> response) {
                        response.data.objectKey = "oss://" + response.data.objectKey;
                        putFile(response.data, vector.get(vector.size() - 1), fileType, callback);
                        vector.remove(vector.size() - 1);
                    }
                });
    }

    public void onFail(final UploadCallback callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.uploadFail();
            }
        });
    }

    private void putFile(final UploadResponseBody data, File file, String fileType, final UploadCallback callback) {
        try {
            URL url = new URL(data.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", fileType);
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
                Log.e(TAG, "putFile: " + res);
                file.delete();
                if (res == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.uploadSuccess(data);
                        }
                    });
                } else {
                    onFail(callback);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            onFail(callback);
        }
    }

    public interface UploadCallback {
        void uploadSuccess(UploadResponseBody url);

        void uploadFail();
    }
}
