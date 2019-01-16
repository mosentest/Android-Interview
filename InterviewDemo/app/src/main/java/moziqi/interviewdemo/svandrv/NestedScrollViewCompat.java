package moziqi.interviewdemo.svandrv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Copyright (C), 2018-2019
 * Author: ziqimo
 * Date: 2019/1/16 下午9:43
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class NestedScrollViewCompat extends ScrollView {


    private OnScrollChanged mOnScrollChanged;

    public NestedScrollViewCompat(@NonNull Context context) {
        super(context);
    }

    public NestedScrollViewCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollViewCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mOnScrollChanged != null) {
            mOnScrollChanged.onScroll(x, y, oldx, oldy);
        }
    }

    public void setOnScrollChanged(OnScrollChanged onScrollChanged) {
        this.mOnScrollChanged = onScrollChanged;
    }

    public interface OnScrollChanged {
        void onScroll(int x, int y, int oldx, int oldy);
    }

}
