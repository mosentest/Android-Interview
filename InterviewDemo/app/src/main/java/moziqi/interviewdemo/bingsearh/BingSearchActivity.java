package moziqi.interviewdemo.bingsearh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.TouchUtils;
import moziqi.interviewdemo.webview.TouchWebView;
import moziqi.interviewdemo.webview.WebViewHelper;

public class BingSearchActivity extends AppCompatActivity {

    private TouchWebView touchWebView;

    private EditText etKey;

    private Button btnSearch;

    private TextView webViewUrl;


    //    private final static String BING = "https://www.mbsupersonic.com/3J67C1/4THNTP42/?sub1=%s";
//    private final static String BING = "https://www.mbsupersonic.com/3KFRZ6/4THQ8KSM/?sub1=%s";
//    private final static String BING = "https://cn.bing.com/";
    private final static String BING = "https://searchhtt.com/search?q=%s&UPC=Z001";


    private TouchUtils touchUtils = new TouchUtils();

    private WebViewHelper webViewHelper = null;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_search);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        touchWebView = findViewById(R.id.touchWebView);

        touchWebView.setPackageName("com.game.fire");
        WebViewCompat.handleLoadUrlPackageName(touchWebView, "com.game.fire");

        webViewUrl = findViewById(R.id.webViewUrl);

//        touchWebView.loadURL(String.format(BING, "我是地球人"));
//        touchWebView.loadURL("http://snail.gameasy.top");

        webViewHelper = new WebViewHelper(touchWebView);

        BlockingQueue<WebViewHelper.WebData> webData = new ArrayBlockingQueue<>(3);
        //String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))
        webData.add(webViewHelper.createWebData(0, String.format(BING, "我是地球人"), Arrays.asList(
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5)))
        )));
        webData.add(webViewHelper.createWebData(1, String.format(BING, "我是中国人"), Arrays.asList(
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5)))
        )));
        webData.add(webViewHelper.createWebData(0, String.format(BING, "我是外国人"), Arrays.asList(
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5)))
        )));
        webViewHelper.setData(webData).start();

        etKey = findViewById(R.id.etKey);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockingQueue<WebViewHelper.WebData> webData = new ArrayBlockingQueue<>(3);
                webData.add(webViewHelper.createWebData(0, String.format(BING, "我是红色人"), Arrays.asList(
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5)))
                )));
                webData.add(webViewHelper.createWebData(1, String.format(BING, "我是蓝色人"), Arrays.asList(
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5)))
                )));
                webData.add(webViewHelper.createWebData(0, String.format(BING, "我是绿色人"), Arrays.asList(
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5))),
                        webViewHelper.createJsObj(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", webViewHelper.getRandom(5)))
                )));
                webViewHelper.setData(webData).start();
            }
        });

        findViewById(R.id.btnShouldInterceptRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchWebView.setShouldInterceptRequest(!touchWebView.isShouldInterceptRequest());
            }
        });
    }


//    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            LogUtils.i("mo", "msg.what>>" + msg.what);
//            switch (msg.what) {
//                case 1:
//                    //模拟点击搜索框
//                    touchWebView.loadJs("javascript:"
//                            + "document.getElementById('sb_form_q').click();");
//                    handler.sendEmptyMessageDelayed(3, 2000);
//                    break;
//                case 2:
//                    //模拟输入内容
//                    String trim = etKey.getText().toString().trim();
//                    touchWebView.loadJs(String.format("javascript:"
//                            + "document.getElementById('sb_form_q').value=\"%s\"", trim));
//                    handler.sendEmptyMessageDelayed(3, 3000);
//                    break;
//                case 3:
//                    //模拟点击搜索框
//                    touchWebView.loadJs("javascript:"
//                            + "document.getElementById('sbBtn').click();");
//                    handler.sendEmptyMessageDelayed(5, 3000);
//                    break;
//                case 4:
//                    //模拟二次点击搜索框
//                    touchWebView.loadJs("javascript:"
//                            + "document.getElementById('sbBtn').click();");
//                    handler.sendEmptyMessageDelayed(5, 3000);
//                    break;
//                case 5:
//                    //注入js点击1-3的链接
//                    Random random = new Random();
//                    int nextInt = random.nextInt(5);
//                    for (int n = nextInt; n >= 0; n--) {
//                        touchWebView.loadJs(String.format("javascript:document.getElementsByClassName('b_algoheader')[%d].getElementsByTagName('a')[0].click();", n));
//                    }
//                    boolean b6 = handler.hasMessages(6);
//                    if (!b6) {
//                        handler.sendEmptyMessageDelayed(6, 10 * 1000);
//                    }
//                    break;
//                case 6:
//                    if (touchWebView.canGoBack()) {
//                        while (touchWebView.canGoBack()) {
//                            touchWebView.goBack();
//                        }
//                    } else {
//                        boolean b5 = handler.hasMessages(5);
//                        if (!b5) {
//                            handler.sendEmptyMessageDelayed(5, 10 * 1000);
//                        }
//                    }
//                    break;
//                case 7:
//                    touchWebView.reload();
//                    break;
//            }
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
        touchWebView.onResume();
        touchWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        touchWebView.pauseTimers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        touchWebView.stopLoading();
    }

    @Override
    protected void onDestroy() {
        webViewHelper.onDestroy();
        touchWebView = null;
        super.onDestroy();
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
