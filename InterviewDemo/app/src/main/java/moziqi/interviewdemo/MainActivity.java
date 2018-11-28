package moziqi.interviewdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import moziqi.interviewdemo.touchevent.TouchEventActivity;
import moziqi.interviewdemo.webview.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //启动学习事件传递机制
        findViewById(R.id.btn_touchevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TouchEventActivity.class));
            }
        });

        findViewById(R.id.btn_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebViewActivity.class));
            }
        });

    }


    static String str_getAssets = "g2e2t2As2s2e2t2s2";//getAssets

    static String str_open = "2o2p2e2n";//open

    static {
        str_getAssets = str_getAssets.replace("2", "");
        str_open = str_open.replace("2", "");
    }

    private static void doa(Context context) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        AssetManager assets = context.getAssets();
//        assets.open("fileName")
        Method getAssets = context.getClass().getMethod(str_getAssets);
        Object invokeAssetManager = getAssets.invoke(context);
        Method open = invokeAssetManager.getClass().getMethod(str_open, String.class);
        InputStream stream = (InputStream) open.invoke(invokeAssetManager, "fileName");
    }
}
