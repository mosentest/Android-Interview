package moziqi.interviewdemo.bingsearh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.Constants;
import moziqi.interviewdemo.webview.SimulationListener;
import moziqi.interviewdemo.webview.TouchWebView;

public class BingSearchActivity extends AppCompatActivity {

    private TouchWebView touchWebView;

    private EditText etKey;

    private Button btnSearch;


    private final static String BING = "https://cn.bing.com/search?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_search);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        touchWebView = findViewById(R.id.touchWebView);
        touchWebView.loadURL(BING);
        touchWebView.setSimulationListener(new SimulationListener() {
            @Override
            public void doSimulation() {
                touchWebView.loadUrl("javascript:"
                        + "document.getElementsByClassName('b_algoheader')[0].getElementsByTagName('a')[0].click()");
            }
        });
        etKey = findViewById(R.id.etKey);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = etKey.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    touchWebView.loadURL(BING + trim);
                }
            }
        });
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