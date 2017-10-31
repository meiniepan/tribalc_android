package com.gs.buluo.app.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * Created by Solang on 2017/10/31.
 */

public class CustomWheelRecyclerView extends RecyclerView {
    private VelocityTracker mVelocityTracker = null;

    public CustomWheelRecyclerView(Context context) {
        super(context);
    }

    public CustomWheelRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                //1000是1000毫秒的意思，即获取每秒的速率
                mVelocityTracker.computeCurrentVelocity(1000);
                Log.e("xz", mVelocityTracker.getXVelocity(0) + "");
                Log.i("xz", mVelocityTracker.getYVelocity(0) + "");
                break;
        }
        return true;
    }
}
