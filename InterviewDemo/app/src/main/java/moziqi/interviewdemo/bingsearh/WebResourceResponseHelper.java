package moziqi.interviewdemo.bingsearh;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.webkit.WebResourceResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import moziqi.interviewdemo.bingsearh.disklrucache.DiskLruCache;
import moziqi.interviewdemo.webview.UAHelper;

/**
 * 作者 : moziqi
 * 邮箱 : 709847739@qq.com
 * 时间   : 2019/6/28-9:31
 * desc   :
 * version: 1.0
 */
public class WebResourceResponseHelper {


    /**
     * 这仅仅是内存的，基本没用
     */
    //@Deprecated
    //private final static LruCache<String, WebResourceResponse> webResourceResponsesLru = new LruCache<>(128);

    //private final static LruCache<String, String> cookiesLru = new LruCache<>(128);

    /**
     * 我需要缓存到本地目录
     */
    private volatile static DiskLruCache diskLruCache = null;


    private final static CookieManager manager = new CookieManager();
    ;

    /**
     * @param context
     * @param url
     * @param refererUrl
     * @param packageName
     * @return
     */
    public static WebResourceResponse newWebResourceResponse(Context context, String url, String refererUrl, String packageName) {
        try {
            if (diskLruCache == null) {
                synchronized (WebResourceResponseHelper.class) {
                    if (diskLruCache == null) {
                        diskLruCache = DiskLruCache.open(context.getFilesDir(), 1, 1, 10 * 1024 * 1024);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        HttpsURLConnection conn = null;
        try {
            Log.i("mo", Thread.currentThread().getName() + ".newWebResourceResponse>" + url);
            if (TextUtils.isEmpty(url)) {
                return null;
            }
            if (diskLruCache != null) {
                synchronized (WebResourceResponseHelper.class) {
                    DiskLruCache.Snapshot snapshot = diskLruCache.get(md5(url));
                    if (snapshot != null) {
                        InputStream inputStream = snapshot.getInputStream(0);
                        if (inputStream != null) {
                            return new WebResourceResponse("text/html", "utf-8", inputStream);
                        }
                    }
                }
            }
            cookie();
            //提高效率的话，需要缓存文件流，通过LRU方式实现css,js,图片样式缓存
            //通过截取最后一个/判断文件类型，对对应缓存
            //适配下https
            //https://googleads.g.doubleclick.net 替换成  http://jsu.bigbuyres.com
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
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("X-Requested-With", packageName);
                conn.setRequestProperty("User-Agent", UAHelper.instance(context));
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Pragma", "no-cache");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Host", netUrl.getHost());
                conn.setRequestProperty("Accept", "*/*");
                if (!TextUtils.isEmpty(refererUrl)) {
                    int i = refererUrl.lastIndexOf("adurl=");
                    if (i > 0) {
                        String adUrl = refererUrl.substring(i + "adurl=".length());
                        String decode = URLDecoder.decode(adUrl);
                        Log.i("mo", "https.newWebResourceResponse.adUrl>" + decode);
                        conn.setRequestProperty("Referer", decode);
                    }
                }
                //conn.connect();
                InputStream inputStream = conn.getInputStream();
                //判断css类型
                String contentType = conn.getContentType();
                int code = conn.getResponseCode();
                Log.i("mo", "https.newWebResourceResponse.code>" + code);
                Log.i("mo", "https.newWebResourceResponse.contentType>" + contentType);
                if (302 == code) {
                    String redirectUrl = conn.getHeaderField("Location");
                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        return newWebResourceResponse(context, redirectUrl, url, packageName);
                    }
                }
                return getWebResourceResponse(url, inputStream, contentType);
            } else {
                urlConnection = (HttpURLConnection) netUrl.openConnection();
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setRequestProperty("X-Requested-With", packageName);
                urlConnection.setRequestProperty("User-Agent", UAHelper.instance(context));
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("Pragma", "no-cache");
                urlConnection.setRequestProperty("Cache-Control", "no-cache");
                urlConnection.setRequestProperty("Host", netUrl.getHost());
                urlConnection.setRequestProperty("Accept", "*/*");
                if (!TextUtils.isEmpty(refererUrl)) {
                    int i = refererUrl.lastIndexOf("adurl=");
                    if (i > 0) {
                        String adUrl = refererUrl.substring(i + "adurl=".length());
                        String decode = URLDecoder.decode(adUrl);
                        Log.i("mo", "http.newWebResourceResponse.adUrl>" + decode);
                        urlConnection.setRequestProperty("Referer", decode);
                    }
                }
                //urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                //判断css类型
                String contentType = urlConnection.getContentType();
                int code = urlConnection.getResponseCode();
                Log.i("mo", "http.newWebResourceResponse.code>" + code);
                Log.i("mo", "http.newWebResourceResponse.contentType>" + contentType);
                if (302 == code) {
                    String redirectUrl = urlConnection.getHeaderField("Location");
                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        return newWebResourceResponse(context, redirectUrl, url, packageName);
                    }
                }
                return getWebResourceResponse(url, inputStream, contentType);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
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


    public static void cookie() {
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(manager);
    }

    /**
     * 先用文件缓存，后面在考虑是否加上LruCache
     *
     * @param url
     * @param inputStream
     * @param contentType
     * @return
     * @throws IOException
     */
    private static WebResourceResponse getWebResourceResponse(String url, InputStream inputStream, String contentType) throws IOException {
        Log.i("mo", "getWebResourceResponse.1");
        if (TextUtils.isEmpty(contentType)) {
            return new WebResourceResponse("text/html", "utf-8", inputStream);
        }
        WebResourceResponse webResourceResponse = null;
        if (contentType.startsWith("text/css")) {
            Log.i("mo", "getWebResourceResponse.2 css");
            //css样式就不缓存了，不好判断来管理
            webResourceResponse = new WebResourceResponse("text/css", "utf-8", inputStream);
        } else if (contentType.contains("jpeg")
                || contentType.contains("png")
                || contentType.contains("application/javascript")
                || contentType.contains("text/javascript")
        ) {
            Log.i("mo", "getWebResourceResponse.3 ");
            //图片类缓存
            if (diskLruCache != null) {
                synchronized (WebResourceResponseHelper.class) {
                    OutputStream outputStream = null;
                    DiskLruCache.Editor edit = null;
                    //这里是写缓存
                    try {
                        Log.i("mo", "getWebResourceResponse.3.1");
                        edit = diskLruCache.edit(md5(url));
                        outputStream = edit.newOutputStream(0);
                        int b;
                        while ((b = inputStream.read()) != -1) {
                            outputStream.write(b);
                        }
                        edit.commit();
                        Log.i("mo", "getWebResourceResponse.3.2");
                        //这里是读取缓存
                        try {
                            Log.i("mo", "getWebResourceResponse.3.3");
                            //假如写入成功，里面在缓存取出来
                            if (diskLruCache != null) {
                                DiskLruCache.Snapshot snapshot = diskLruCache.get(md5(url));
                                InputStream cacheInputStream = snapshot.getInputStream(0);
                                if (cacheInputStream == null) {
                                    throw new RuntimeException("diskLruCache inputStream is null");
                                }
                                webResourceResponse = new WebResourceResponse("text/html", "utf-8", cacheInputStream);
                            }
                            Log.i("mo", "getWebResourceResponse.3.4");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("mo", "getWebResourceResponse.3.5");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (edit != null) {
                            edit.abort();
                        }
                        webResourceResponse = new WebResourceResponse("text/html", "utf-8", inputStream);
                        Log.i("mo", "getWebResourceResponse.3.6");
                    } finally {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                }
            } else {
                Log.i("mo", "getWebResourceResponse.4 other");
                webResourceResponse = new WebResourceResponse("text/html", "utf-8", inputStream);
            }
        } else {
            Log.i("mo", "getWebResourceResponse.5 other");
            webResourceResponse = new WebResourceResponse("text/html", "utf-8", inputStream);
        }
        return webResourceResponse;
    }

    public static String md5(String string) throws NoSuchAlgorithmException {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;

        md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(string.getBytes());
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            String temp = Integer.toHexString(b & 0xff);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result.append(temp);
        }
        return result.toString();
    }


}
