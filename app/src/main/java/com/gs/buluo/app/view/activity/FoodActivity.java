package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.FoodGridAdapter;
import com.gs.buluo.app.adapter.ServiceListAdapter;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.view.impl.IFoodShopView;
import com.gs.buluo.app.view.widget.FilterBoard;
import com.gs.buluo.app.view.widget.SortBoard;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class FoodActivity extends BaseActivity implements View.OnClickListener, IFoodShopView {
    @Bind(R.id.food_list)
    RefreshRecyclerView refreshView;

    @Bind(R.id.food_sort_mark)
    ImageView sortMark;
    @Bind(R.id.food_filter_mark)
    ImageView filterMark;

    @Bind(R.id.food_filter_title)
    TextView tvFilter;
    @Bind(R.id.food_sort_title)
    TextView tvSort;

    View shadow;
    private FilterBoard filterBoard;
    private SortBoard sortBoard;
    private ServiceListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        shadow = findViewById(R.id.food_shadow);
        findViewById(R.id.food_map).setOnClickListener(this);
        findViewById(R.id.food_sort).setOnClickListener(this);
        findViewById(R.id.food_filter).setOnClickListener(this);
        findViewById(R.id.food_back).setOnClickListener(this);

        filterBoard = new FilterBoard(this);
        filterBoard.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shadow.setVisibility(View.GONE);
                filterMark.setImageResource(R.mipmap.down);
                tvFilter.setTextColor(0x90000000);
            }
        });
        sortBoard = new SortBoard(this);
        sortBoard.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shadow.setVisibility(View.GONE);
                sortMark.setImageResource(R.mipmap.down);
                tvSort.setTextColor(0x90000000);
            }
        });
        initList();

    }

    private void initList() {
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_food;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.food_map:
                startActivity(new Intent(FoodActivity.this, MapActivity.class));
                break;
            case R.id.food_sort:
                showSortBoard();
                break;
            case R.id.food_filter:
                showFilterBoard();
                break;
            case R.id.food_back:
                finish();
                break;
        }
    }

    private void showSortBoard() {
        shadow.setVisibility(View.VISIBLE);
        sortBoard.showAsDropDown(findViewById(R.id.food_parent), 0, 0);
        sortMark.setImageResource(R.mipmap.up_colored);
        tvSort.setTextColor(getResources().getColor(R.color.custom_color));
    }

    private void showFilterBoard() {
        shadow.setVisibility(View.VISIBLE);
        filterBoard.showAsDropDown(findViewById(R.id.food_parent), 0, 0);
        filterMark.setImageResource(R.mipmap.up_colored);
        tvFilter.setTextColor(getResources().getColor(R.color.custom_color));
    }

    @Override
    public void showError(int res) {

    }
}
