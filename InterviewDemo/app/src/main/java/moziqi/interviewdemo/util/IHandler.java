package moziqi.interviewdemo.util;

import android.os.Handler;

public interface IHandler extends ICommon {

    /**
     * 因为view里面有getHandler方法，所以这里叫getMainHandler
     * @return
     */
    public Handler getMainHandler();

    public void setMainHandler(Handler mainHandler);
}
