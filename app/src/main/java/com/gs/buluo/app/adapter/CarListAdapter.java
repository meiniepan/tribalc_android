package com.gs.buluo.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private List<ShoppingCart.ListGoodsList> list;

    public CarListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder<ShoppingCart> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return  new CarListHolder(parent);
    }

    class CarListHolder extends BaseViewHolder<ShoppingCart> {
        TextView shopName;
        ListView listView;
        CheckBox select;

        public CarListHolder(ViewGroup itemView) {
            super(itemView, R.layout.car_list_item);
        }

        @Override
        public void onInitializeView() {
            shopName = findViewById(R.id.car_item_name);
            listView = findViewById(R.id.car_item_list);
            select = findViewById(R.id.car_item_select);
        }

        @Override
        public void setData(final ShoppingCart entity) {
            super.setData(entity);
            shopName.setText(entity.tMarkStore.name);
            list = entity.tListGoodsList;
            adapter = new CarItemListAdapter(context, list);
            int height = DensityUtils.dip2px(context, 140);
            listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (list.size()) * height));
            listView.setAdapter(adapter);
            select.setChecked(getData().get(getAdapterPosition()).isSelected);

            select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (ShoppingCart.ListGoodsList listGoodsList : list) {
                        listGoodsList.isSelected = isChecked;
                    }
                    adapter.notifyDataSetChanged();
                    entity.isSelected=isChecked;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(getAdapterPosition());
                        }
                    });
                }
            });
        }
    }
}
