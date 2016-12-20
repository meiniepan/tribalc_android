package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.OrderDetailGoodsAdapter;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.OrderPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.impl.IOrderView;
import com.gs.buluo.app.view.widget.PayPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener,IOrderView {
    @Bind(R.id.order_detail_create_time)
    TextView tvCreateTime;
    @Bind(R.id.order_detail_address)
    TextView tvAddress;
    @Bind(R.id.order_detail_receiver)
    TextView tvReceiver;
    @Bind(R.id.order_detail_phone)
    TextView tvPhone;
    @Bind(R.id.order_detail_store_name)
    TextView tvStoreName;
    @Bind(R.id.order_detail_number)
    TextView tvOrderNum;
    @Bind(R.id.order_send_method)
    TextView tvMethod;
    @Bind(R.id.order_send_price)
    TextView tvSendPrice;

    @Bind(R.id.order_price_total)
    TextView tvTotal;
    @Bind(R.id.order_detail_goods_list)
    ListView lvGoods;
    @Bind(R.id.order_detail_pay_time)
    TextView tvPayTime;
    @Bind(R.id.order_detail_send_time)
    TextView tvSendTime;
    @Bind(R.id.order_detail_receive_time)
    TextView tvReceiveTime;
    @Bind(R.id.order_detail_button)
    TextView tvButton;

    private Context mCtx;
    private float total;
    private OrderBean bean;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        findViewById(R.id.order_detail_back).setOnClickListener(this);
        findViewById(R.id.order_detail_choose_address).setOnClickListener(this);
        findViewById(R.id.order_detail_cancel).setOnClickListener(this);
        tvButton.setOnClickListener(this);
        String addressID = new UserSensitiveDao().findFirst().getAddressID();
        UserAddressEntity entity = new AddressInfoDao().find(TribeApplication.getInstance().getUserInfo().getId(), addressID);
        if (null != entity) {
            String defaultsAddress = entity.getArea() + entity.getAddress();
            tvAddress.setText(defaultsAddress);
            tvPhone.setText(entity.getPhone());
            tvReceiver.setText(entity.getName());
        }
        bean = getIntent().getParcelableExtra(Constant.ORDER);
        int type=getIntent().getIntExtra(Constant.TYPE,0);
        if (type==1){  //待付款
            findViewById(R.id.order_detail_cancel).setVisibility(View.VISIBLE);
        }else if (type==2){  //付款未发货
            findViewById(R.id.ll_send_time).setVisibility(View.GONE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
//            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvButton.setText(R.string.set_no_send);
        }else if (type==3){ //待收货
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvButton.setText(R.string.set_receive);
            tvButton.setOnClickListener(this);
        }else {  //完成
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_receive_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvReceiveTime.setText(TribeDateUtils.dateFormat7(new Date(bean.receivedTime)));
            tvButton.setVisibility(View.GONE);
        }

        if (bean !=null){
            initData(bean);
        }
    }

    private void initData(final OrderBean order) {
        if (order.store!=null){
            tvStoreName.setText(order.store.name);
        }
        tvOrderNum.setText(order.orderNum);
        tvCreateTime.setText(TribeDateUtils.dateFormat7(new Date(order.createTime)));
        tvMethod.setText(order.expressType);
        tvSendPrice.setText(order.expressFee+"");

        total = 0;
        for (CartItem item:order.itemList){
            total +=Float.parseFloat(item.goods.salePrice)*100*item.amount/100;
        }
        tvTotal.setText(total +"");
        if (order.store!=null)
            tvStoreName.setText(order.store.name);
        float total=0;
        for (CartItem item :order.itemList){
            total +=item.amount*Float.parseFloat(item.goods.salePrice)*1000/1000;
        }

        OrderDetailGoodsAdapter adapter=new OrderDetailGoodsAdapter(order.itemList,this);
        lvGoods.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(lvGoods);

        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemId = order.itemList.get(position).goods.id;
                Intent intent = new Intent(mCtx,GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_ID,itemId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_back:
                finish();
                break;
            case R.id.order_detail_cancel:
                cancelOrder();
                break;
            case R.id.order_detail_button:
                if (bean.status== OrderBean.OrderStatus.NO_SETTLE){
                    PayPanel payPanel=new PayPanel(this,null);
                    List<String> ids=new ArrayList<>();
                    ids.add(bean.id);
                    payPanel.setData(OrderBean.PayChannel.BALANCE,total+"" , ids);
                    payPanel.show();
                }else if (bean.status== OrderBean.OrderStatus.DELIVERY){
                    ((OrderPresenter)mPresenter).updateOrderStatus(bean.id, OrderBean.OrderStatus.RECEIVED.name());
                }
                break;
            case R.id.order_detail_choose_address:
                Intent intent = new Intent(mCtx, AddressListActivity.class);
                intent.putExtra(Constant.ForIntent.FROM_ORDER,true);
                startActivityForResult(intent, Constant.REQUEST_ADDRESS);
                break;
        }
    }

    private void cancelOrder() {
        ((OrderPresenter)mPresenter).updateOrderStatus(bean.id, OrderBean.OrderStatus.CANCEL.name());
    }

    @Override
    protected BasePresenter getPresenter() {
        return new OrderPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constant.REQUEST_ADDRESS) {
            tvAddress.setText(data.getStringExtra(Constant.ADDRESS));
            tvReceiver.setText(data.getStringExtra(Constant.RECEIVER));
            tvPhone.setText(data.getStringExtra(Constant.PHONE));
        }
    }

    @Override
    public void updateSuccess() {
        ToastUtils.ToastMessage(this,R.string.update_success);
        finish();
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,res);
    }

    @Override
    public void getOrderInfoSuccess(OrderResponse.OrderResponseBean data) {
    }

}
