package moziqi.interviewdemo.bingsearh;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * 作者 : moziqi
 * 邮箱 : 709847739@qq.com
 * 时间   : 2019/6/28-9:31
 * desc   :
 * version: 1.0
 */
public class WebResourceResponseHelper {

    public static WebResourceResponse newWebResourceResponse(String url) {
        HttpURLConnection urlConnection = null;
        HttpsURLConnection conn = null;
        try {
            Log.i("mo", "newWebResourceResponse>" + url);
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            //提高效率的话，需要缓存文件流，通过LRU方式实现css,js,图片样式缓存
            //通过截取最后一个/判断文件类型，对对应缓存
            //适配下https
            URL netUrl = new URL(url);
            if ("https".equals(netUrl.getProtocol())) {
                //https://stackoverflow.com/questions/29916962/javax-net-ssl-sslhandshakeexception-javax-net-ssl-sslprotocolexception-ssl-han
//                SSLContext sslcontext = SSLContext.getInstance("TLSv1");
//                sslcontext.init(null, null, null);
//                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
                //用默認的
                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory();
                HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
                conn = (HttpsURLConnection) netUrl.openConnection();
                conn.setRequestProperty("X-Requested-With", "com.mo.aaaaa");
                //conn.connect();
                InputStream inputStream = conn.getInputStream();
                //判断css类型
                String contentType = conn.getContentType();
                Log.i("mo", "https.newWebResourceResponse.contentType>" + contentType);
                WebResourceResponse webResourceResponse = new WebResourceResponse(
                        !TextUtils.isEmpty(contentType) && contentType.startsWith("text/css") ? "text/css" : "text/html",
                        "utf-8",
                        inputStream);
                return webResourceResponse;
            } else {
                urlConnection = (HttpURLConnection) netUrl.openConnection();
                urlConnection.setRequestProperty("X-Requested-With", "com.mo.aaaaa");
                //urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                //判断css类型
                String contentType = urlConnection.getContentType();
                Log.i("mo", "http.newWebResourceResponse.contentType>" + contentType);
                WebResourceResponse webResourceResponse = new WebResourceResponse(
                        !TextUtils.isEmpty(contentType) && contentType.startsWith("text/css") ? "text/css" : "text/html",
                        "utf-8",
                        inputStream);
                return webResourceResponse;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //流不能关闭
            if (conn != null) {
                //conn.disconnect();
            }
            if (urlConnection != null) {
                //urlConnection.disconnect();
            }
        }
        return null;
    }
}
