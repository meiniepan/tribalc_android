package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.NewOrderAdapter;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderRequestBody;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.NewOrderEvent;
import com.gs.buluo.app.eventbus.PaymentEvent;
import com.gs.buluo.app.eventbus.WXPayEvent;
import com.gs.buluo.app.network.ShoppingApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.widget.panel.PayPanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/5.
 */
public class NewOrderActivity extends BaseActivity implements View.OnClickListener, PayPanel.OnPayPanelDismissListener, NewOrderAdapter.onAmountCalculateFinishListener {
    @BindView(R.id.new_order_detail_goods_list)
    ListView listView;
    @BindView(R.id.new_order_price_total)
    TextView tvTotal;
    @BindView(R.id.new_order_detail_address)
    TextView address;
    @BindView(R.id.new_order_detail_receiver)
    TextView receiver;
    @BindView(R.id.new_order_detail_phone)
    TextView phone;

    private String addressID;
    private List<ShoppingCart> carts;

    private Context context;
    private NewOrderAdapter adapter;
    private PayPanel payBoard;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        context = this;
        EventBus.getDefault().register(this);
        orderType = getIntent().getIntExtra(Constant.NEWORDER_TYPE, 0);
        findViewById(R.id.new_order_back).setOnClickListener(this);
        findViewById(R.id.new_order_finish).setOnClickListener(this);
        findViewById(R.id.new_order_detail_choose_address).setOnClickListener(this);
        UserInfoEntity userSensitiveEntity = new UserInfoDao().findFirst();
        addressID = userSensitiveEntity.getAddressID();
        UserAddressEntity entity = new AddressInfoDao().find(TribeApplication.getInstance().getUserInfo().getId(), addressID);
        if (entity != null) {
            address.setText(entity.getArea() + entity.getAddress());
        } else {
            findViewById(R.id.new_order_address_receiver).setVisibility(View.GONE);
            address.setText(getString(R.string.please_add_address));
        }
        phone.setText(userSensitiveEntity.getPhone());
        receiver.setText(userSensitiveEntity.getNickname());

        carts = getIntent().getParcelableArrayListExtra("cart");
        if (carts == null) return;
        adapter = new NewOrderAdapter(this, carts, this);
        listView.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(listView);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_order;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_order_back:
                finish();
                break;
            case R.id.new_order_finish:
                createNewOrder();
                break;
            case R.id.new_order_detail_choose_address:
                Intent intent = new Intent(context, AddressListActivity.class);
                intent.putExtra(Constant.ForIntent.FROM_ORDER, true);
                startActivityForResult(intent, Constant.REQUEST_ADDRESS);
                break;
        }
    }

    private int orderType; // 0、购物车  1、直接购买

    private void createNewOrder() {
        if (addressID == null) {
            ToastUtils.ToastMessage(this, "请选择地址");
            return;
        }
        showLoadingDialog();
        NewOrderRequestBody body = new NewOrderRequestBody();
        body.addressId = addressID;
        body.itemList = new ArrayList<>();
        NewOrderBean bean;
        for (ShoppingCart cart : carts) {
            for (CartItem item : cart.goodsList) {
                bean = new NewOrderBean();
                bean.goodsId = item.goods.id;
                bean.amount = item.amount;
                bean.shoppingCartGoodsId = item.id;
                if (cart.note != null) bean.note = cart.note;
                body.itemList.add(bean);
            }
        }
        if (orderType == 0) {
            TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                    createNewOrder(TribeApplication.getInstance().getUserInfo().getId(), body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<List<OrderBean>>>() {
                        @Override
                        public void onNext(BaseResponse<List<OrderBean>> response) {
                            EventBus.getDefault().post(new NewOrderEvent());
                            showPayBoard(response.data);
                        }

                        @Override
                        public void onFail(ApiException e) {
                            if (e.getDisplayMessage() != null) {
                                ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                            } else if (e.getCode() == 412) {
                                ToastUtils.ToastMessage(getCtx(), R.string.not_enough_goods);
                            } else {
                                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                            }
                        }
                    });
        } else {
            TribeRetrofit.getInstance().createApi(ShoppingApis.class)
                    .createDirectNewOrder(TribeApplication.getInstance().getUserInfo().getId(), body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<List<OrderBean>>>() {
                        @Override
                        public void onNext(BaseResponse<List<OrderBean>> response) {
                            EventBus.getDefault().post(new NewOrderEvent());
                            showPayBoard(response.data);
                        }

                        @Override
                        public void onFail(ApiException e) {
                            if (e.getDisplayMessage() != null) {
                                ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                            } else if (e.getCode() == 412) {
                                ToastUtils.ToastMessage(getCtx(), R.string.not_enough_goods);
                            } else {
                                ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                            }
                        }
                    });
        }

    }

    private void showPayBoard(List<OrderBean> data) {
        List<String> ids = new ArrayList<>();
        boolean needShowPayPanel = false;
        float total = 0;
        for (OrderBean bean : data) {
            if (bean.status == OrderBean.OrderStatus.NO_SETTLE) {
                needShowPayPanel = true;
                ids.add(bean.id);
                total += bean.totalFee;
            }
        }
        if (needShowPayPanel) {
            payBoard = new PayPanel(this, this);
            payBoard.setData(total + "", ids, "order");
            payBoard.show();
        } else {
            startActivity(new Intent(this, OrderActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.REQUEST_ADDRESS) {
            findViewById(R.id.new_order_address_receiver).setVisibility(View.VISIBLE);
            address.setText(data.getStringExtra(Constant.ADDRESS));
            receiver.setText(data.getStringExtra(Constant.RECEIVER));
            phone.setText(data.getStringExtra(Constant.PHONE));
            addressID = data.getStringExtra(Constant.ADDRESS_ID);
        }
    }

    @Override
    public void onPayPanelDismiss() {
        startActivity(new Intent(this, OrderActivity.class));
        AppManager.getAppManager().finishActivity(ShoppingCarActivity.class);
        finish();
    }


    @Override
    public void onFinished(float amount) {
        //计算总价完成回调,adapter可能多次调用，只计算一次，如果做分页加载，另算
        if (tvTotal.length() == 0) tvTotal.setText(amount + "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWXPaySucess(WXPayEvent event) {
        if (payBoard != null) payBoard.dismiss();
        payBoard=null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPaySucess(PaymentEvent event) {
        if (payBoard != null) payBoard.dismiss();
        payBoard=null;
        finish();
    }
}
