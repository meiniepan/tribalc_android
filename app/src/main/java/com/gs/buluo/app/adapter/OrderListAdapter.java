package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.utils.FresoUtils;
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
            mCtx.startActivity(intent);
        }

        private void initGoodsList(ListView listView, List<OrderBean.OrderItem> itemList) {
            OrderGoodsAdapter adapter =new OrderGoodsAdapter(itemList);
            listView.setAdapter(adapter);
        }
    }

    class OrderGoodsAdapter extends BaseAdapter{
        private final List<OrderBean.OrderItem> itemList1;

        public OrderGoodsAdapter(List<OrderBean.OrderItem> itemList) {
            itemList1 = itemList;
        }

        @Override
        public int getCount() {
            return itemList1.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListGoods goods = itemList1.get(position).goods;
            OrderGoodsItemHolder holder=null;
            if (convertView == null) {
                holder=new OrderGoodsItemHolder();
                convertView = holder.getHolderView();
            }else {
                holder = (OrderGoodsItemHolder) convertView.getTag();
            }
            holder.name.setText(goods.name);
            holder.brand.setText(goods.brand);
            holder.money.setText("¥ "+goods.salePrice);
            holder.number.setText("x "+itemList1.get(position).amount);
            if (goods.standardSnapshot!=null){
                String[] arr1 = goods.standardSnapshot.split("\\|");
                if (arr1.length>1){
                    holder.colorKey.setText(arr1[0].split(":")[0]);
                    holder.color.setText(arr1[0].split(":")[1]);
                    holder.sizeKey.setText(arr1[1].split(":")[0]);
                    holder.size.setText(arr1[1].split(":")[1]);
                    FresoUtils.loadImage(Constant.BASE_IMG_URL+goods.mainPicture,holder.picture);
                }else {
                    holder.colorKey.setText(goods.standardSnapshot.split(":")[0]);
                    holder.color.setText(goods.standardSnapshot.split(":")[1]);
                }
            }
            convertView.setTag(holder);
            return convertView;
        }
    }


    private class OrderGoodsItemHolder {
        public TextView name;
        public TextView brand;
        public TextView money;
        public TextView number;
        public TextView color;
        public TextView size;
        public SimpleDraweeView picture;
        private TextView colorKey;
        private TextView sizeKey;

        public View getHolderView() {
            View view= LayoutInflater.from(mCtx).inflate(R.layout.order_goods_item,null);
            name= (TextView) view.findViewById(R.id.order_item_goods_name);
            brand= (TextView) view.findViewById(R.id.order_item_goods_brand);
            size= (TextView) view.findViewById(R.id.order_item_goods_size);
            money= (TextView) view.findViewById(R.id.order_item_goods_money);
            number= (TextView) view.findViewById(R.id.order_item_goods_number);
            color= (TextView) view.findViewById(R.id.order_item_goods_color);
            sizeKey= (TextView) view.findViewById(R.id.order_item_goods_size_key);
            colorKey= (TextView) view.findViewById(R.id.order_item_goods_color_key);
            picture= (SimpleDraweeView) view.findViewById(R.id.order_item_goods_head);
            return view;
        }
    }
}
