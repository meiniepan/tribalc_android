package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BoardroomReserveTimeEntity;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class ReserveDateAdapter extends BaseQuickAdapter<BoardroomReserveTimeEntity,BaseHolder> {
    public ReserveDateAdapter(int layoutResId, @Nullable List<BoardroomReserveTimeEntity> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseHolder helper, final BoardroomReserveTimeEntity item) {
        helper.setText(R.id.tv, item.date);
        View view = helper.getView(R.id.icon);
        if (item.checked) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);
    }

}
