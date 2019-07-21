package moziqi.interviewdemo.subscribe;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Copyright (C), 2018-2019
 * Author: ziqimo
 * Date: 2019/7/21 7:47 AM
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class PhoneUtils {
    private static String phoneNum;

    /**
     * 获取本机号码
     *
     * @param context
     * @return
     */
    public static String getPhoneNum(Context context) {
        if (TextUtils.isEmpty(phoneNum)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            phoneNum = tm.getLine1Number();//获取本机号码
        }
        return phoneNum;
    }
}
