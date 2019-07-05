package moziqi.interviewdemo.webview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.Constants;
import moziqi.interviewdemo.util.ILog;
import moziqi.interviewdemo.util.LogUtils;
import moziqi.interviewdemo.util.TouchUtils;

/**
 * Copyright (C), 2018-2018
 * FileName: WebViewActivity
 * Author: ziqimo
 * Date: 2018/11/28 下午9:25
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WebViewActivity extends AppCompatActivity implements ILog {


    public TouchWebView touchWebView;

    public static int currentPixel = 200;

    private final static long delay_time = 2000;

    private int webViewHeight;

    private int webViewWidth;

    private TouchUtils touchUtils = new TouchUtils();


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (touchWebView != null) {
                webViewWidth = touchWebView.getWidth();
                webViewHeight = touchWebView.getHeight();
                LogUtils.i(getTAG(), "currentPixel>>" + currentPixel);
                switch (msg.what) {
                    case 1:
                        touchUtils.simulationTouch(touchWebView, 0, webViewHeight / 2f, currentPixel);
                        LogUtils.i(getTAG(), "webViewHeight + touchWebView.getScrollY()>>>" + (webViewHeight + touchWebView.getScrollY()));
                        if (touchWebView.getContentHeight() * touchWebView.getScale() - (touchWebView.getHeight() + touchWebView.getScrollY()) == 0) {
                            //到底了
                            currentPixel = 200;
                            //sendEmptyMessageDelayed(2, 800);
                            //改为关闭当前页面
                            sendEmptyMessageDelayed(3, 800);
                        } else {
                            currentPixel += 200;
                            sendEmptyMessageDelayed(1, 800);
                        }
                        break;
                    case 2:
                        touchUtils.simulationTouch(touchWebView, 0, webViewHeight / 2f, currentPixel);
                        LogUtils.i(getTAG(), "touchWebView.getScrollY()>>>" + touchWebView.getScrollY());
                        if (touchWebView.getScrollY() == 0) {
                            //到顶了
                            currentPixel = 0;
                            sendEmptyMessageDelayed(1, 800);
                        } else {
                            currentPixel -= 200;
                            sendEmptyMessageDelayed(2, 800);
                        }
                        break;
                    case 3:
                        //结束activity
                        setResult(Activity.RESULT_OK);
                        finish();
                        break;
                }
            }
        }
    };

    @Override
    public String getTAG() {
        return "WebViewActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        touchWebView = findViewById(R.id.webview);
        String url = getIntent().getStringExtra(Constants.IntentCode.rv_url);
        //touchWebView.loadUrl("https://www.cnblogs.com/doit8791/p/7776501.html");
        touchWebView.loadURL(url);
        touchWebView.setSimulationListener(new SimulationListener() {
            @Override
            public void doSimulation() {
                handler.sendEmptyMessageDelayed(1, delay_time);
            }

            @Override
            public void onPageFinished(String url) {

            }

            @Override
            public void onError(String url) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        touchWebView.destroy();
        touchWebView = null;
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        currentPixel = 0;
    }

    @Override
    public void onBackPressed() {
        if (touchWebView != null) {
            //如果h5页面可能返回，跳转到上个页面
            if (touchWebView.canGoBack()) {
                touchWebView.goBack();
            } else {
                //不能返回上个页面，直接finish当前Activity
                finish();
            }
        } else {
            finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
