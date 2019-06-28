package moziqi.interviewdemo.bingsearh;

import android.util.Log;
import android.webkit.WebView;

import org.wall.mo.utils.relect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 作者 : moziqi
 * 邮箱 : 709847739@qq.com
 * 时间   : 2019/6/28-11:02
 * desc   :
 * version: 1.0
 */
public class WebViewCompat {

    /**
     * @param webView
     * @param packageName
     */
    public static void handleLoadUrlPackageName(WebView webView, String packageName) {
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
            Object mObj = Proxy.newProxyInstance(mNavigationControllerObject.getClass().getClassLoader(), inter, mHandler);
            FieldUtils.writeField(mNavigationControllerField, mAwContentsObject, mObj);
        } catch (Exception e) {
            Log.wtf("mo", e);
        }
    }
}
