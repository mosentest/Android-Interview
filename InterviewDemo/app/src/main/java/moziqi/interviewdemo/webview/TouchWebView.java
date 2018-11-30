package moziqi.interviewdemo.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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


    public void setSimulationListener(SimulationListener simulationListener) {
        this.simulationListener = simulationListener;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
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
        //声明WebSettings子类
        WebSettings webSettings = this.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
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
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        //不使用缓存:
        //webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

        String cacheDirPath = getContext().getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录


        //
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.i(getTAG(), "onPageFinished>>>" + url);
                inFinish();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.i(getTAG(), "onPageStarted>>>" + url);
            }
        });


        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) {
                    LogUtils.i(getTAG(), "onProgressChanged>>>" + newProgress);
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


}
