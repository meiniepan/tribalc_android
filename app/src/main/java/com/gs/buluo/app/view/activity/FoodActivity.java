package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

/**
 * Created by hjn on 2016/11/3.
 */
public class FoodActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.food_map).setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.food_map:
                startActivity(new Intent(FoodActivity.this,MapActivity.class));
                break;
            case R.id.ll_birthday:
                break;
        }
    }
}
