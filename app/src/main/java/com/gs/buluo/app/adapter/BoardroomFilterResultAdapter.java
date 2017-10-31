package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import org.xutils.x;

import java.util.List;

/**
 * Created by hjn on 2017/10/19.
 */

public class BoardroomFilterResultAdapter extends BaseQuickAdapter<ConferenceRoom, BaseHolder> {
    public BoardroomFilterResultAdapter(int layoutResId, @Nullable List<ConferenceRoom> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ConferenceRoom item) {
        helper.setText(R.id.room_item_name, item.name)
                .setText(R.id.room_item_floor, item.floor + "层")
                .setText(R.id.room_item_people, item.galleryful + "-" + item.maxGalleryful)
                .setText(R.id.room_item_fee, item.fee + "元/人");
        if (!TextUtils.isEmpty(item.picture))
            x.image().bind((ImageView) helper.getView(R.id.item_room_picture), FresoUtils.transformUrl(item.picture));
        if (item.equipments.size()>0) {
            TextView textView1 = helper.getView(R.id.room_item_equip0);
            textView1.setBackgroundResource(R.drawable.text_background);
            textView1.setText(item.equipments.get(0).name);
        }
        if (item.equipments.size()>1) {
            TextView textView1 = helper.getView(R.id.room_item_equip1);
            textView1.setBackgroundResource(R.drawable.text_background);
            textView1.setText(item.equipments.get(1).name);
        }
        if (item.equipments.size()>2) {
            TextView textView1 = helper.getView(R.id.room_item_equip2);
            textView1.setBackgroundResource(R.drawable.text_background);
            textView1.setText(item.equipments.get(2).name);
        }
        if (item.equipments.size()>3) {
            TextView textView1 = helper.getView(R.id.room_item_equip3);
            textView1.setBackgroundResource(R.drawable.text_background);
            textView1.setText(item.equipments.get(3).name);
        }
        if (item.equipments.size()>4) {
            TextView textView1 = helper.getView(R.id.room_item_equip4);
            textView1.setBackgroundResource(R.drawable.text_background);
            textView1.setText(item.equipments.get(4).name);
        }
        if (item.equipments.size() > 5) {
            helper.getView(R.id.room_item_more).setVisibility(View.VISIBLE);
        }
    }
}
