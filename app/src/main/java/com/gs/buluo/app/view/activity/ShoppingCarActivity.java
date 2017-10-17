package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.CarListAdapter;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.eventbus.NewOrderEvent;
import com.gs.buluo.app.network.ShoppingApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.ShoppingCarPresenter;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IShoppingView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCarActivity extends BaseActivity implements IShoppingView, View.OnClickListener, CarListAdapter.CheckInterface, CarListAdapter.UpdateInterface {
    @BindView(R.id.car_list)
    ExpandableListView expandableListView;
    @BindView(R.id.car_edit)
    TextView editButton;
    @BindView(R.id.car_total_price)
    TextView tvTotal;
    @BindView(R.id.car_select_all)
    CheckBox checkBox;
    @BindView(R.id.car_finish)
    TextView finish;
    @BindView(R.id.car_list_layout)
    StatusLayout mStatusLayout;
    private CarListAdapter adapter;
    private boolean isEdit;
    private List<ShoppingCart> cartList;
    private float total = 0;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.car_edit).setOnClickListener(this);
        findViewById(R.id.car_back).setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        finish.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setAllChecked(isChecked);
                calculateTotalPrice();
            }
        });
        mStatusLayout.showProgressView();
        ((ShoppingCarPresenter) mPresenter).getShoppingListFirst();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shoppping_car;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(NewOrderEvent event) {
        removeSelected();
    }


    @Override
    protected BasePresenter getPresenter() {
        return new ShoppingCarPresenter();
    }

    @Override
    public void showError(int res) {
//        ToastUtils.ToastMessage(this, res);
        mStatusLayout.showErrorView(getString(res));
    }

    @Override
    public void getShoppingCarInfoSuccess(ShoppingCartResponse body) {
        mStatusLayout.showContentView();
        cartList = body.content;
        if (cartList.size() == 0) {
            mStatusLayout.showEmptyView(getString(R.string.no_goods));
            return;
        }
        adapter = new CarListAdapter(this, cartList);
        adapter.addCheckInterface(this);
        adapter.addGoodsChangedInterface(this);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_edit:
                if (adapter == null) return;
                if (isEdit) {
                    editButton.setText(R.string.edit);
                    finish.setText(getString(R.string.account));
                    isEdit = false;
                } else {
                    editButton.setText(R.string.finish);
                    finish.setText(getString(R.string.delete));
                    isEdit = true;
                }
                adapter.setEdit(isEdit);
                break;
            case R.id.car_finish:
                if (adapter == null) return;
                if (isEdit) {
                    deleteSelected();
                } else {
                    if (total == 0) {
                        ToastUtils.ToastMessage(this, "请选择结算商品");
                        return;
                    }
                    accountOrder();
                }
                break;
            case R.id.car_back:
                finish();
                break;
        }
    }

    private void accountOrder() {
        Intent intent = new Intent(ShoppingCarActivity.this, NewOrderActivity.class);
        ArrayList<ShoppingCart> carts = new ArrayList<>();
        for (int i = 0; i < cartList.size(); i++) {
            ShoppingCart cart = cartList.get(i);
            ShoppingCart newCart = new ShoppingCart();
            newCart.goodsList = new ArrayList<>();
            for (int j = 0; j < cart.goodsList.size(); j++) {
                CartItem item = cart.goodsList.get(j);
                if (item.isSelected) {
                    newCart.goodsList.add(item);
                }
            }
            if (newCart.goodsList.size() != 0) {
                newCart.id = cart.id;
                newCart.store = cart.store;
                carts.add(newCart);
            }
        }
        intent.putParcelableArrayListExtra("cart", carts);
        startActivity(intent);
    }

    private void deleteSelected() {
        if (cartList.size() == 0) return;
        StringBuffer sb = new StringBuffer();
        for (ShoppingCart cart : cartList) {
            for (CartItem item : cart.goodsList)
                if (item.isSelected) {
                    sb.append(item.id).append(",");
                }
        }
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).deleteCart(TribeApplication.getInstance().getUserInfo().getId(), sb.toString().substring(0, sb.length() - 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        removeSelected();
                        ToastUtils.ToastMessage(ShoppingCarActivity.this, R.string.delete_success);
                        if (cartList.size() == 0) {
                            editButton.setText(R.string.edit);
                            finish.setText(getString(R.string.account));
                            isEdit = false;
                            checkBox.setChecked(false);
                        }
                    }
                });
    }

    public void removeSelected() {
        Iterator<ShoppingCart> iterator = cartList.iterator();
        while (iterator.hasNext()) {
            ShoppingCart cart = iterator.next();
            Iterator<CartItem> iterator2 = cart.goodsList.iterator();
            while (iterator2.hasNext()) {
                CartItem item = iterator2.next();
                if (item.isSelected) {
                    iterator2.remove();
                }
            }
            if (cart.goodsList.size() == 0) {
                iterator.remove();
            }
        }
        adapter.notifyDataSetChanged();
        if (cartList.size() == 0) {
            mStatusLayout.showEmptyView(getString(R.string.no_goods));
        } else {
            calculateTotalPrice();
        }
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        total = 0;
        for (ShoppingCart cart : cartList) {
            for (CartItem item : cart.goodsList) {
                if (item.isSelected) {
                    total += Float.parseFloat(item.goods.salePrice) * 100 * item.amount;
                }
            }
        }
        tvTotal.setText(total / 100 + "");
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        CartItem item = (CartItem) adapter.getChild(groupPosition, childPosition);
        if (isChecked) {
            total += Float.parseFloat(item.goods.salePrice) * 100 * item.amount;
        } else {
            total -= Float.parseFloat(item.goods.salePrice) * 100 * item.amount;
        }

        tvTotal.setText(total / 100 + "");
    }

    @Override
    public void onUpdate() {
        if (cartList.size() == 0) {
            mStatusLayout.showEmptyView(getString(R.string.no_goods));
        }
        calculateTotalPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
