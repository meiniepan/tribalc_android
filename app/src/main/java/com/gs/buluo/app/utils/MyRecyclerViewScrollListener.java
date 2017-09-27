package com.gs.buluo.app.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Solang on 2017/9/25.
 */

public class MyRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    View mView;

    public MyRecyclerViewScrollListener(View view) {
        mView = view;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        // 当不滚动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 判断是否滚动超过一屏
            if (firstVisibleItemPosition == 0) {
                mView.setVisibility(View.INVISIBLE);
            } else {
                mView.setVisibility(View.VISIBLE);
            }

        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
//            mView.setVisibility(View.INVISIBLE);
        }
    }
}
