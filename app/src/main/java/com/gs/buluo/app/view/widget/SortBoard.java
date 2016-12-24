package com.gs.buluo.app.view.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ServeSortGridAdapter;
import com.gs.buluo.app.utils.zxing.view.ViewfinderView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/22.
 */
public class SortBoard extends PopupWindow implements View.OnClickListener {

    @Bind(R.id.foot_grid)
    GridView sortGridView;

    @Bind(R.id.food_filter_room_img)
    ImageView roomImg;
    @Bind(R.id.food_filter_booking_img)
    ImageView bookImg;
    @Bind(R.id.food_filter_booking_text)
    TextView tvBooking;
    @Bind(R.id.food_filter_room_text)
    TextView tvRoom;

    Context mContext;
    private ServeSortGridAdapter adapter;
    private OnSelectListener onSelectListener;
    private int currentPos=-1;
    private View rootView;

    public SortBoard(Context context,OnSelectListener onSelectListener) {
        mContext=context;
        this.onSelectListener=onSelectListener;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.sort_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(false);
        setTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(false);

        rootView.findViewById(R.id.food_filter_booking).setOnClickListener(this);
        rootView.findViewById(R.id.food_filter_room).setOnClickListener(this);
        rootView.findViewById(R.id.sort_empty).setOnClickListener(this);
        initGrid();
    }

    private void initGrid() {
        adapter = new ServeSortGridAdapter(mContext);
        sortGridView.setAdapter(adapter);
        sortGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPos(position);
                adapter.notifyDataSetChanged();
                if (position!=currentPos)
                    onSelectListener.onSelected(getPositionSort(position));
                dismiss();
            }
        });
    }

    private String  getPositionSort(int position) {
        currentPos=position;
        if (position==0){
            return Constant.SORT_PERSON_EXPENSE_ASC;
        }else if (position==1){
            return Constant.SORT_PERSON_EXPENSE_DESC;
        }else {
            return Constant.SORT_POPULAR;
        }
    }

    public void setFilterVisible() {
        sortGridView.setVisibility(View.GONE);
        rootView.findViewById(R.id.food_filter_view).setVisibility(View.VISIBLE);
    }
    public void setSortVisible() {
        sortGridView.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.food_filter_view).setVisibility(View.GONE);
    }


    public interface OnSelectListener{
        void onSelected(String sort);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.food_filter_booking:
                tvRoom.setTextColor(0x90000000);
                tvBooking.setTextColor(mContext.getResources().getColor(R.color.custom_color));
                roomImg.setImageResource(R.mipmap.room_select);
                bookImg.setImageResource(R.mipmap.booking_selected);
                dismiss();
                break;
            case R.id.food_filter_room:
                tvBooking.setTextColor(0x90000000);
                tvRoom.setTextColor(mContext.getResources().getColor(R.color.custom_color));
                roomImg.setImageResource(R.mipmap.room_selected);
                bookImg.setImageResource(R.mipmap.booking);
                dismiss();
                break;
            case R.id.sort_empty:
                dismiss();
                break;
        }
    }
}
