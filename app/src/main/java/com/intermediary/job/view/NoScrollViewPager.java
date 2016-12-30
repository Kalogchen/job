package com.intermediary.job.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 让viewPager不能左右滑动
 * Created by kalogchen on 2016/12/23.
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 拦截onTouch事件，让其什么事情都不做
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
