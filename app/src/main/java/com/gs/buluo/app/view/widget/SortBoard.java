package com.gs.buluo.app.view.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ServeSortGridAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/22.
 */
public class SortBoard extends PopupWindow{

    @Bind(R.id.foot_grid)
    GridView sortGridView;
    Context mContext;
    private ServeSortGridAdapter adapter;
    private OnSelectListener onSelectListener;
    private int currentPos=-1;

    public SortBoard(Context context,OnSelectListener onSelectListener) {
        mContext=context;
        this.onSelectListener=onSelectListener;
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

    public interface OnSelectListener{
        void onSelected(String sort);
    }
}
