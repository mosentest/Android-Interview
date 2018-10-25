package moziqi.interviewdemo.touchevent;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import moziqi.interviewdemo.util.IHandler;
import moziqi.interviewdemo.util.ILog;
import moziqi.interviewdemo.util.IViewTouchEvent;

public class TouchEventViewGroup extends FrameLayout implements ILog, IHandler, IViewTouchEvent {


    private Handler handler;

    private int dispatch = 2;

    private int touchEvent = 1;

    public TouchEventViewGroup(@NonNull Context context) {
        super(context);
    }

    public TouchEventViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEventViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchEventViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public Handler getMainHandler() {
        return handler;
    }

    @Override
    public void setMainHandler(Handler mainHandler) {
        this.handler = mainHandler;
    }

    @Override
    public String getTAG() {
        return "TouchEventViewGroup";
    }

    @Override
    public void setDispatchTouchEvent(int dispatch) {

    }

    @Override
    public void setTouchEvent(int touchEvent) {

    }
}
