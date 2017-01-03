package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.holder.OrderDetailGoodsItemHolder;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.widget.panel.GoodsChoosePanel;

import java.util.List;

/**
 * Created by hjn on 2016/11/28.
 */
public class OrderDetailGoodsAdapter extends BaseAdapter {
    private final List<CartItem> itemList1;
    private Context mCtx;
    private GoodsChoosePanel panel;

    public OrderDetailGoodsAdapter(List<CartItem> itemList, Context context) {
        mCtx = context;
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

        OrderDetailGoodsItemHolder holder = null;
        if (convertView == null) {
            holder = new OrderDetailGoodsItemHolder(mCtx);
            convertView = holder.getHolderView();
        } else {
            holder = (OrderDetailGoodsItemHolder) convertView.getTag();
        }
        holder.name.setText(goods.name);
        holder.money.setText("¥ " + goods.salePrice);
        holder.number.setText("x" + itemList1.get(position).amount);
        if (goods.standardSnapshot != null) {
            String[] arr1 = goods.standardSnapshot.split("\\|");
            if (arr1.length > 1) {
                holder.colorKey.setText(arr1[0].split(":")[0] + " : ");
                holder.color.setText(arr1[0].split(":")[1]);
                holder.sizeKey.setText(arr1[1].split(":")[0] + " : ");
                holder.size.setText(arr1[1].split(":")[1]);
                FresoUtils.loadImage(goods.mainPicture, holder.picture);
            } else {
                holder.colorKey.setText(goods.standardSnapshot.split(":")[0] + " : ");
                holder.color.setText(goods.standardSnapshot.split(":")[1]);
            }
        }
    FresoUtils.loadImage(goods.mainPicture,holder.picture);

    convertView.setTag(holder);
    return convertView;
}
}
