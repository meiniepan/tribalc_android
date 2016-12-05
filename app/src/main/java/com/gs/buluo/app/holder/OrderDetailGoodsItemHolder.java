package com.gs.buluo.app.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;

/**
 * Created by hjn on 2016/11/28.
 */
public class OrderDetailGoodsItemHolder {
    Context mCtx;

    public OrderDetailGoodsItemHolder(Context context) {
        mCtx = context;
    }

    public TextView name;
    public TextView money;
    public TextView number;
    public TextView color;
    public TextView size;
    public SimpleDraweeView picture;
    public TextView colorKey;
    public TextView sizeKey;

    public View getHolderView() {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.order_detail_goods_item, null);
        name = (TextView) view.findViewById(R.id.order_detail_detail_item_goods_name);
        money = (TextView) view.findViewById(R.id.order_detail_item_goods_money);
        number = (TextView) view.findViewById(R.id.order_detail_item_goods_number);
        size = (TextView) view.findViewById(R.id.order_detail_item_goods_size);
        color = (TextView) view.findViewById(R.id.order_detail_item_goods_color);
        sizeKey = (TextView) view.findViewById(R.id.order_detail_item_goods_size_key);
        colorKey = (TextView) view.findViewById(R.id.order_detail_item_goods_color_key);
        picture = (SimpleDraweeView) view.findViewById(R.id.order_detail_item_goods_head);
        return view;
    }
}