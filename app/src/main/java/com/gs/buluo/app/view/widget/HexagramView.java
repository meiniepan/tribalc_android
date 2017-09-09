package com.gs.buluo.app.view.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by hjn on 2017/9/7.
 */

public class HexagramView extends View {
    private Paint paint;
    private Path innerCircle;//内圆 path
    private Path outerCircle;//外圆 path
    private Path trangle1;//第一个三角形的 Path
    private Path trangle2;//第二个三角形的 Path
    private Path drawPath;//用于截取路径的 Path

    private PathMeasure pathMeasure;

    private float mViewWidth;
    private float mViewHeight;

    private long duration = 3000;
    private ValueAnimator valueAnimator;

    private Handler mHanlder;

    private float distance;//当前动画执行的百分比取值为0-1
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener;
    private Animator.AnimatorListener animatorListener;

    private State mCurrentState = State.CIRCLE_STATE;
    private RectF innerRect;

    private enum State {
        CIRCLE_STATE,
        TANGLE_STATE,
        FINISH_STATE
    }

    public HexagramView(Context context) {
        this(context, null);
    }

    public HexagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HexagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
        initPath();
        initAnimator();
        initAnimatorListener();
    }

    private void initAnimatorListener() {
        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    private void initPaint() {
        paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setShadowLayer(15, 0, 0, Color.WHITE);//白色光影效果
    }

    private void initPath() {
        innerCircle = new Path();
        outerCircle = new Path();
        trangle1 = new Path();
        trangle2 = new Path();
        drawPath = new Path();

        pathMeasure = new PathMeasure();
        innerRect = new RectF(-220, -220, 220, 220);
        RectF outerRect = new RectF(-280, -280, 280, 280);
        innerCircle.addArc(innerRect, 150, -359.9F);     // 不能取360f，否则可能造成测量到的值不准确
        outerCircle.addArc(outerRect, 60, -359.9F);

        pathMeasure.setPath(innerCircle, false);//开始一个内部三角的轨迹
        float[] floats = new float[2];
        pathMeasure.getPosTan(0, floats, null);
        trangle1.moveTo(floats[0], floats[1]);
        pathMeasure.getPosTan(1f / 3f * pathMeasure.getLength(), floats, null);
        System.out.println(floats[0] + ".................." + floats[1]);
        trangle1.lineTo(floats[0], floats[1]);
        pathMeasure.getPosTan(2f / 3f * pathMeasure.getLength(), floats, null);
        System.out.println(floats[0] + ".................." + floats[1]);
        trangle1.lineTo(floats[0], floats[1]);
        trangle1.close();

        Matrix matrix = new Matrix();
        matrix.postRotate(-180);
        trangle1.transform(matrix, trangle2);
    }

    private int progress = 100;
    private int mPro = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth / 2, mViewHeight / 2);

//        canvas.drawPath(innerCircle, paint);
        canvas.drawPath(outerCircle, paint);
        canvas.drawPath(trangle1, paint);
        canvas.drawPath(trangle2, paint);

        canvas.drawArc(innerRect, 0, 1, false, paint);

        if (mPro < progress) {
            mPro++;
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(2000);
        animator.addUpdateListener(animatorUpdateListener);
        animator.addListener(animatorListener);
    }
}
