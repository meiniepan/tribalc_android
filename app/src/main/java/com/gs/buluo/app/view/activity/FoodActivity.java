package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.FoodGridAdapter;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IFoodShopView;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class FoodActivity extends BaseActivity implements View.OnClickListener ,IFoodShopView{
    @Bind(R.id.food_list)
    RecyclerView recyclerView;

    @Bind(R.id.foot_grid)
    GridView gridView;

    int[] icons = {R.mipmap.average_low, R.mipmap.average_high, R.mipmap.most_popular, R.mipmap.most_near, R.mipmap.most_comment};

    int[] iconsSelc = {R.mipmap.average_low_selc, R.mipmap.average_high_selc, R.mipmap.most_popular_selc, R.mipmap.most_near_selc, R.mipmap.most_comment_selc};
    private FoodGridAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.food_map).setOnClickListener(this);
        findViewById(R.id.food_sort).setOnClickListener(this);
        findViewById(R.id.food_filter).setOnClickListener(this);
        initGrid();
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
                if (gridView.getVisibility()==View.VISIBLE){
                    gridView.setVisibility(View.GONE);
                }else {
                    gridView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.food_filter:
                break;
        }
    }

    private void initGrid() {
        adapter = new FoodGridAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPos(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showError(int res) {

    }
}
