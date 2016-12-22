package com.gs.buluo.app.network;

import android.content.Context;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2016/11/1.
 */
public class TribeRetrofit {

    private static TribeRetrofit instance;
    private Map<Class, Object> apis = new HashMap<>();
    private final Retrofit retrofit;

    private TribeRetrofit(){
        OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        builder.interceptors().add(new HttpInterceptor());
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);

//        onHttpCertficates(builder);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.Base.BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public  synchronized  static TribeRetrofit getInstance(){
        if (null==instance){
            instance=new TribeRetrofit();
        }
        return instance;
    }

    public <T>T createApi(Class<T> service) {
        if (!apis.containsKey(service)) {
            T instance = retrofit.create(service);
            apis.put(service, instance);
        }

        return (T)apis.get(service);
    }

    protected static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        //CertificateFactory用来证书生成
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(is));

                if (is != null) {
                    is.close();
                }
            }
            //Create a TrustManager that trusts the CAs in our keyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            return null;
        }

    }
    private void onHttpCertficates(OkHttpClient.Builder builder) {
        int[] certficates = new int[]{R.raw.beep};
        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(TribeApplication.getInstance().getApplicationContext(), certficates);
        if (sslSocketFactory !=null)
            builder.socketFactory(sslSocketFactory);
    }

}
