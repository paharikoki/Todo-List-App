package com.example.todo.Api;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {
    private static Retrofit retrofit;
    private static final String baseUrl="https://10.0.110.147:7284/api/";

    public static Retrofit konekRetrofit(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = getOkHttpClient().newBuilder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(loggingInterceptor);

        httpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        X509TrustManager x509TrustManager = null;
        try {
            TrustManagerFactory trustManagerFactory =TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers =trustManagerFactory.getTrustManagers();
            if (trustManagers.length!=1 || !(trustManagers[0] instanceof X509TrustManager)){
                throw new IllegalAccessException("Unexpected default trust managers:"+ Arrays.toString(trustManagers));
            }
            x509TrustManager = (X509TrustManager)trustManagers[0];
        } catch (Exception err) {
            err.printStackTrace();
        }
        try {
            if (x509TrustManager != null){
                httpClient.sslSocketFactory(getOkHttpClient().sslSocketFactory(),x509TrustManager);
            }else{
                Log.e("Error","SericeFactory # error");
            }
        }catch (Exception e){
            Log.e("Error","ServiceFactory # error "+e.getMessage());
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)

    private static SSLSocketFactory sslSocketFactory() throws  CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException{
        final TrustManager[]  trustManagers= new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        final  SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null,trustManagers,new SecureRandom());
        return sslContext.getSocketFactory();
    }


    public static OkHttpClient getOkHttpClient(){
        try {
            final TrustManager[] trustManagers = new  TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String authType)  {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String authType)  {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,trustManagers,new SecureRandom());
            final SSLSocketFactory  sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .build();
            return okHttpClient;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
