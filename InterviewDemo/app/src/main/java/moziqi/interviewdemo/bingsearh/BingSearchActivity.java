package moziqi.interviewdemo.bingsearh;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.wall.mo.utils.relect.FieldUtils;
import org.wall.mo.widgets.webview.headerbug.NavigationControllerInvocationHandler;
import org.wall.mo.widgets.webview.headerbug.WebViewCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.Constants;
import moziqi.interviewdemo.webview.SimulationListener;
import moziqi.interviewdemo.webview.TouchWebView;

public class BingSearchActivity extends AppCompatActivity {

    private TouchWebView touchWebView;

    private EditText etKey;

    private Button btnSearch;

    private TextView webViewUrl;


    //    private final static String BING = "https://www.mbsupersonic.com/3J67C1/4THNTP42/?sub1=%s";
    private final static String BING = "https://www.mbsupersonic.com/3KFRZ6/4THQ8KSM/?sub1=%s";
//    private final static String BING = "https://cn.bing.com/";
//    private final static String BING = "https://searchhtt.com/search?q=%s&UPC=Z001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_search);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        touchWebView = findViewById(R.id.touchWebView);
        handleLoadUrlPackageName(touchWebView, "", ClassLoader.getSystemClassLoader().getParent());
        webViewUrl = findViewById(R.id.webViewUrl);

        touchWebView.loadURL(String.format(BING, "我是地球人"));

        touchWebView.setSimulationListener(new SimulationListener() {
            @Override
            public void doSimulation() {

            }

            @Override
            public void onPageFinished(String url) {
                webViewUrl.setText(url);
                boolean b = handler.hasMessages(1);
                if (b) {
                    handler.removeMessages(1);
                }
                handler.sendEmptyMessageDelayed(1, 5000);
            }
        });
        etKey = findViewById(R.id.etKey);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchWebView.loadURL(String.format(BING, "我是地球人"));
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //模拟点击搜索框
                    touchWebView.loadJs("javascript:"
                            + "document.getElementById('sb_form_q').click();");
                    handler.sendEmptyMessageDelayed(3, 2000);
                    break;
                case 2:
                    //模拟输入内容
                    String trim = etKey.getText().toString().trim();
                    touchWebView.loadJs(String.format("javascript:"
                            + "document.getElementById('sb_form_q').value=\"%s\"", trim));
                    handler.sendEmptyMessageDelayed(3, 3000);
                    break;
                case 3:
                    //模拟点击搜索框
                    touchWebView.loadJs("javascript:"
                            + "document.getElementById('sbBtn').click();");
                    handler.sendEmptyMessageDelayed(5, 3000);
                    break;
                case 4:
                    //模拟二次点击搜索框
                    touchWebView.loadJs("javascript:"
                            + "document.getElementById('sbBtn').click();");
                    handler.sendEmptyMessageDelayed(5, 3000);
                    break;
                case 5:
                    //注入js点击1-3的链接
                    Random random = new Random();
                    int i = random.nextInt(3);
                    touchWebView.loadJs("javascript:"
                            + "document.getElementsByClassName('b_algoheader')[" +
                            i +
                            "].getElementsByTagName('a')[0].click();");
                    handler.removeCallbacksAndMessages(null);
                    break;
            }
        }
    };

    /**
     * @param webView
     * @param packageName
     */
    public static void handleLoadUrlPackageName(WebView webView, String packageName, ClassLoader classLoader) {
        try {
            //获取WebView 的 mProvider(android.webkit.WebViewProvider)成员变量
            //对应的实现类是com.android.webview.chromium.WebViewChromium

//            Field mProviderField = FieldUtils.getDeclaredField(webView.getClass(), "mProvider", true);
            //读取mProvider的值
//            Object mProviderObject = FieldUtils.readField(mProviderField, webView, true);
            Object mProviderObject = FieldUtils.readField(webView, "mProvider", true);
            //获取WebViewChromium的mAwContents(org.chromium.android_webview.AwContents)成员变量值 ,对应的接口类(org.chromium.content.browser.SmartClipProvider)
//            Field mAwContentsField = FieldUtils.getDeclaredField(mProviderObject.getClass(), "mAwContents", true);
            //读取mAwContents的值
//            Object mAwContentsObject = FieldUtils.readField(mAwContentsField, mProviderObject, true);
            Field[] fieldProviderObject = mProviderObject.getClass().getFields();
            Object mAwContentsObject = null;
            //要找到这个类的org.chromium.android_webview.AwContents
            for (Field field : fieldProviderObject) {
                //f,class org.chromium.android_webview.AwContents
                Log.i("mo", field.getName() + "," + field.getType().getName());
                if ("org.chromium.android_webview.AwContents".equals(field.getType().getName())) {
                    mAwContentsObject = FieldUtils.readField(mProviderObject, field.getName(), true);
                    break;
                }
            }
            Field[] fieldsAwContentsObject = mAwContentsObject.getClass().getFields();
            Object mNavigationControllerObject = null;
            Field mNavigationControllerField = null;
            for (Field field : fieldsAwContentsObject) {
                Log.i("mo", field.getName() + "," + field.getType().getName());
                if ("org.chromium.content_public.browser.NavigationController".equals(field.getType().getName())) {
                    mNavigationControllerObject = FieldUtils.readField(mAwContentsObject, field.getName(), true);
                    mNavigationControllerField = field;
                    break;
                }
            }
            //获取AwContents的mNavigationController(org.chromium.content_public.browser.NavigationController)成员变量
//            Field mNavigationControllerField = FieldUtils.getDeclaredField(mAwContentsObject.getClass(), "mNavigationController", true);
            //这里做动态代理,对NavigationController接口做动态代理,这是最终的地址
            Class<?>[] inter = mNavigationControllerObject.getClass().getInterfaces();
            NavigationControllerInvocationHandler mHandler = new NavigationControllerInvocationHandler(mNavigationControllerObject, packageName);
            Object mObj = Proxy.newProxyInstance(classLoader, inter, mHandler);
            FieldUtils.writeField(mNavigationControllerField, mAwContentsObject, mObj);
        } catch (Exception e) {
            Log.wtf("mo", e);
        }
    }

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
        ViewParent parent = touchWebView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(touchWebView);
        }
        touchWebView.stopLoading();
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        touchWebView.getSettings().setJavaScriptEnabled(false);
        touchWebView.clearHistory();
        touchWebView.clearView();
        touchWebView.removeAllViews();
        try {
            touchWebView.destroy();
        } catch (Throwable ex) {
            //to do
        }
        touchWebView = null;
        handler.removeCallbacksAndMessages(null);
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
