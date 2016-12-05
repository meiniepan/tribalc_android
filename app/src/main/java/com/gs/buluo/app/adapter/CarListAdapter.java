package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/12/2.
 */
public class CarListAdapter extends RecyclerAdapter<ShoppingCart> {
    private CarItemListAdapter adapter;
    private Context context;
    private boolean showEdit;
    public CarListHolder carListHolder;

    public CarListAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public BaseViewHolder<ShoppingCart> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        carListHolder = new CarListHolder(parent);
        carListHolder.getAdapterPosition();
        return carListHolder;
    }

    public void showEditView() {
        showEdit=true;
        adapter.setIsEdit(showEdit);
//        adapter.notifyDataSetChanged();
    }

    public void finishEdit(){
        showEdit=false;
        adapter.setIsEdit(showEdit);
//        adapter.notifyDataSetChanged();
    }

    class CarListHolder extends BaseViewHolder<ShoppingCart>{
        TextView shopName;
        ListView listView;
        ImageView select;

        public CarListHolder(ViewGroup itemView) {
            super(itemView, R.layout.car_list_item);
        }

        @Override
        public void onInitializeView() {
            shopName=findViewById(R.id.car_item_name);
            listView=findViewById(R.id.car_item_list);
            select=findViewById(R.id.car_item_select);
        }

        @Override
        public void setData(ShoppingCart entity) {
            super.setData(entity);
//            shopName.setText(entity.tMarkStore.name);
            List list=entity.tListGoodsList;
            adapter = new CarItemListAdapter(context, list);
            int height = DensityUtils.dip2px(context,140);
            listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(list.size())* height));
            listView.setAdapter(adapter);
        }
    }
}
