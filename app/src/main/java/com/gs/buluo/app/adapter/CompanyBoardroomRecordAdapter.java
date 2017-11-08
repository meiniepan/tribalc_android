package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ConferenceReservation;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by hjn on 2017/10/31.
 */

public class CompanyBoardroomRecordAdapter extends BaseQuickAdapter<ConferenceReservation,BaseHolder> {
    public CompanyBoardroomRecordAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseHolder helper, ConferenceReservation item) {
        String beginTime = TribeDateUtils.dateFormat6(new Date(item.conferenceBeginTime));
        String endTime = TribeDateUtils.dateFormat6(new Date(item.conferenceEndTime));
        long duration = item.conferenceEndTime - item.conferenceBeginTime;
        String durationText = (duration * 10 / 3600000) / 10.0 + "";
        helper.setText(R.id.record_item_number, item.id)
                .setText(R.id.record_item_name, item.name)
                .setText(R.id.record_item_fee, "¥" + item.totalFee)
                .setText(R.id.record_item_floor, item.floor + "层")
                .setText(R.id.record_item_date, TribeDateUtils.dateFormat5(new Date(item.conferenceBeginTime)))
                .setText(R.id.record_item_time, beginTime + "-" + endTime + "(" + durationText + "小时)")
                .setText(R.id.record_item_duration, "共计" + durationText + "小时");
    }
}
