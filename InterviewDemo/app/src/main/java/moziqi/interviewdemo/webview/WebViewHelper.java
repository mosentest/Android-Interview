package moziqi.interviewdemo.webview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 作者 : moziqi
 * 邮箱 : 709847739@qq.com
 * 时间   : 2019/7/26-11:26
 * desc   : 用于刷订阅之类的框架
 * version: 1.0
 */
public class WebViewHelper {

    private final TouchWebView localWebView;

    private final InnerHandler handler;

    private BlockingQueue<WebData> mWebDatas;

    private int reloadCount = 0;

    private final static int RELOAD_COUNT = 3;

    /**
     * 针对当前一个webData对象，放进去执行的js
     */
    private List<JsObj> runningJs = new ArrayList<>();

    /**
     * 传递webView进来，用于控制
     *
     * @param webView
     */
    public WebViewHelper(TouchWebView webView) {
        this.localWebView = webView;
        this.handler = new InnerHandler(localWebView.getContext());
    }


    /**
     * 设置数据
     *
     * @param webDatas
     */
    public WebViewHelper setData(BlockingQueue<WebData> webDatas) {
        mWebDatas = webDatas;
        handler.removeCallbacksAndMessages(null);
        return this;
    }

    /*
     *启动工作
     */
    public void start() {
        if (mWebDatas == null || mWebDatas.isEmpty()) {
            return;
        }
        reloadCount = 0;
        //清除这里
        runningJs.clear();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取一个值
                    final WebData webData = mWebDatas.poll();
                    if (localWebView != null && webData != null) {
                        localWebView.loadURL(webData.loadUrl);
                        localWebView.setSimulationListener(new SimulationListener() {
                            @Override
                            public void doSimulation() {
                                doJs(webData, 0);
                            }

                            @Override
                            public void onPageFinished(String url) {

                            }

                            @Override
                            public void onError(String url) {
                                if (reloadCount < RELOAD_COUNT) {
                                    //5s内再执行
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (localWebView != null) {
                                                localWebView.reload();
                                                //当没仔细过，重复执行
                                                runningJs.clear();
                                                //增加次数
                                                reloadCount++;
                                            }
                                        }
                                    }, 1000 * 5);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("mo", "start", e);
                }
            }
        });

    }

    /**
     * 执行js
     *
     * @param webData
     * @param pos
     */
    private void doJs(final WebData webData, final int pos) {
        List<JsObj> jsQueues = webData.jsQueues;
        int size = jsQueues.size();
        if (pos >= size) {
            Log.i("mo", "doJs start");
            //执行下一个
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, 5000);
            return;
        }
        final JsObj poll = jsQueues.get(pos);
        if (!runningJs.contains(poll)) {
            //判断js是否执行过了
            runningJs.add(poll);
            Log.i("mo", String.format("doJs pos is %s", pos));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (webData.postHtml == 1) {
                        localWebView.getHtml();
                    }
                    localWebView.getLoadCompete();
                    localWebView.loadJs(poll.js);
                    //继续执行
                    doJs(webData, pos + 1);
                }
            }, poll.delayTime);
        } else {
            //继续下一组js
            Log.i("mo", "doJs contains pos");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doJs(webData, pos + 1);
                }
            }, poll.delayTime);

        }
    }


    /**
     * 创建对象
     *
     * @param postHtml
     * @param loadUrl
     * @param jsQueues
     * @return
     */
    public WebData createWebData(int postHtml, String loadUrl, List<JsObj> jsQueues) {
        WebData temp = new WebData(postHtml, loadUrl, jsQueues);
        return temp;
    }

    /**
     * 创建对象
     *
     * @param js
     * @return
     */
    public JsObj createJsObj(String js) {
        JsObj temp = new JsObj(js);
        return temp;
    }

    /**
     * 获取随机数
     *
     * @param bound
     * @return
     */
    public int getRandom(int bound) {
        Random random = new Random();
        int nextInt = random.nextInt(bound);
        return nextInt;
    }

    /**
     * 停止webView
     */
    public void onDestroy() {
        if (localWebView != null) {
            ViewParent parent = localWebView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(localWebView);
            }
            localWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            localWebView.getSettings().setJavaScriptEnabled(false);
            localWebView.clearHistory();
            localWebView.clearView();
            localWebView.removeAllViews();
            try {
                localWebView.destroy();
            } catch (Throwable ex) {
                //to do
            }
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 内容
     */
    public static class WebData {
        public int postHtml; //如果是1就是要上报html
        public String loadUrl;
        public int netType;//网络状态
        public List<JsObj> jsQueues;

        public WebData(int postHtml, String loadUrl, List<JsObj> jsQueues) {
            this.postHtml = postHtml;
            this.loadUrl = loadUrl;
            this.jsQueues = jsQueues;
        }

        public WebData(int postHtml, String loadUrl, int netType, List<JsObj> jsQueues) {
            this.postHtml = postHtml;
            this.loadUrl = loadUrl;
            this.netType = netType;
            this.jsQueues = jsQueues;
        }

        public WebData() {
        }
    }

    /**
     * js 相关操作
     */
    public static class JsObj {
        public String js;
        public int delayTime = 10 * 1000;
        public int type;// 0是不需要处理 1需要phone 2需要code短信验证

        public JsObj(String js, int delayTime, int type) {
            this.js = js;
            this.delayTime = delayTime;
            this.type = type;
        }

        public JsObj(String js, int delayTime) {
            this.js = js;
            this.delayTime = delayTime;
        }


        public JsObj(String js) {
            this.js = js;
        }

        public JsObj() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JsObj jsObj = (JsObj) o;

            if (delayTime != jsObj.delayTime) return false;
            if (type != jsObj.type) return false;
            return js.equals(jsObj.js);
        }

        @Override
        public int hashCode() {
            int result = js.hashCode();
            result = 31 * result + delayTime;
            result = 31 * result + type;
            return result;
        }
    }

    /**
     * 实现弱引用
     */
    private static class InnerHandler extends Handler {
        private final WeakReference<Context> mContext;

        public InnerHandler(Context context) {
            super(Looper.getMainLooper());
            mContext = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mContext.get() == null) {
                return;
            }
        }
    }

}
