package com.gs.buluo.app.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.view.activity.AddAddressActivity;
import com.gs.buluo.app.view.activity.AddressListActivity;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class FoodListAdapter extends  RecyclerView.Adapter<FoodListAdapter.FoodShopHolder>{
    private  List<UserAddressEntity> mDatas;
    private AddressListActivity mCtx;

    public FoodListAdapter(AddressListActivity context, List<UserAddressEntity> datas) {
        mCtx=context;
        mDatas = datas;
    }

    @Override
    public FoodShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.food_list_item, parent,false);
        return new FoodShopHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodShopHolder holder, int position) {
        final UserAddressEntity entity = mDatas.get(position);
        holder.name.setText(entity.getName());
        holder.price.setText(entity.getArea()+entity.getDetailAddress());


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(List<UserAddressEntity> datas) {
        this.mDatas = datas;
    }

    class FoodShopHolder extends RecyclerView.ViewHolder  {
         TextView name;
         TextView price;
         TextView address;
         ImageView mBook;
         ImageView mRoom;
        public FoodShopHolder(View view) {
            super(view);
            name= (TextView) view.findViewById(R.id.food_shop_name);
            address = (TextView) view.findViewById(R.id.food_shop_address);
            price = (TextView) view.findViewById(R.id.food_price);
            mBook= (ImageView) view.findViewById(R.id.food_book_seat);
            mRoom= (ImageView) view.findViewById(R.id.food_book_room);
        }
    }
}
