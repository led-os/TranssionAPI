package com.transsion.api.widget.http;

import android.os.Build;
import android.os.Process;

import com.transsion.api.widget.TLog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Http下载相关
 *
 * @author 孙志刚.
 * @date 04/09/17.
 * ==============================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpClient {
    private static HttpClient sClientInstance = null;
    ExecutorService executorService = Executors.newCachedThreadPool();
    private List<HttpRequest> mRunningTasks = new ArrayList<>();

    public static HttpClient getInstance() {
        if (sClientInstance == null) {
            synchronized (HttpClient.class) {
                if (sClientInstance == null) {
                    sClientInstance = new HttpClient();
                }
            }
        }

        return sClientInstance;
    }

    public void request(final HttpRequest request) {
        if (request == null || mRunningTasks.contains(request)) {
            return;
        }

        TLog.d("http url=" + request.getUrl());
        mRunningTasks.add(request);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                try {
                    if (request.isCanceled()) {
                        return;
                    }

                    final HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();

                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(15000);
                    connection.setDoInput(true);
                    connection.setRequestProperty("User-Agent", getDefaultUserAgent());

                    if (request.getUrl().startsWith("https")) {
                        trustAllHosts((HttpsURLConnection) connection);
                    }

                    if (request.getHeaders() != null) {
                        for (String key : request.getHeaders().keySet()) {
                            connection.setRequestProperty(key, request.getHeaders().get(key));
                        }
                    }

                    if (request.getPostParams() != null || request.getPostString() != null) {
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        TLog.i("set method post");
                        if (request.getPostString() != null) {
                            TLog.d("body=" + request.getPostString());
                            OutputStream outputStream = connection.getOutputStream();
                            outputStream.write(request.getPostString().getBytes());
                        } else {
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                            StringBuilder builder = new StringBuilder();
                            for (String key : request.getPostParams().keySet()) {
                                builder.append(key);
                                builder.append("=");
                                builder.append(URLEncoder.encode(request.getPostParams().get(key), "UTF-8"));
                                builder.append("&");
                            }

                            if (builder.length() > 0) {
                                builder.deleteCharAt(builder.length() - 1);
                            }

                            TLog.d("encoded body=" + builder.toString());
                            OutputStream outputStream = connection.getOutputStream();
                            outputStream.write(builder.toString().getBytes());
                        }
                    } else {
                        connection.setRequestMethod("GET");
                        TLog.i("set method get");
                    }

                    if (connection.getResponseCode() == 302 ||
                            connection.getResponseCode() == 301) {
                        String location = connection.getHeaderField("Location");
                        request.url = location;
                        request(request);
                        return;
                    } else if (connection.getResponseCode() > 400) {
                        throw new IllegalArgumentException("error http code = " + connection.getResponseCode());
                    }

                    int read;
                    byte[] streams = new byte[4096];
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    do {
                        read = is.read(streams);
                        if (read > 0) {
                            bos.write(streams, 0, read);
                        }
                    } while (read >= 0 && !request.isCanceled());

                    if (request.isCanceled()) {
                        return;
                    }

                    if (request.getListener() != null) {
                        request.getListener().onLoadFinish(true, new String(bos.toByteArray()));
                    }
                } catch (Exception e) {
                    TLog.e(e);

                    if (request.getListener() != null) {
                        request.getListener().onLoadFinish(false, null);
                    }
                } finally {
                    mRunningTasks.remove(request);
                }
            }
        });
    }

    /**
     * @param tag null: 删除所有请求; not null: 删除所有带指定tag标签的请求
     */
    public void cancelAll(Object tag) {
        for (HttpRequest request : mRunningTasks) {
            if (request != null && (tag == null || tag.equals(request.getTag()))) {
                request.cancel();
            }
        }
    }

    private String getDefaultUserAgent() {
        final String UA = "Mozilla/5.0 (Linux; Android %s;) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36";
        Locale locale = Locale.getDefault();
        StringBuffer buffer = new StringBuffer();
        // Add version
        final String version = Build.VERSION.RELEASE;
        if (version.length() > 0) {
            buffer.append(version);
        } else {
            // default to "1.0"
            buffer.append("1.0");
        }
        buffer.append("; ");

        final String language = locale.getLanguage();
        if (language != null) {
            buffer.append(language.toLowerCase());
            final String country = locale.getCountry();
            if (country != null) {
                buffer.append("-");
                buffer.append(country.toLowerCase());
            }
        } else {
            // default to "en"
            buffer.append("en");
        }

        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            final String model = Build.MODEL;
            if (model.length() > 0) {
                buffer.append("; ");
                buffer.append(model);
            }
        }

        final String id = Build.ID;
        if (id.length() > 0) {
            buffer.append(" Build/");
            buffer.append(id);
        }

        return String.format(UA, buffer.toString());
    }

    private void trustAllHosts(HttpsURLConnection connection) {
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
