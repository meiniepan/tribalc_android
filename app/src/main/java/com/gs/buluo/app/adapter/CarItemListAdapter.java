package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ShoppingCart;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/12/2.
 */
public class CarItemListAdapter extends BaseAdapter{
    List<ShoppingCart.ListGoodsList> list ;
    private Context context;
    private CarItemGoodsHolder holder;
    private boolean showEidt;

    public CarItemListAdapter(Context context, List list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
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
        ShoppingCart.ListGoodsList goodsList = list.get(position);
        if (convertView == null) {
            holder = new CarItemGoodsHolder();
            convertView = holder.getConvertView();
        }else {
            holder= (CarItemGoodsHolder) convertView.getTag();
        }
        if (showEidt){
            holder.hideView1.setVisibility(View.VISIBLE);
            holder.hideView2.setVisibility(View.VISIBLE);
            holder.hideView3.setVisibility(View.VISIBLE);
            holder.commonView1.setVisibility(View.GONE);
            holder.commonView2.setVisibility(View.GONE);
        }else {
            holder.hideView1.setVisibility(View.GONE);
            holder.hideView2.setVisibility(View.GONE);
            holder.hideView3.setVisibility(View.GONE);
            holder.commonView1.setVisibility(View.VISIBLE);
            holder.commonView2.setVisibility(View.VISIBLE);
        }

        holder.name.setText(goodsList.goods.name);
        holder.price.setText(goodsList.goods.salePrice);

        convertView.setTag(holder);
        return convertView;
    }

    public void setIsEdit(boolean edit){
        showEidt=edit;
    }

    public class CarItemGoodsHolder{
        public View hideView1;
        public View hideView2;
        public View hideView3;
        public View commonView1;
        public View commonView2;

        public TextView name;
        public TextView price;

        public View getConvertView() {
            View view =  LayoutInflater.from(context).inflate(R.layout.car_item_goods_item, null);
            hideView1=view.findViewById(R.id.car_item_good_hidden_price);
            hideView2=view.findViewById(R.id.car_item_good_arrow);
            hideView3=view.findViewById(R.id.car_item_good_number_board);

            commonView1=view.findViewById(R.id.car_item_good_number);
            commonView2=view.findViewById(R.id.car_item_good_common_price);

            name= (TextView) view.findViewById(R.id.car_item_goods_name);
            price= (TextView) view.findViewById(R.id.car_item_good_price);
            return view;
        }
    }


}
