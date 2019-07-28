package moziqi.interviewdemo.subscribe;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2018-2019
 * Author: ziqimo
 * Date: 2019/7/21 8:19 AM
 * Description: ${DESCRIPTION}
 * History: https://blog.csdn.net/github_as/article/details/79502269
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class SmsObserver extends ContentObserver {


    public final static int MSG_RECEIVED_CODE = 1000;
    private Handler mHandler;
    private Context mContext;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String code;
        if (uri.toString().equals("content://sms/raw"))  //onChange会执行二次,第二次短信才会入库
        {
            return;
        }

        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri,
                null,
                null,
                null,
                "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                String body = c.getString(c.getColumnIndex("body"));//获取短信内容
                Pattern pattern = Pattern.compile("(\\d{6})");//正则表达式   连续6位数字
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    code = matcher.group(0);
                    mHandler.obtainMessage(MSG_RECEIVED_CODE, code).sendToTarget();
                }
            }
            c.close();
        }
    }


    public static void registerContentObserver(Context context, SmsObserver observer) {
        try {
            Uri uri = Uri.parse("content://sms");
            context.getContentResolver().registerContentObserver(uri, true, observer);
        } catch (Exception e) {

        }
    }

    public static void unregisterContentObserver(Context context, SmsObserver observer) {
        try {
            context.getContentResolver().unregisterContentObserver(observer);
        } catch (Exception e) {

        }
    }
}
