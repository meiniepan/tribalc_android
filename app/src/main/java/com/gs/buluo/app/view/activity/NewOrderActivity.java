package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.NewOrderAdapter;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderRequestBody;
import com.gs.buluo.app.bean.ResponseBody.NewOrderResponse;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.eventbus.NewOrderEvent;
import com.gs.buluo.app.model.ShoppingModel;
import com.gs.buluo.app.utils.AppManager;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.panel.PayPanel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/5.
 */
public class NewOrderActivity extends BaseActivity implements View.OnClickListener, PayPanel.OnPayPanelDismissListener {
    @Bind(R.id.new_order_detail_goods_list)
    ListView listView;
    @Bind(R.id.new_order_price_total)
    TextView tvTotal;
    @Bind(R.id.new_order_detail_address)
    TextView address;
    @Bind(R.id.new_order_detail_receiver)
    TextView receiver;
    @Bind(R.id.new_order_detail_phone)
    TextView phone;
    private float count;

    private String addressID;
    private List<ShoppingCart> carts;

    private Context context;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        context=this;
        findViewById(R.id.new_order_back).setOnClickListener(this);
        findViewById(R.id.new_order_finish).setOnClickListener(this);
        findViewById(R.id.new_order_detail_choose_address).setOnClickListener(this);
        count = getIntent().getFloatExtra("count",0);
        tvTotal.setText(count +"");
        UserSensitiveEntity userSensitiveEntity = new UserSensitiveDao().findFirst();
        addressID = userSensitiveEntity.getAddressID();
        UserAddressEntity entity = new AddressInfoDao().find(TribeApplication.getInstance().getUserInfo().getId(), addressID);
        if (entity!=null){
            address.setText(entity.getArea()+entity.getAddress());
        }else {
            findViewById(R.id.new_order_address_receiver).setVisibility(View.GONE);
            address.setText(getString(R.string.please_add_address));
        }
        phone.setText(userSensitiveEntity.getPhone());
        receiver.setText(userSensitiveEntity.getName());

        carts = getIntent().getParcelableArrayListExtra("cart");
        if (carts==null)return;
        NewOrderAdapter adapter=new NewOrderAdapter(this, carts);
        listView.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(listView);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_new_order;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_order_back:
                finish();
                break;
            case R.id.new_order_finish:
                createNewOrder();
                break;
            case R.id.new_order_detail_choose_address:
                Intent intent = new Intent(context, AddressListActivity.class);
                intent.putExtra(Constant.ForIntent.FROM_ORDER,true);
                startActivityForResult(intent, Constant.REQUEST_ADDRESS);
                break;
        }
    }

    private void createNewOrder() {
        if (addressID==null){
            ToastUtils.ToastMessage(this,"请选择地址");
            return;
        }
        NewOrderRequestBody body=new NewOrderRequestBody();
        body.addressId=addressID;
        body.itemList=new ArrayList<>();
        NewOrderBean bean;
        for (ShoppingCart cart:carts){
            for (CartItem item:cart.goodsList){
                bean=new NewOrderBean();
                bean.goodsId=item.goods.id;
                bean.amount=item.amount;
                bean.shoppingCartGoodsId =item.id;
                body.itemList.add(bean);
            }
        }
        new ShoppingModel().createNewOrder(body, new Callback<NewOrderResponse>() {
            @Override
            public void onResponse(Call<NewOrderResponse> call, Response<NewOrderResponse> response) {
                if (response.body()!=null&&response.body().code== ResponseCode.GET_SUCCESS){
                    EventBus.getDefault().post(new NewOrderEvent());
                    showPayBoard(response.body().data);
                }else {
                    ToastUtils.ToastMessage(context,R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<NewOrderResponse> call, Throwable t) {
                ToastUtils.ToastMessage(context,R.string.connect_fail);
            }
        });
    }
    private void showPayBoard(List<OrderBean> data) {
        float total=0;
        List<String> ids=new ArrayList<>();
        for (OrderBean bean:data) {
            ids.add(bean.id);
            total+=bean.totalFee;
        }
        PayPanel payBoard=new PayPanel(this,this);
        payBoard.setData(total+"",ids, "order");
        payBoard.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.REQUEST_ADDRESS) {
            findViewById(R.id.new_order_address_receiver).setVisibility(View.VISIBLE);
            address.setText(data.getStringExtra(Constant.ADDRESS));
            receiver.setText(data.getStringExtra(Constant.RECEIVER));
            phone.setText(data.getStringExtra(Constant.PHONE));
            addressID=data.getStringExtra(Constant.ADDRESS_ID);
        }
    }

    @Override
    public void onPayPanelDismiss() {
        startActivity(new Intent(this,OrderActivity.class));
        AppManager.getAppManager().finishActivity(ShoppingCarActivity.class);
        finish();
    }
}
