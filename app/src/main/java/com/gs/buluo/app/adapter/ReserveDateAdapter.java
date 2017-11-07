package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ConferenceReserveTimeEntity;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class ReserveDateAdapter extends BaseQuickAdapter<ConferenceReserveTimeEntity, BaseHolder> {
    public ReserveDateAdapter(int layoutResId, @Nullable List<ConferenceReserveTimeEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, final ConferenceReserveTimeEntity item) {
        String date = TribeDateUtils.dateFormat5(new Date(item.date));
        helper.setText(R.id.tv, date);
        View view = helper.getView(R.id.icon);
        if (item.checked) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        int width;
            width = CommonUtils.getScreenWidth(mContext) / 3;
        View view = mLayoutInflater.inflate(layoutResId, parent, false);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
        return view;
    }
}
