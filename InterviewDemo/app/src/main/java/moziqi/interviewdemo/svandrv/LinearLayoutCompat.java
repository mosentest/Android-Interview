package moziqi.interviewdemo.svandrv;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

import moziqi.interviewdemo.util.ILog;
import moziqi.interviewdemo.util.LogUtils;

/**
 * Copyright (C), 2018-2019
 * Author: ziqimo
 * Date: 2019/1/16 下午10:12
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class LinearLayoutCompat extends LinearLayout implements ILog {

    private Scroller mScroller;


    private int topY;

    private float mYDownPos;

    private float mYMovePos;
    private float mYLastMovePos;

    public LinearLayoutCompat(Context context) {
        super(context);
        init();
    }

    public LinearLayoutCompat(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearLayoutCompat(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mScroller = new Scroller(getContext());
    }

    public void setTopY(int topY) {
        this.topY = topY;
        LogUtils.i(getTAG(), "topY>>" + topY);
    }


    private int mScrollY;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollY = t;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDownPos = event.getRawY();
                mYLastMovePos = mYMovePos;
                break;
            case MotionEvent.ACTION_MOVE:
                mYMovePos = event.getRawY();
//                LogUtils.i(getTAG(), "onInterceptTouchEvent.ACTION_MOVE>>mScroller.getFinalY():" + mScroller.getFinalY());
//                LogUtils.i(getTAG(), "onInterceptTouchEvent.ACTION_MOVE>>mScroller.getScrollY():" + getScrollY());
                //LogUtils.i(getTAG(), "onInterceptTouchEvent.ACTION_MOVE>>mScroller.topY:" + topY);
                //topY:263
                int scrolledY = (int) (mYDownPos - mYMovePos);


                LogUtils.i(getTAG(), "onInterceptTouchEvent.ACTION_MOVE>>scrolledY" + scrolledY);
                LogUtils.i(getTAG(), "onInterceptTouchEvent.ACTION_MOVE>>getScrollY()" + getScrollY());
                LogUtils.i(getTAG(), "onInterceptTouchEvent.ACTION_MOVE>>mScrollY" + mScrollY);

                if (mScroller.getFinalY() >= topY) {
                    LogUtils.i(getTAG(), "onInterceptTouchEvent....");
                    return super.onInterceptTouchEvent(event);
                }
                return true;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mYDownPos = event.getRawY();
                mYLastMovePos = mYMovePos;
                break;
            case MotionEvent.ACTION_MOVE:
                mYMovePos = event.getRawY();
                int scrolledY = (int) (mYDownPos - mYMovePos);

                LogUtils.i(getTAG(), "onTouchEvent.ACTION_MOVE>>scrolledY" + scrolledY);
//                LogUtils.i(getTAG(), "onTouchEvent.ACTION_MOVE>>getScrollY()" + getScrollY());

                LogUtils.i(getTAG(), "onTouchEvent.ACTION_MOVE>>mScroller.getFinalY()" + mScroller.getFinalY());
                if (mScroller.getFinalY() >= topY) {
                    LogUtils.i(getTAG(), "onTouchEvent....333");
                    return false;
                }
                smoothScrollTo(0, scrolledY);
                return true;
            case MotionEvent.ACTION_UP:
                mYMovePos = event.getRawY();
                break;
        }
        return super.onTouchEvent(event);
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {
        //设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 100);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    @Override
    public void computeScroll() {

        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public String getTAG() {
        return "LinearLayoutCompat";
    }
}
