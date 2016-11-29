package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.activity.GoodsDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class GoodsListAdapter extends RecyclerAdapter<ListGoods> {
    private List<ListGoods> mDatas;
    Context mCtx;

    public GoodsListAdapter(Context context) {
        super(context);
        mCtx=context;
    }

    @Override
    public BaseViewHolder<ListGoods> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new GoodsHolder(parent);
    }

    class GoodsHolder extends BaseViewHolder<ListGoods> {
        SimpleDraweeView picture;
        TextView name;
        TextView price;
        TextView brand;

        public GoodsHolder(ViewGroup parent) {
            super(parent, R.layout.good_list_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            name = findViewById(R.id.goods_list_name);
            brand = findViewById(R.id.goods_list_brand);
            price = findViewById(R.id.goods_list_price);
            picture = findViewById(R.id.goods_list_picture);
        }

        @Override
        public void setData(ListGoods entity) {
            super.setData(entity);
            name.setText(entity.name);
            price.setText("￥" + entity.salePrice);
            brand.setText(entity.brand);
            FresoUtils.loadImage(entity.mainPicture, picture);
        }

        @Override
        public void onItemViewClick(ListGoods listGoods) {
            super.onItemViewClick(listGoods);
            Intent intent = new Intent(mCtx, GoodsDetailActivity.class);
            intent.putExtra(Constant.GOODS_ID, listGoods.id);
            mCtx.startActivity(intent);
        }
    }
}
