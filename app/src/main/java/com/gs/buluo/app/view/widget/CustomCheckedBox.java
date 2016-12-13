package com.gs.buluo.app.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.DensityUtils;

/**
 * Created by hjn on 2016/12/1.
 */
public class CustomCheckedBox extends RelativeLayout implements CompoundButton.OnCheckedChangeListener {
    private final Context context;
    private boolean status ;
    private TextView content;
    private OnCheckChangedListener onCheckedListener;

    public CustomCheckedBox(Context context) {
        this(context,null);
    }

    public CustomCheckedBox(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomCheckedBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initView();
    }

    private void initView() {
        CheckBox box=new CheckBox(context);
        box.setGravity(CENTER_IN_PARENT);
        this.addView(box);
        box.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        onCheckedListener.onCheckedChanged(isChecked);
    }

    public void setOnCheckedListener(OnCheckChangedListener onCheckedListener){
        this.onCheckedListener=onCheckedListener;
    }

    public interface OnCheckChangedListener{
        void onCheckedChanged(boolean isChecked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i =getChildCount();
        View view =getChildAt(0);
        measureChild(view,DensityUtils.dip2px(context,18),DensityUtils.dip2px(context,18));
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
}
