package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;

import com.gs.buluo.app.bean.ConferenceRoom;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2017/10/19.
 */

public class BoardroomFilterResultAdapter extends BaseQuickAdapter<ConferenceRoom,BaseHolder> {
    public BoardroomFilterResultAdapter(int layoutResId, @Nullable List<ConferenceRoom> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, ConferenceRoom item) {

    }
}
