package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.MapActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/22.
 */
public class FilterBoard extends PopupWindow implements View.OnClickListener {

    @Bind(R.id.food_filter_room_img)
    ImageView roomImg;
    @Bind(R.id.food_filter_booking_img)
    ImageView bookImg;
    @Bind(R.id.food_filter_booking_text)
    TextView tvBooking;
    @Bind(R.id.food_filter_room_text)
    TextView tvRoom;

    Context mContext;
    public FilterBoard(Context context) {
        mContext=context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.filter_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);

        rootView.findViewById(R.id.food_filter_booking).setOnClickListener(this);
        rootView.findViewById(R.id.food_filter_room).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.food_filter_booking:
                tvRoom.setTextColor(0x90000000);
                tvBooking.setTextColor(mContext.getResources().getColor(R.color.custom_color));
                roomImg.setImageResource(R.mipmap.room);
                bookImg.setImageResource(R.mipmap.booking_selected);
                break;
            case R.id.food_filter_room:
                tvBooking.setTextColor(0x90000000);
                tvRoom.setTextColor(mContext.getResources().getColor(R.color.custom_color));
                roomImg.setImageResource(R.mipmap.room_selected);
                bookImg.setImageResource(R.mipmap.booking);
                break;
        }
    }

}
