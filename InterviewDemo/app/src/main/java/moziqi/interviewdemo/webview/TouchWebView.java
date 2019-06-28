package moziqi.interviewdemo.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import moziqi.interviewdemo.bingsearh.WebResourceResponseHelper;
import moziqi.interviewdemo.util.ILog;
import moziqi.interviewdemo.util.LogUtils;

/**
 * Copyright (C), 2018-2018
 * FileName: TouchWebView
 * Author: ziqimo
 * Date: 2018/11/28 下午9:27
 * Description: ${DESCRIPTION}
 * History:https://www.jianshu.com/p/3c94ae673e2a/
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TouchWebView extends WebView implements ILog {

    private static final String APP_CACAHE_DIRNAME = "webview_cache";

    private volatile boolean isFinish = false;

    private SimulationListener simulationListener;

    private String packageName;

    public void setSimulationListener(SimulationListener simulationListener) {
        this.simulationListener = simulationListener;
    }

    public TouchWebView(Context context) {
        super(context);
        init();
    }

    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {

        // 在Android 3.0以下 去除远程代码执行漏洞
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");

        //声明WebSettings子类
        WebSettings webSettings = this.getSettings();


        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        } else {

        }

        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //支持插件
        //webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        // 加快HTML网页加载完成的速度，等页面finish再加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0 以后的WebView加载的链接为Https开头，但是链接里面的内容，
            // 比如图片为Http链接，这时候，图片就会加载不出来
            // 下面两者都可以
            // Android 5.0上Webview默认不允许加载Http与Https混合内容
            // ws.setMixedContentMode(ws.getMixedContentMode())
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 4.1以后默认禁止
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(false);
            webSettings.setAllowUniversalAccessFromFileURLs(false);
        }
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        // 定位相关
//        webSettings.setGeolocationEnabled(false);

        //优先使用缓存:
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        //不使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
//
        String cacheDirPath = getContext().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

        // 不保存密码，已经废弃了该方法，以后的版本都不会保存密码
        webSettings.setSavePassword(false);

        webSettings.setUserAgentString(UAHelper.instance(getContext()));


        //
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                isFinish = false;
                loadURL(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.i(getTAG(), "onPageFinished>>>" + url);
                if (simulationListener != null) {
                    simulationListener.onPageFinished(url);
                }
                inFinish();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.i(getTAG(), "onPageStarted>>>" + url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (TextUtils.isEmpty(url)) {
                    return super.shouldInterceptRequest(view, url);
                }
                WebResourceResponse webResourceResponse = WebResourceResponseHelper.newWebResourceResponse(getContext(), url, packageName);
                return webResourceResponse;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (TextUtils.isEmpty(url)) {
                    return super.shouldInterceptRequest(view, request);
                }
                WebResourceResponse webResourceResponse = WebResourceResponseHelper.newWebResourceResponse(getContext(), request.getUrl().toString(), packageName);
                return webResourceResponse;
            }
        });


        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 80) {
                    //LogUtils.i(getTAG(), "onProgressChanged>>>" + newProgress);
                    inFinish();
                }
            }

        });

    }

    private void inFinish() {
        LogUtils.i(getTAG(), "inFinish.thread_name:" + Thread.currentThread().getName());
        synchronized (TouchWebView.class) {
            if (!isFinish) {
                isFinish = true;
                if (simulationListener != null) {
                    simulationListener.doSimulation();
                }
            }
        }
    }

    @Override
    public String getTAG() {
        return "TouchWebView";
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 加载url地址
     *
     * @param url
     */
    public void loadURL(String url) {
        isFinish = false;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Requested-With", "com.mo.aaaaa");
        headerMap.put("x-requested-with", "com.mo.aaaaa");
        loadUrl(url, headerMap);
    }

    public void loadJs(String js) {
        LogUtils.i(getTAG(), "loadJs.js:" + js);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    LogUtils.i(getTAG(), "loadJs.value:" + value);
                }
            });
        } else {
            loadURL(js);
        }
    }

    /**
     * 获取页面元素
     */
    public void getHtml() {
        // 获取页面内容
        loadJs("javascript:window.java_obj.showSource("
                + "document.getElementsByTagName('html')[0].innerHTML);");
    }
}
