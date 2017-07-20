package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.Privilege;

import java.util.List;

/**
 * Created by hjn on 2017/7/20.
 */

public class DiscountIntroAdapter extends BaseAdapter {
    Context mCtx;
    List<Privilege> datas;

    public DiscountIntroAdapter(Context context, List<Privilege> data) {
        mCtx = context;
        datas = data;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.discount_intro_item, parent, false);
        }
        Privilege privilege = datas.get(position);
        TextView tvTime = (TextView) convertView.findViewById(R.id.discount_info_time);
        TextView tvValue = (TextView) convertView.findViewById(R.id.discount_info_value);

        String value = "";
        switch (privilege.type) {
            case DISCOUNT:
                value = (privilege.value.floatValue() * 10) + "折";
                break;
            case REDUCE:
                value = "满" + privilege.condition + "减" + privilege.value + "元";
                break;
            case ALIQUOT:
                value = "每满" + privilege.condition + "减" + privilege.value + "元";
                break;
        }
        tvValue.setText(value);

        if (privilege.activityTime != null) {
            String begin = add0ToClock(privilege.activityTime.get(0) / 3600, privilege.activityTime.get(0) % 3600 / 60);
            String last = add0ToClock(privilege.activityTime.get(1) / 3600, privilege.activityTime.get(1) % 3600 / 60);

            if (privilege.activityTime.get(0) < privilege.activityTime.get(1)) {
                tvTime.setText("(使用时间:每天" + begin + "-" + last + ")");
            } else {
                tvTime.setText("(使用时间:每天" + begin + "-次日" + last + ")");
            }
        }
        return convertView;
    }

    private String add0ToClock(int startTime1, int endTime1) {
        String s1 = "";
        String e1 = "";
        if (startTime1 < 10) {
            s1 = "0" + startTime1 + "";
        } else {
            s1 = startTime1 + "";
        }
        if (endTime1 < 10) {
            e1 = "0" + endTime1 + "";
        } else {
            e1 = endTime1 + "";
        }
        return s1 + ":" + e1;
    }
}
