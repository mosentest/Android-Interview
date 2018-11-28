package moziqi.interviewdemo.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.ILog;

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


    private TouchWebView touchWebView;

    @Override
    public String getTAG() {
        return "WebViewActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        touchWebView = findViewById(R.id.webview);
        touchWebView.loadUrl("https://www.cnblogs.com/doit8791/p/7776501.html");
    }
}
