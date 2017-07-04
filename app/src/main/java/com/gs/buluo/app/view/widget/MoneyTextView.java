package com.gs.buluo.app.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.DimenTool;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.DensityUtils;

/**
 * Created by hjn on 2017/7/4.
 */

public class MoneyTextView extends LinearLayout {
    private Context context;
    private TextView point;

    public MoneyTextView(Context context) {
        this(context, null);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private TextView bigger;
    private TextView smaller;
    private void init(@Nullable AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        bigger = new TextView(context);
        smaller = new TextView(context);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MoneyTextView);
        int color = array.getColor(R.styleable.MoneyTextView_textColor, context.getResources().getColor(R.color.common_dark));
        float size = array.getDimension(R.styleable.MoneyTextView_textSize, DensityUtils.dip2px(context,20));
        int style = array.getInteger(R.styleable.MoneyTextView_textStyle, Typeface.NORMAL);
        bigger.setTypeface(Typeface.defaultFromStyle(style));//加粗
        bigger.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        smaller.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        bigger.setTextColor(color);
        smaller.setTextColor(color);
        array.recycle();

        point = new TextView(context);
        point.setTextColor(color);
        point.setText(".");
    }

    public void setMoneyText(String money) {
        if (money == null) return;
        String[] arrs = money.split("\\.");
        if (arrs.length > 1) {
            bigger.setText(arrs[0]);
            smaller.setText(arrs[1]);
        } else {
            bigger.setText(money);
            smaller.setText("00");
        }
        addView(bigger);
        addView(point);
        addView(smaller);
    }

}
