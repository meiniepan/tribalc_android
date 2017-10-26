package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2017/10/20.
 */

public class BoardroomRecordAdapter extends BaseQuickAdapter<ConferenceRoom, BaseHolder> {
    public BoardroomRecordAdapter(int layoutResId, @Nullable List<ConferenceRoom> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ConferenceRoom item) {
//        helper.setText(R.id.record_item_number,item.id)
//        .setText(R.id.record_item_name,item.name)
//        .setText(R.id.record_item_fee,item.fee)
//        .setText(R.id.record_item_floor,item.floor);
    }
}
