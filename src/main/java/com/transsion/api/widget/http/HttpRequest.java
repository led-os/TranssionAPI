package com.transsion.api.widget.http;

import com.transsion.api.widget.TLog;
import com.transsion.api.widget.Tson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * http请求器
 *
 * @author 孙志刚.
 * @date 04/09/17.
 * ==============================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class HttpRequest {
    protected String url = null;
    private Map<String, String> headers = null;
    private Map<String, String> params = null;
    private String postString = null;
    private HttpListener listener = null;
    private Object tag;
    private boolean canceled = false;

    protected HttpRequest() {
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getPostParams() {
        return params;
    }

    public String getPostString() {
        return postString;
    }

    public HttpListener getListener() {
        return listener;
    }

    public Object getTag() {
        return tag;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void cancel() {
        canceled = true;
        listener = null;
    }

    public static class Builder {
        private String url = null;
        private Map<String, String> headers = null;
        private Map<String, String> params = null;
        private String postString = null;
        private HttpListener listener = null;
        private Object tag;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setHeader(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setPostParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder setPostString(String body) {
            this.postString = body;
            return this;
        }

        public Builder setListener(HttpListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        public HttpRequest build() {
            HttpRequest request = new HttpRequest();
            request.url = url;
            request.headers = headers;
            request.params = params;
            request.postString = postString;
            request.listener = listener;
            request.tag = tag;
            return request;
        }
    }

    public abstract static class HttpListener<T> {
        private Class<T> t;

        public HttpListener() {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            t = (Class) params[0];
        }

        public void onLoadFinish(boolean result, String data) {
            try {
                onResult(result, t.equals(String.class) ? (T) data : Tson.fromJsonString(data, t));
            } catch (Exception e) {
                TLog.e(e);
            }
        }

        public abstract void onResult(boolean result, T data);
    }
}
