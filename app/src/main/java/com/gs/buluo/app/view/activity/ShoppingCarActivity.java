package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.CarListAdapter;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.ShoppingCarPresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IShoppingView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCarActivity extends BaseActivity implements IShoppingView, View.OnClickListener, CarListAdapter.CheckInterface, CarListAdapter.UpdateInterface {
    @Bind(R.id.car_list)
    ExpandableListView expandableListView;
    @Bind(R.id.car_edit)
    TextView editButton;
    @Bind(R.id.car_total_price)
    TextView tvTotal;
    @Bind(R.id.car_select_all)
    CheckBox checkBox;


    private CarListAdapter adapter;
    private boolean isEdit ;
    private List<ShoppingCart> data;
    private List<ShoppingCart> cartList;
    private float total = 0;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.car_edit).setOnClickListener(this);
        findViewById(R.id.car_back).setOnClickListener(this);
        findViewById(R.id.car_finish).setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setAllChecked(isChecked);
                calculateTotalPrice();
            }
        });
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
        cartList = body.content;
        adapter = new CarListAdapter(this, cartList);
        adapter.addCheckInterface(this);
        adapter.addGoodsChangedInterface(this);
        expandableListView.setAdapter(adapter);
        for (int i=0; i<adapter.getGroupCount();i++){
            expandableListView.expandGroup(i);
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
                adapter.setEdit(isEdit);
                break;
            case R.id.car_finish:
                Intent intent = new Intent(ShoppingCarActivity.this, NewOrderActivity.class);
                List<ShoppingCart> carts =new ArrayList<>();
                for (int i=0;i<cartList.size();i++){
                    ShoppingCart newCart = cartList.get(i);
                    List<ShoppingCart.ListGoodsListItem> list=new ArrayList<>();
                    for (int j=0;j<newCart.goodsList.size();j++){
                        ShoppingCart.ListGoodsListItem item = newCart.goodsList.get(j);
                        if (item.isSelected){
                            list.add(item);
                        }
                    }
                    if (list.size()!=0){
                        carts.add(newCart);
                    }
                }
                intent.putExtra("count",total/100);
                intent.putExtra("cart", (Serializable)carts);
                startActivity(intent);
                break;
            case R.id.car_back:
                finish();
                break;
        }
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        total=0;
        for (ShoppingCart cart:cartList){
            for (ShoppingCart.ListGoodsListItem item :cart.goodsList){
                if (item.isSelected){
                    total +=Float.parseFloat(item.goods.salePrice)*100*item.amount;
                }
            }
        }
        tvTotal.setText(total/100 +"");
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        ShoppingCart.ListGoodsListItem item= (ShoppingCart.ListGoodsListItem) adapter.getChild(groupPosition,childPosition);
        if (isChecked){
            total+= Float.parseFloat(item.goods.salePrice)*100*item.amount;
        }else {
            total-= Float.parseFloat(item.goods.salePrice)*100*item.amount;
        }

        tvTotal.setText(total/100+"");
    }

    @Override
    public void onUpdate() {
        calculateTotalPrice();
    }
}
