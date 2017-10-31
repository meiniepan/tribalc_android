package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2017/10/31.
 */

public class CompanyBoardroomRecordAdapter extends BaseQuickAdapter<String,BaseHolder> {
    public CompanyBoardroomRecordAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, String item) {

    }
}
