package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.FoodGridAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/22.
 */
public class SortBoard extends PopupWindow{

    @Bind(R.id.foot_grid)
    GridView sortGridView;
    Context mContext;
    private FoodGridAdapter adapter;

    public SortBoard(Context context) {
        mContext=context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.sort_board, null);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);

        initGrid();
    }

    private void initGrid() {
        adapter = new FoodGridAdapter(mContext);
        sortGridView.setAdapter(adapter);
        sortGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPos(position);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
