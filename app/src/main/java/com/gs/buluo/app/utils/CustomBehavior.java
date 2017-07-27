package com.gs.buluo.app.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gs.buluo.app.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.ref.WeakReference;

public class CustomBehavior extends CoordinatorLayout.Behavior {


    private WeakReference<View> dependentView;
    private WeakReference<View> childView;
    private boolean isScroll;
    private boolean isExpand;
    private int headSize = -1;
    private int minHead = -1;
    private boolean mCanRefresh;

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (child != null) {
            childView = new WeakReference<View>(child);
        }
        if (dependency != null && dependency instanceof RelativeLayout) {
            dependentView = new WeakReference<>(dependency);
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {


        child.layout(0, 0, parent.getWidth(), (parent.getHeight() - dependentView.get().getHeight()));
        if (headSize == -1) {
            headSize = dependentView.get().getHeight();
            minHead = dependentView.get().findViewById(R.id.rl_icon).getHeight();
            child.setTranslationY(headSize);
        }

        return true;

    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        View view = dependentView.get();
        mCanRefresh = false;
        float translationY = child.getTranslationY();

        float min = minHead * 1.0f / headSize;
        float pro = (translationY) / headSize;
        View child1 = view.findViewById(R.id.ll);
        View searchView = view.findViewById(R.id.tv_search);
        searchView.setVisibility(View.GONE);
        child1.setPivotY(0);
        child1.setPivotX(0);
        View titleView = dependentView.get().findViewById(R.id.rl_icon);
        titleView.setPivotY(0);
        titleView.setPivotX(0);
        titleView.setAlpha(1 - pro);
        if (pro == min ) {
            titleView.setAlpha(1);
        }
        if (pro == 1) {
            titleView.setVisibility(View.GONE);
            mCanRefresh = true;
            searchView.setVisibility(View.VISIBLE);
        } else {
            titleView.setVisibility(View.VISIBLE);

        }
        if (pro >= min && pro <= 1) {

            child1.setAlpha(pro);
            if (pro <= min + 0.1) {
                mCanRefresh = false;
                child1.setVisibility(View.GONE);
            } else {
                child1.setVisibility(View.VISIBLE);
            }

            return true;
        }

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        clearAnimotor();
        isScroll = false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        View view = dependentView.get();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int height = (int) child.getTranslationY();
        if (dy > 0 && height > minHead) {
            if (height <= headSize) {
                int h = height - dy;
                int H = (h < minHead) ? minHead : h;
                params.height = H;
                view.setLayoutParams(params);
                child.setTranslationY(H);
                consumed[1] = dy;
            }


        }


    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (child instanceof XRecyclerView) {
            XRecyclerView recycleView = (XRecyclerView) child;
            if (mCanRefresh){
                recycleView.setPullRefreshEnabled(true);
            }else {
                recycleView.setPullRefreshEnabled(false);
            }
        }
        if (dyUnconsumed > 0) {
            return;
        }

        View view = dependentView.get();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int height = (int) child.getTranslationY();
        if (dyUnconsumed < 0 && params != null) {
            int h = height - dyUnconsumed;

            if (h >= 0 && h <= headSize) {
                params.height = h;
                view.setLayoutParams(params);
                child.setTranslationY(h);
            }

        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return onStopDrag(child, velocityY);

    }

    private boolean onStopDrag(View child, float velocityY) {
        int height = dependentView.get().getHeight();
        if (height > minHead) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {

        int height = dependentView.get().getHeight();
        float translationY = childView.get().getTranslationY();
        if (translationY > height) {
            isExpand = true;
        } else {
            isExpand = false;
        }

        if (isExpand) {
            float pro = ((translationY - height) * 1.0f / headSize);
            creatExpendAnimator(translationY, height, (int) (500 * pro));
        }


        if (!isScroll && height > minHead && height < headSize) {
            childView.get().setScrollY(0);
            if (height < 0.7 * headSize) {//上滑
                float pro = (height - minHead) * 1.0f / (headSize - minHead);
                creatAnimation(height, minHead, (int) (500 * pro));
            } else {//下滑
                float pro = (headSize - height) * 1.0f / (headSize - minHead);
                creatAnimation(height, headSize, (int) (500 * pro));
            }
            isScroll = true;
        }


    }


    private ValueAnimator animator;

    private void creatAnimation(float start, float end, int duration) {
        clearAnimotor();
        animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                View view = dependentView.get();
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = (int) value;
                view.setLayoutParams(params);
                childView.get().setTranslationY(value);

            }
        });
        animator.setDuration(duration);
        animator.start();


    }

    private void creatExpendAnimator(float start, float end, int duration) {
        clearAnimotor();
        animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
//                View view = dependentView.get();
//                ViewGroup.LayoutParams params = view.getLayoutParams();
//                params.height = (int) value;
//                view.setLayoutParams(params);
                childView.get().setTranslationY(value);

            }
        });
        animator.setDuration(duration);
        animator.start();
    }


    private void clearAnimotor() {
        if (animator != null) {
            animator.cancel();
        }


        isScroll = false;
    }

}
