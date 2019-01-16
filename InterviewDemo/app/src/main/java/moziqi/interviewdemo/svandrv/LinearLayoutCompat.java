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
    private int y;

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


    float startY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getRawY();
                LogUtils.i(getTAG(), "onTouchEvent.ACTION_DOWN>>startY:" + startY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getRawY();
                LogUtils.i(getTAG(), "onTouchEvent.ACTION_MOVE>>endY:" + endY);
                LogUtils.i(getTAG(), "onTouchEvent.ACTION_MOVE>>xx:" + (endY - startY));
                int y = (int) (mScroller.getCurrY() + startY - endY);
                if (y > topY) {
                    return super.onTouchEvent(event);
                }
                if (y == getHeight()) {
                    return super.onTouchEvent(event);
                }
                smoothScrollTo(0, y);
                return true;
            case MotionEvent.ACTION_UP:
                float upY = event.getRawY();
                LogUtils.i(getTAG(), "onTouchEvent.ACTION_UP>>upY:" + upY);
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
