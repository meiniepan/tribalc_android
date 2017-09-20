package com.gs.buluo.app.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import org.xutils.x;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class StoreInfoGoodsListAdapter extends BaseQuickAdapter<ListGoods, BaseHolder> {
    public StoreInfoGoodsListAdapter(@LayoutRes int layoutResId, @Nullable List<ListGoods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ListGoods item) {
        List<String> tags = item.tags;
        StringBuffer text = new StringBuffer();
        if (tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                if (i == tags.size() - 1)
                    text.append(tags.get(i));
                else text.append(tags.get(i) + " | ");
            }
        }
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_average, item.salePrice)
                .setText(R.id.tv_tags, text);
        ImageView picture = helper.getView(R.id.goods_list_picture);
        x.image().bind(picture, FrescoImageLoader.formatImageUrl(item.mainPicture));
    }


}
