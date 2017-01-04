package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.activity.OrderDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderListAdapter extends RecyclerAdapter<OrderBean> {
    Context mCtx;

    public OrderListAdapter(Context context) {
        super(context);
        mCtx=context;
    }

    @Override
    public BaseViewHolder<OrderBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new OrderItemHolder(parent);
    }

    class OrderItemHolder extends BaseViewHolder<OrderBean>{
        ListView listView;
        TextView number;
        TextView money;
        TextView statusView;
        View finishView;
        public OrderItemHolder(ViewGroup itemView) {
            super(itemView, R.layout.order_list_item);
        }

        @Override
        public void onInitializeView() {
            listView=findViewById(R.id.order_item_good_list);
            number=findViewById(R.id.order_item_number);
            money=findViewById(R.id.order_item_money);
            statusView=findViewById(R.id.order_item_status);
            finishView=findViewById(R.id.order_item_finish);
        }

        @Override
        public void setData(OrderBean entity) {
            super.setData(entity);
            if (entity==null||entity.itemList==null)return;
            number.setText(entity.orderNum);
            statusView.setText(transferStatus(entity.status));
            money.setText("¥ "+entity.totalFee);
            initGoodsList(listView,entity.itemList,entity);
        }

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

        @Override
        public void onItemViewClick(OrderBean entity) {
            goDetail(entity);
        }

        private void initGoodsList(ListView listView, List<CartItem> itemList, final OrderBean entity) {
            OrderGoodsAdapter adapter =new OrderGoodsAdapter(itemList,mCtx);
            listView.setAdapter(adapter);
            CommonUtils.setListViewHeightBasedOnChildren(listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    goDetail(entity);
                }
            });
        }
    }

    private void goDetail(OrderBean entity) {
        Intent intent=new Intent(mCtx,OrderDetailActivity.class);
        intent.putExtra(Constant.ORDER,entity);
        mCtx.startActivity(intent);
    }
}
