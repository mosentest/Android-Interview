package moziqi.interviewdemo.bingsearh;

import android.text.TextUtils;
import android.util.Log;

import org.wall.mo.utils.relect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * @author ziqi-mo
 */
public class NavigationControllerInvocationHandler implements InvocationHandler {

    private final static String TAG = "NavigationControllerInvocationHandler";

    private Object mTargetNavigationController;

    private String mPackageName;

    public NavigationControllerInvocationHandler(Object mObject, String packageName) {
        mTargetNavigationController = mObject;
        mPackageName = packageName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (TextUtils.isEmpty(mPackageName)) {
                mPackageName = "com.mo.aaaaa";
            }
            if (args != null && args.length > 0) {
                Class<?> clazz = args[0].getClass();
                String name = clazz.getName();
                //找到这个类
                //org.chromium.content_public.browser.LoadUrlParams
                Log.i("mo", "invoke.name:" + name);
                if ("org.chromium.content_public.browser.LoadUrlParams".equals(name)) {
                    Field[] fields = clazz.getFields();
                    for (Field field : fields) {
                        Log.i("mo", "invoke.field:" + field.getName() + "," + field.getType().getName());
                        if ("java.util.Map".equals(field.getType().getName())) {
                            Map<String, String> headerMap = (Map<String, String>) FieldUtils.readField(args[0], field.getName(), true);
                            if (headerMap != null) {
                                headerMap.remove("X-Requested-With");
                                headerMap.put("X-Requested-With", mPackageName);
                            }
                            FieldUtils.writeField(args[0], field.getName(), headerMap);
                            break;
                        }
                    }
                }

            }
            return method.invoke(mTargetNavigationController, args);
        } catch (Exception e) {
            Log.wtf(TAG, e);
        }
        return method.invoke(mTargetNavigationController, args);
    }
}