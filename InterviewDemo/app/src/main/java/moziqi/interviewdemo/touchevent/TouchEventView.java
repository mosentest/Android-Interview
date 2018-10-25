package moziqi.interviewdemo.touchevent;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import moziqi.interviewdemo.util.IHandler;
import moziqi.interviewdemo.util.ILog;
import moziqi.interviewdemo.util.IViewTouchEvent;

public class TouchEventView extends TextView implements ILog, IHandler, IViewTouchEvent {


    private Handler handler;

    private int dispatch = 2;

    private int touchEvent = 1;

    public TouchEventView(Context context) {
        super(context);
    }

    public TouchEventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchEventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchEventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Message message = handler.obtainMessage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTAG());
        stringBuilder.append("===");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stringBuilder.append("dispatchTouchEvent>ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                stringBuilder.append("dispatchTouchEvent>ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                stringBuilder.append("dispatchTouchEvent>ACTION_MOVE");
                break;
            default:
                stringBuilder.append("dispatchTouchEvent>default");
        }
        message.what = 1;
        message.obj = stringBuilder.toString();
        handler.sendMessage(message);
        if (dispatch == 0) {
            return false;
        } else if (dispatch == 1) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Message message = handler.obtainMessage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTAG());
        stringBuilder.append("===");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stringBuilder.append("onTouchEvent>ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                stringBuilder.append("onTouchEvent>ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                stringBuilder.append("onTouchEvent>ACTION_MOVE");
                break;
            default:
                stringBuilder.append("onTouchEvent>default");
        }
        message.what = 1;
        message.obj = stringBuilder.toString();
        handler.sendMessage(message);
        if (touchEvent == 0) {
            return false;
        } else if (touchEvent == 1) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public String getTAG() {
        return "TouchEventView";
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
    public void setDispatchTouchEvent(int dispatch) {
        this.dispatch = dispatch;
    }

    @Override
    public void setTouchEvent(int touchEvent) {
        this.touchEvent = touchEvent;
    }
}
