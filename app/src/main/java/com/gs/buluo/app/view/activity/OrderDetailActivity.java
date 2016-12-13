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
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.widget.PayPanel;

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
    private float total;

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
        OrderBean bean =getIntent().getParcelableExtra(Constant.ORDER);
        int type=getIntent().getIntExtra(Constant.TYPE,0);
        if (type==1){
            findViewById(R.id.order_detail_cancel).setVisibility(View.VISIBLE);
        }else if (type==2){  //待收货
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvButton.setText(R.string.set_receive);
        }

        if (bean!=null){
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
            total +=item.amount*Float.parseFloat(item.goods.salePrice)*100/100;
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
                finish();
                break;
            case R.id.order_detail_button:
                PayPanel payPanel=new PayPanel(this,null);
                payPanel.setData("",total+"");
                payPanel.show();
                break;
            case R.id.order_detail_choose_address:
                Intent intent = new Intent(mCtx, AddressListActivity.class);
                intent.putExtra(Constant.FROM_ORDER,true);
                startActivityForResult(intent, Constant.REQUEST_ADDRESS);
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
