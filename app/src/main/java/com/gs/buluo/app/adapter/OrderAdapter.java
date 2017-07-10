package com.gs.buluo.app.adapter;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.activity.OrderDetailActivity;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2017/7/7.
 */

public class OrderAdapter extends BaseQuickAdapter<OrderBean,BaseHolder> {


    public OrderAdapter(@LayoutRes int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }

    public OrderAdapter(@Nullable List<OrderBean> data) {
        super(data);
    }

    @Override
    protected void convert(BaseHolder helper, OrderBean item) {
        statusView = helper.getView(R.id.order_item_status);
        finishView = helper.getView(R.id.order_item_finish);
        helper.setText(R.id.order_item_number,item.orderNum)
                .setText(R.id.order_item_status,transferStatus(item.status))
                .setText(R.id.order_item_money,"¥"+item.totalFee);

        ListView  listView =helper.getView(R.id.order_item_good_list);
        initGoodsList(listView,item.itemList,item);
    }
    private void initGoodsList(ListView listView, List<CartItem> itemList, final OrderBean entity) {
        OrderGoodsAdapter adapter =new OrderGoodsAdapter(itemList,mContext);
        listView.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goDetail(entity);
            }
        });
    }
    private void goDetail(OrderBean entity) {
        Intent intent=new Intent(mContext,OrderDetailActivity.class);
        intent.putExtra(Constant.ORDER,entity);
        mContext.startActivity(intent);
    }


    private TextView statusView;
    private View finishView;
    private String transferStatus(OrderBean.OrderStatus status) {
        if (status== OrderBean.OrderStatus.NO_SETTLE){
            finishView.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
        }else if (status == OrderBean.OrderStatus.SETTLE){
            finishView.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
        }else if (status== OrderBean.OrderStatus.DELIVERY){
            finishView.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
        }else if (status == OrderBean.OrderStatus.RECEIVED){
            finishView.setVisibility(View.VISIBLE);
            statusView.setVisibility(View.GONE);
        }else {
            finishView.setVisibility(View.GONE);
            statusView.setVisibility(View.VISIBLE);
        }
        return status.toString();
    }
}
