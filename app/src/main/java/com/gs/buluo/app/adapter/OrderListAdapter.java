package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
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
    private int type=0;

    public OrderListAdapter(Context context) {
        super(context);
        mCtx=context;
    }

    @Override
    public BaseViewHolder<OrderBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new OrderItemHolder(parent);
    }

    public void setStatus(OrderBean.OrderStatus status) {
        if (status== OrderBean.OrderStatus.NO_SETTLE){
            type=1;
        }else if (status== OrderBean.OrderStatus.DELIVERY){
            type=2;
        }else if (status== OrderBean.OrderStatus.RECEIVED){
            type=3;
        }
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
            int total =0;
            for (int i=0;i<entity.itemList.size();i++){
                int amount = entity.itemList.get(i).amount;
                Double price =Double.parseDouble(entity.itemList.get(i).goods.salePrice);
                total+=amount*price;
            }
            statusView.setText(transferStatus(entity.status));
            money.setText("¥ "+total);
            initGoodsList(listView,entity.itemList,entity);
        }

        private int transferStatus(OrderBean.OrderStatus status) {
            setStatus(status);
            if (status== OrderBean.OrderStatus.NO_SETTLE){
                return R.string.wait_pay;
            }else if (status== OrderBean.OrderStatus.DELIVERY){
                return R.string.delivery_order;
            }else if (status == OrderBean.OrderStatus.RECEIVED){
                finishView.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                return R.string.cancel_order;
            }
            return R.string.cancel_order;
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
        intent.putExtra(Constant.TYPE,type);
        mCtx.startActivity(intent);
    }
}
