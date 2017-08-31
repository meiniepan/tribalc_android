package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.activity.GoodsDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import org.xutils.x;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class GoodsListAdapter extends BaseQuickAdapter<ListGoods, BaseHolder> {
    public GoodsListAdapter(@LayoutRes int layoutResId, @Nullable List<ListGoods> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseHolder helper, ListGoods item) {
        helper.setText(R.id.goods_list_name, item.name)
                .setText(R.id.goods_list_price, "￥" + item.salePrice)
                .setText(R.id.goods_list_brand, item.brand);
        ImageView picture = helper.getView(R.id.goods_list_picture);
        x.image().bind(picture, FrescoImageLoader.formatImageUrl(item.mainPicture));
    }
}
