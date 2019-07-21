package moziqi.interviewdemo.subscribe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2018-2019
 * Author: ziqimo
 * Date: 2019/7/21 7:51 AM
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class SmsReceiver extends BroadcastReceiver {


    /**
     * https://blog.csdn.net/yuncaidaishu/article/details/88650731
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        StringBuilder content = new StringBuilder();//用于存储短信内容
        String sender = null;//存储短信发送方手机号
        Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
        String format = intent.getStringExtra("format");
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");//根据pdus关键字获取短信字节数组，数组内的每个元素都是一条短信
            for (Object object : pdus) {
                SmsMessage message = null;//将字节数组转化为Message对象
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    //https://www.cnblogs.com/peterpan-/p/6103817.html
                    message = SmsMessage.createFromPdu((byte[]) object, format);
                } else {
                    message = SmsMessage.createFromPdu((byte[]) object);
                }
                sender = message.getOriginatingAddress();//获取短信手机号
                content.append(message.getMessageBody());//获取短信内容
            }
        }
        String phoneNumberFromSMSText = getPhoneNumberFromSMSText(content.toString());//获取短信中的手机号码
    }


    /**
     * https://blog.csdn.net/jielysong117/article/details/55095592
     * 获取电话号码，存下来，刷东西
     *
     * @param sms
     * @return
     */
    public static String getPhoneNumberFromSMSText(String sms) {
        List<String> list = getNumberInString(sms);
        for (String str : list) {
            if (str.length() == 11)
                return str;
        }
        return "";
    }

    /**
     * 获取数字
     *
     * @param str
     * @return
     */
    public static List<String> getNumberInString(String str) {
        List<String> list = new ArrayList<String>();
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                list.add(m.group());
        }
        return list;
    }

    /**
     * 注册广播
     *
     * @param context
     * @param smsReceiver
     */
    public static void registerReceiver(Context context, SmsReceiver smsReceiver) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            context.registerReceiver(smsReceiver, filter);//注册广播接收器
        } catch (Exception e) {
            throw new RuntimeException(String.format("registerReceiver fail is %s", e.getMessage()));
        }
    }

    /**
     * 反注册广播
     *
     * @param context
     * @param smsReceiver
     */
    public static void unregisterReceiver(Context context, SmsReceiver smsReceiver) {
        try {
            context.unregisterReceiver(smsReceiver);
        } catch (Exception e) {
            throw new RuntimeException(String.format("unregisterReceiver fail is %s", e.getMessage()));
        }
    }
}
