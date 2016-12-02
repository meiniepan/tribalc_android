package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.OrderDetailGoodsAdapter;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.TribeDateUtils;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
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
    @Bind(R.id.order_detail_button)
    TextView tvButton;
    private Context mCtx;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        findViewById(R.id.order_detail_back).setOnClickListener(this);
        findViewById(R.id.order_detail_choose_address).setOnClickListener(this);
        String addressID = new UserSensitiveDao().findFirst().getAddressID();
        UserAddressEntity entity = new AddressInfoDao().find(TribeApplication.getInstance().getUserInfo().getId(), addressID);
        if (null != entity) {
            String defaultsAddress = entity.getArea() + entity.getAddress();
            tvAddress.setText(defaultsAddress);
            tvPhone.setText(entity.getPhone());
            tvReceiver.setText(entity.getName());
        }
        OrderBean bean = (OrderBean) getIntent().getSerializableExtra(Constant.ORDER);
        int type=getIntent().getIntExtra(Constant.TYPE,0);
        if (type==2){  //待收货
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_money).setVisibility(View.GONE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvButton.setText(R.string.set_receive);

        }

        if (bean!=null){
            initData(bean);
        }
    }

    private void initData(OrderBean order) {
        if (order.store!=null){
            tvStoreName.setText(order.store.name);
        }
        tvOrderNum.setText(order.orderNum);
        tvCreateTime.setText(TribeDateUtils.dateFormat7(new Date(order.createTime)));
        tvMethod.setText(order.expressType);
        tvSendPrice.setText(order.expressFee+"");


        order.itemList.add(order.itemList.get(0));
        order.itemList.add(order.itemList.get(0));
        OrderDetailGoodsAdapter adapter=new OrderDetailGoodsAdapter(order.itemList,this);
        lvGoods.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,order.itemList.size() * DensityUtils.dip2px(mCtx,96)));
        lvGoods.setAdapter(adapter);

        tvTotal.setText(order.totalFee+"");

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
            case R.id.order_detail_choose_address:
                startActivityForResult(new Intent(mCtx, AddressListActivity.class), Constant.REQUEST_ADDRESS);
                break;
        }
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
}