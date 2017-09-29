package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.OrderDetailGoodsAdapter;
import com.gs.buluo.app.bean.HomeMessageEnum;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.XgMessageReadBody;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.bean.XgMessageResponse;
import com.gs.buluo.app.eventbus.PaymentEvent;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.OrderPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.impl.IOrderView;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.app.view.widget.panel.PayPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.panel.SimpleChoosePanel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, IOrderView, SimpleChoosePanel.OnSelectedFinished {
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
    @Bind(R.id.order_detail_tag)
    TextView tvTips;
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
    @Bind(R.id.order_detail_counter)
    TextView tvCounter;
    @Bind(R.id.order_detail_cancel)
    Button tvNegative;

    private OrderBean bean;
    private String orderId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        tvNegative.setOnClickListener(this);
        findViewById(R.id.order_detail_button).setOnClickListener(this);
        Intent intent = getIntent();
        if ((orderId = intent.getStringExtra(Constant.ORDER_ID)) != null) {
            ((OrderPresenter) mPresenter).getOrder(orderId);
        } else
            bean = intent.getParcelableExtra(Constant.ORDER);
        if (bean != null) {
            initView();
            initData(bean);
        }
    }

    private void initView() {
        if (bean.status == OrderBean.OrderStatus.NO_SETTLE) {  //待付款
            findViewById(R.id.ll_order_detail_counter).setVisibility(View.VISIBLE);
            tvNegative.setVisibility(View.VISIBLE);
        } else if (bean.status == OrderBean.OrderStatus.SETTLE) {  //付款未发货
            findViewById(R.id.ll_send_time).setVisibility(View.GONE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvNegative.setVisibility(View.GONE);
            tvNegative.setText(R.string.refund);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvButton.setText(R.string.set_no_send);
            findViewById(R.id.ll_order_detail_counter).setVisibility(View.GONE);
        } else if (bean.status == OrderBean.OrderStatus.DELIVERY) { //待收货
            tvNegative.setVisibility(View.GONE);
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvButton.setText(R.string.set_receive);
            tvButton.setOnClickListener(this);
            findViewById(R.id.ll_order_detail_counter).setVisibility(View.GONE);
        } else if (bean.status == OrderBean.OrderStatus.RECEIVED) {  //完成 取消
            findViewById(R.id.ll_send_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_receive_time).setVisibility(View.VISIBLE);
            tvNegative.setVisibility(View.GONE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvReceiveTime.setText(TribeDateUtils.dateFormat7(new Date(bean.receivedTime)));
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
            findViewById(R.id.ll_order_detail_counter).setVisibility(View.GONE);
        } else if (bean.status == OrderBean.OrderStatus.REFUNDED) {
            findViewById(R.id.ll_send_time).setVisibility(View.GONE);
            findViewById(R.id.ll_pay_time).setVisibility(View.VISIBLE);
            tvNegative.setVisibility(View.GONE);
            tvPayTime.setText(TribeDateUtils.dateFormat7(new Date(bean.settleTime)));
            tvSendTime.setText(TribeDateUtils.dateFormat7(new Date(bean.deliveryTime)));
            tvButton.setText(R.string.send_goods);
            tvButton.setOnClickListener(this);
            findViewById(R.id.ll_order_detail_counter).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ll_send_time).setVisibility(View.GONE);
            findViewById(R.id.ll_pay_time).setVisibility(View.GONE);
            findViewById(R.id.ll_receive_time).setVisibility(View.GONE);
            tvNegative.setVisibility(View.GONE);
            findViewById(R.id.order_bottom).setVisibility(View.GONE);
            findViewById(R.id.ll_order_detail_counter).setVisibility(View.GONE);
        }
    }

    private void readMessage() {
        XgMessageReadBody body = new XgMessageReadBody();
        body.messageBodyType = HomeMessageEnum.ORDER_DELIVERY;
        body.referenceId = orderId;
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).readXgMessage(TribeApplication.getInstance().getUserInfo().getId(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<XgMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<XgMessageResponse> xgMessageResponseBaseResponse) {
                        TribeApplication.getInstance().getXgMessage();
                    }
                });
    }

    private void initData(final OrderBean order) {
        if (orderId != null)
            readMessage();
        if (order.store != null) {
            tvStoreName.setText(order.store.name);
        }
        String[] address = order.address.split("\\|");
        tvAddress.setText(address[2]);
        tvPhone.setText(address[1]);
        tvReceiver.setText(address[0]);
        tvOrderNum.setText(order.orderNum);
        tvCreateTime.setText(TribeDateUtils.dateFormat7(new Date(order.createTime)));
        if (order.status == OrderBean.OrderStatus.NO_SETTLE) setCounter(order.createTime);
        if (order.note != null) tvTips.setText(order.note);
        tvMethod.setText("包邮");
        tvSendPrice.setText(order.expressFee + "");

        tvTotal.setText(order.totalFee + "");
        if (order.store != null)
            tvStoreName.setText(order.store.name);

        OrderDetailGoodsAdapter adapter = new OrderDetailGoodsAdapter(order.itemList, this);
        lvGoods.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(lvGoods);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_cancel:
                if (bean.status == OrderBean.OrderStatus.NO_SETTLE) {
                    cancelOrder();
                }
                break;
            case R.id.order_detail_button:
                if (bean.status == OrderBean.OrderStatus.NO_SETTLE) {
                    PayPanel payPanel = new PayPanel(this, null);
                    List<String> ids = new ArrayList<>();
                    ids.add(bean.id);
                    payPanel.setData(bean.totalFee + "", ids, "order");
                    payPanel.show();
                } else if (bean.status == OrderBean.OrderStatus.DELIVERY) {
                    showLoadingDialog();
                    ((OrderPresenter) mPresenter).updateOrderStatus(bean.id, OrderBean.OrderStatus.RECEIVED.name());
                } else if (bean.status == OrderBean.OrderStatus.SETTLE) {
                    ToastUtils.ToastMessage(getCtx(), "已提醒商家发货");
                }
                break;
        }
    }

    private void cancelOrder() {
        new CustomAlertDialog.Builder(this).setMessage("确定取消订单？").setTitle("提示").setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoadingDialog();
                ((OrderPresenter) mPresenter).updateOrderStatus(bean.id, OrderBean.OrderStatus.CANCEL.name());
            }
        }).setNegativeButton(getString(R.string.cancel), null).create().show();
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
    public void updateSuccess(OrderBean order) {
        if (order.status == OrderBean.OrderStatus.RECEIVED) {
            ToastUtils.ToastMessage(this, "收货成功");
        } else {
            ToastUtils.ToastMessage(this, R.string.update_success);
        }
        EventBus.getDefault().post(new PaymentEvent());
        bean = order;
        initView();
        initData(order);
    }

    @Override
    public void getOrderDetail(OrderBean data) {
        bean = data;
        initView();
        initData(data);
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, res);
    }

    @Override
    public void getOrderInfoSuccess(OrderResponse data) {
    }

    //待付款订单页面 付款成功的通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void paySuccess(PaymentEvent event) {
        ((OrderPresenter) mPresenter).getOrder(bean.id);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);
    }

    public void setCounter(long createTime) {
        time = createTime + 24 * 3600 * 1000 - System.currentTimeMillis();
        if (time <= 0) {
            tvCounter.setText("已超时");
            return;
        }
        tvCounter.setText(TribeDateUtils.hourCounter(time));
        handler.postDelayed(runnable, 1000);
    }

    private long time;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time <= 0) {
                tvCounter.setText("时间到");
                handler.removeCallbacks(runnable);
                return;
            }
            time -= 1000;
            tvCounter.setText(TribeDateUtils.hourCounter(time));
            handler.postDelayed(this, 1000);
        }
    };

    private void refundOrder() {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("我不想卖了");
        messages.add("卖家缺货");
        messages.add("同城见面交易");
        messages.add("其他原因");
        new SimpleChoosePanel.Builder<String>(this, this)
                .setData(messages).setTitle("选择退款理由").build().show();
    }

    @Override
    public void onSelected(Object o) {

    }
}
