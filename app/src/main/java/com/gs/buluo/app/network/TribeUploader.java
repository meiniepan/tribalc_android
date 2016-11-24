package com.gs.buluo.app.network;

import com.gs.buluo.app.bean.ResponseBody.UploadAccessResponse;
import com.gs.buluo.app.model.MainModel;


import org.xutils.common.util.MD5;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/24.
 */
public class TribeUploader {
    private static TribeUploader uploader;
    private UploadCallback callback;

    private TribeUploader(){
    }

    public static TribeUploader getInstance(){
        if (uploader==null){
            uploader=new TribeUploader();
        }
        return uploader;
    }

    public void uploadFile(String name, String fileType, final File file, final UploadCallback callback) {
        fileType = "image/jpeg";
        new MainModel().uploadFile(file, name, fileType, new Callback<UploadAccessResponse>() {
            @Override
            public void onResponse(Call<UploadAccessResponse> call, final Response<UploadAccessResponse> response) {
                if (response.body()!=null&&response.body().code==201){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            putFile(response.body().data,file);
                        }
                    }).start();
                }
            }
            @Override
            public void onFailure(Call<UploadAccessResponse> call, Throwable t) {
                callback.uploadFail();
            }
        });

    }

    private void putFile(String data,File file) {
        try {
            URL url = new URL(data);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("PUT");  //请求方式
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestProperty("Content-MD5", MD5.md5(file));
            conn.connect();

            if(file!=null){
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1){
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.flush();
                int res = conn.getResponseCode();
                if(res==200){
                   callback.uploadSuccess();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
}

    public interface UploadCallback{
        void uploadSuccess();
        void uploadFail();
    }
}
