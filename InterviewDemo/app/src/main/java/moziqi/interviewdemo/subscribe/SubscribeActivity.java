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
import moziqi.interviewdemo.webview.SimulationListener;
import moziqi.interviewdemo.webview.TouchWebView;

public class SubscribeActivity extends AppCompatActivity {

    private Button mGetPhone;

    private Button mGetMsg;

    private TouchWebView mTouchWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mGetPhone = findViewById(R.id.getPhone);
        mGetMsg = findViewById(R.id.getMsg);
        mTouchWebView = findViewById(R.id.touchWebView);

        mTouchWebView.loadURL("https://ssl.zc.qq.com/v3/index-chs.html");
        mTouchWebView.setSimulationListener(new SimulationListener() {
            @Override
            public void doSimulation() {
            }

            @Override
            public void onPageFinished(String url) {
                //在这个方法处理
                handler.sendEmptyMessageDelayed(1, 1000);
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

                    mTouchWebView.loadJs("javascript:document.getElementById('nickname').value='测试人员'");

                    mTouchWebView.loadJs("javascript:document.getElementById('password').value='tmd123456'");

                    String phoneNum = PhoneUtils.getPhoneNum(getApplicationContext());
                    mTouchWebView.loadJs(
                            String.format("javascript:document.getElementsByClassName('phone')[0].value='%s'",
                                    TextUtils.isEmpty(phoneNum) ? "1380000000" : phoneNum)
                    );
                    handler.sendEmptyMessageDelayed(2, 1000);
                    break;
                case 2:
                    mTouchWebView.loadJs("javascript:document.getElementById('send-sms').click()");
                    handler.sendEmptyMessageDelayed(3, 3000);
                    break;
                case 3:
                    mTouchWebView.loadJs("javascript:document.getElementById('code').value='112312'");
                    handler.sendEmptyMessageDelayed(4, 3000);
                    break;
                case 4:
                    mTouchWebView.loadJs("javascript:document.getElementsByClassName('submit')[0].click()");
                    Toast.makeText(getApplicationContext(), "点击了提交", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    break;
                case 6:
                    while (mTouchWebView.canGoBack()) {
                        mTouchWebView.goBack();
                    }
                    break;
                case 7:
                    mTouchWebView.reload();
                    break;
            }
        }
    };
}
