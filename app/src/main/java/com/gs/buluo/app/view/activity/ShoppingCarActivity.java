package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CarListAdapter;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.ShoppingCarPresenter;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IShoppingView;
import com.gs.buluo.app.view.widget.RecycleViewDivider;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCarActivity extends BaseActivity implements IShoppingView, View.OnClickListener {
    @Bind(R.id.car_list)
    RefreshRecyclerView recyclerView;
    @Bind(R.id.car_edit)
    TextView editButton;

    private CarListAdapter adapter;
    private boolean isEdit ;
    private List<ShoppingCart> data;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new CarListAdapter(this);
        recyclerView.setNeedLoadMore(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,8), getResources().getColor(R.color.tint_bg)));
        recyclerView.setAdapter(adapter);
        findViewById(R.id.car_edit).setOnClickListener(this);
        findViewById(R.id.car_back).setOnClickListener(this);
        findViewById(R.id.car_finish).setOnClickListener(this);
        showLoadingDialog();
        ((ShoppingCarPresenter)mPresenter).getShoppingListFirst();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shoppping_car;
    }


    @Override
    protected BasePresenter getPresenter() {
        return new ShoppingCarPresenter();
    }

    @Override
    public void showError(int res) {
        dismissDialog();
        ToastUtils.ToastMessage(this,res);
    }

    @Override
    public void getShoppingCarInfoSuccess(ShoppingCartResponse.ShoppingCartResponseBody body) {
        dismissDialog();
        if (body.content==null||body.content.size()==0){
            recyclerView.showNoData(R.string.empty_car);
        }else {
            data = body.content;
            adapter.addAll(data);
            if (!body.hasMore){
                adapter.showNoMore();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_edit:
                if (isEdit){
                    editButton.setText(R.string.edit);
                    isEdit=false;
                }else {
                    editButton.setText(R.string.finish);
                    isEdit=true;
                }
                showEdit(isEdit);
                break;
            case R.id.car_finish:
                startActivity(new Intent(ShoppingCarActivity.this,NewOrderActivity.class));
                break;
            case R.id.car_back:
                finish();
                break;
        }
    }

    private void showEdit(boolean isEdit) {
        if (isEdit){
            for (ShoppingCart cart:data){
                for (ShoppingCart.ListGoodsList goodsList:cart.tListGoodsList){
                    goodsList.isEdit=true;
                    adapter.notifyDataSetChanged();
                }
            }
        }else {
            for (ShoppingCart cart:data){
                for (ShoppingCart.ListGoodsList goodsList:cart.tListGoodsList){
                    goodsList.isEdit=false;
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }


}
