package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.utils.FresoUtils;

import java.util.ArrayList;

/**
 * Created by Solang on 2017/7/3.
 */

public class HighBuyListAdapter extends BaseAdapter {
    private Context mCtx;
    private ArrayList<StoreInfo> datas;

    public HighBuyListAdapter(Context context ,ArrayList<StoreInfo> datas) {
        mCtx = context;
        this.datas = datas;
    }

    @Override

    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoreInfo data = datas.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.high_buy_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.bac = (SimpleDraweeView) convertView.findViewById(R.id.high_buy_bac);
            holder.icon = (SimpleDraweeView) convertView.findViewById(R.id.high_buy_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_buy_list_name);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_buy_list_category);
            holder.tvPlace = (TextView) convertView.findViewById(R.id.tv_buy_list_mark_place);
            holder.tvAverage = (TextView) convertView.findViewById(R.id.tv_buy_list_average);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if ( data.pictures != null && data.pictures.length > 0) {
            FresoUtils.loadImage(data.pictures[0], holder.bac);
        }
        FresoUtils.loadImage(data.logo, holder.icon);
        holder.tvName.setText(data.name);
        holder.tvCategory.setText(data.category);
        holder.tvPlace.setText(data.markPlace);
        holder.tvAverage.setText(data.avgprice);
        return convertView;
    }

    private class ViewHolder {
        private SimpleDraweeView bac;
        private SimpleDraweeView icon;
        private TextView tvName;
        private TextView tvCategory;
        private TextView tvPlace;
        private TextView tvAverage;
    }
}
