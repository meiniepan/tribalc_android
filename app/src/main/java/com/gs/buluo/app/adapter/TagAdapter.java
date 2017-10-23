package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.TagSelectBean;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2017/10/19.
 */

public class TagAdapter extends BaseQuickAdapter<TagSelectBean, BaseHolder> {
    public TagAdapter(int layoutResId, @Nullable List<TagSelectBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseHolder helper, TagSelectBean item) {
        helper.setText(R.id.tag_content, item.content);
        TextView view = helper.getView(R.id.tag_content);
        if (item.isSelected) {
            view.setBackgroundResource(R.color.custom_color);
            view.setTextColor(0xffffffff);
        } else {
            view.setBackgroundResource(R.drawable.text_background_black);
            view.setTextColor(0xff2a2a2a);
        }
    }
}
