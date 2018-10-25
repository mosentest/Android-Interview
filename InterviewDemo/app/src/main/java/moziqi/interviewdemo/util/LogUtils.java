package moziqi.interviewdemo.util;

import android.util.Log;

public class LogUtils {

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }


    public static void i(String msg) {
        LogUtils.i("moziqi", msg);
    }
}
