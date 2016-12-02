package com.gs.buluo.app.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.app.R;

/**
 * Created by hjn on 2016/12/1.
 */
public class CustomSelectView extends LinearLayout {
    private boolean status ;
    private TextView content;

    public CustomSelectView(Context context) {
        this(context,null);
    }

    public CustomSelectView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_select,this);
        final ImageView imageView= (ImageView) view.findViewById(R.id.custom_select);
        content = (TextView) view.findViewById(R.id.custom_view);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status){
                    imageView.setImageResource(R.mipmap.custom_selected);
                    status=true;
                }else {
                    imageView.setImageResource(R.mipmap.custom_un_select);
                    status=false;
                }
            }
        });
    }

    public void setText(String text){
        content.setText(text);
    }

    public boolean getStatus(){
        return status;
    }

}
