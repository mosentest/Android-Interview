package com.me.web;

import android.webkit.JavascriptInterface;

import moziqi.interviewdemo.util.LogUtils;

/**
 * 作者 : moziqi
 * 邮箱 : 709847739@qq.com
 * 时间   : 2019/6/5-12:39
 * desc   :
 * version: 1.0
 */

class InJavaScriptLocalObj {

    private TouchWebView touchWebView;

    public InJavaScriptLocalObj(TouchWebView touchWebView) {
        this.touchWebView = touchWebView;
    }

    @JavascriptInterface
    public void showSource(String html) {
        LogUtils.i("====>showSource=" + html);
        touchWebView.getSource(html);
    }

    @JavascriptInterface
    public void readyState(String readyState) {
        LogUtils.i("====>readyState=" + readyState);
        touchWebView.readyState(readyState);
    }

    @JavascriptInterface
    public void showDescription(String str) {
        LogUtils.i("====>showDescription=" + str);
    }

    @JavascriptInterface
    public void showReferrer(String str) {
        LogUtils.i("====>showReferrer=" + str);
    }
}
