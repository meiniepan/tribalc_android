package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.utils.FresoUtils;

import java.util.List;

/**
 * Created by hjn on 2016/12/7.
 */
public class NewOrderChildAdapter extends BaseAdapter {
    private Context context;
    private  List<CartItem> goodsList;
    public NewOrderChildAdapter(Context context, List<CartItem> goodsList) {
        this.context=context;
        this.goodsList=goodsList;

    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartItem item = goodsList.get(position);
        ChildHolder holder;
        if (convertView==null){
            holder=new ChildHolder();
            convertView=holder.getConvertView();
        }else {
            holder= (ChildHolder) convertView.getTag();
        }
        holder.name.setText(item.goods.name);
        holder.count.setText(item.amount+"");
        holder.price.setText(Float.parseFloat(item.goods.salePrice)+"");
        FresoUtils.loadImage(item.goods.mainPicture,holder.picture);

        if (item.goods.standardSnapshot!=null){
            String[] arr1 = item.goods.standardSnapshot.split("\\|");
            if (arr1.length>1){
                holder.key1.setText(arr1[0].split(":")[0]);
                holder.value1.setText(arr1[0].split(":")[1]);
                holder.key2.setText(arr1[1].split(":")[0]);
                holder.value2.setText(arr1[1].split(":")[1]);
                FresoUtils.loadImage(item.goods.mainPicture,holder.picture);
            }else {
                holder.key1.setText(item.goods.standardSnapshot.split(":")[0]);
                holder.value1.setText(item.goods.standardSnapshot.split(":")[1]);
            }
        }

        convertView.setTag(holder);
        return convertView;
    }


    public class ChildHolder{
        public TextView name;
        public TextView key1;
        public TextView value1;
        public TextView key2;
        public TextView value2;
        public TextView price;
        public TextView count;
        public SimpleDraweeView picture;

        public View getConvertView(){
            View view=View.inflate(context, R.layout.new_order_item_goods_item,null);
            name = (TextView) view.findViewById(R.id.new_order_goods_name);
            key1= (TextView) view.findViewById(R.id.new_order_item_key1);
            key2= (TextView) view.findViewById(R.id.new_order_item_key2);
            value1= (TextView) view.findViewById(R.id.new_order_item_value1);
            value2= (TextView) view.findViewById(R.id.new_order_item_value2);
            price= (TextView) view.findViewById(R.id.new_order_item_price);
            count= (TextView) view.findViewById(R.id.new_order_item_good_count);
            picture = (SimpleDraweeView) view.findViewById(R.id.new_order_goods_picture);
            return view;
        }
    }
}
