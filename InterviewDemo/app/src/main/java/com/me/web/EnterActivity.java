package com.me.web;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import moziqi.interviewdemo.R;

/**
 * http://www.haoduobq.com/
 * <p>
 * 一键随机切换ip，ua ，模拟器参数值 ，清理缓存，后打开网站   并在模拟器，和手机上可以运行
 */
public class EnterActivity extends AppCompatActivity {

    private ProgressBar mProgress;
    private TouchWebView mTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        initView();
        mTouchView.setSimulationListener(new SimulationListener() {
            @Override
            public void doSimulation() {

            }

            @Override
            public void onPageFinished(String url) {
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onProgressChanged(int newProgress) {
                if (newProgress == 100) {
                    mProgress.setVisibility(View.GONE);
                } else {
                    if (mProgress.getVisibility() == View.GONE)
                        mProgress.setVisibility(View.VISIBLE);
                    mProgress.setProgress(newProgress);
                }
            }

            @Override
            public void onError(String url) {

            }
        });
        mTouchView.loadURL("https://www.baidu.com");
    }

    private void initView() {
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mTouchView = (TouchWebView) findViewById(R.id.touchView);
    }
}
