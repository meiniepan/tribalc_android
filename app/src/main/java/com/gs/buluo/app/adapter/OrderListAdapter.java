package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.view.activity.OrderDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by hjn on 2016/11/25.
 */
public class OrderListAdapter extends RecyclerAdapter<OrderBean> {
    Context mCtx;
    private int type;

    public OrderListAdapter(Context context) {
        super(context);
        mCtx=context;
    }

    @Override
    public BaseViewHolder<OrderBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new OrderItemHolder(parent);
    }

    public void setType(int type) {
        this.type = type;
    }

    class OrderItemHolder extends BaseViewHolder<OrderBean>{
        ListView listView;
        TextView number;
        TextView money;

        public OrderItemHolder(ViewGroup itemView) {
            super(itemView, R.layout.order_list_item);
        }

        @Override
        public void onInitializeView() {
            listView=findViewById(R.id.order_item_good_list);
            number=findViewById(R.id.order_item_number);
            money=findViewById(R.id.order_item_money);
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
            money.setText("¥ "+total);
            initGoodsList(listView,entity.itemList);
        }

        @Override
        public void onItemViewClick(OrderBean entity) {
            Intent intent=new Intent(mCtx,OrderDetailActivity.class);
            intent.putExtra(Constant.ORDER,entity);
            intent.putExtra(Constant.TYPE,type);
            mCtx.startActivity(intent);
        }

        private void initGoodsList(ListView listView, List<OrderBean.OrderItem> itemList) {
            OrderGoodsAdapter adapter =new OrderGoodsAdapter(itemList,mCtx);
            listView.setAdapter(adapter);
        }
    }
}
