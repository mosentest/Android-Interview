package moziqi.interviewdemo.util;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/**
 * Copyright (C), 2018-2018
 * FileName: TouchUtils
 * Author: ziqimo
 * Date: 2018/11/30 下午10:36
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TouchUtils implements ILog {


    /**
     * https://www.jianshu.com/p/d83b2caa5249
     */
    public void simulationTouch(View view, float x, float y, float pixel) {
        LogUtils.i(getTAG(), "doTouch");

        long downTime = SystemClock.uptimeMillis();
        long eventTime = downTime + 100;

        int metaState = 0;

        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, metaState);


        view.dispatchTouchEvent(downEvent);

        downTime += 1000;
        eventTime = downTime + 100;

        MotionEvent moveEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_MOVE, x, y - pixel, metaState);

        view.dispatchTouchEvent(moveEvent);

        downTime += 2000;
        eventTime = downTime + 100;

        MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x, y - pixel, metaState);


        view.dispatchTouchEvent(upEvent);

        downEvent.recycle();
        moveEvent.recycle();
        upEvent.recycle();
    }

    @Override
    public String getTAG() {
        return "TouchUtils";
    }
}
