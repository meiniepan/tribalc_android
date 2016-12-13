package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.utils.CommonUtils;

import java.util.List;

/**
 * Created by hjn on 2016/12/7.
 */
public class NewOrderAdapter extends BaseAdapter {
    private Context context;
    private List<ShoppingCart> cartList;
    public NewOrderAdapter(Context context, List<ShoppingCart> list){
        this.context=context;
        cartList=list;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShoppingCart cart = cartList.get(position);
        GroupHolder groupHolder;
        if (convertView==null){
            groupHolder=new GroupHolder();
            convertView=groupHolder.getConvertView();
        }else {
            groupHolder= (GroupHolder) convertView.getTag();
        }
        groupHolder.name.setText(cart.store.name);
        float price = 0;
        for (CartItem item:cart.goodsList){
            price+= Float.parseFloat(item.goods.salePrice)*100 *item.amount;
        }

        groupHolder.price.setText(price/100+"");
        NewOrderChildAdapter adapter = new NewOrderChildAdapter(context,cart.goodsList);
        groupHolder.listView.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(groupHolder.listView);

        convertView.setTag(groupHolder);
        return convertView;
    }

    public class GroupHolder{
        public ListView listView;
        public TextView name;
        public TextView price;

        public View getConvertView(){
            View view=View.inflate(context,R.layout.new_order_list_item,null);
            listView = (ListView) view.findViewById(R.id.new_order_child_list);
            name= (TextView) view.findViewById(R.id.new_order_item_name);
            price= (TextView) view.findViewById(R.id.new_order_price_total);
            return view;
        }
    }
}
