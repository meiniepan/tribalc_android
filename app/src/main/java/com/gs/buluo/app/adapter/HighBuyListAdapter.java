package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.StoreInfo;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.activity.StoreInfoActivity;

import java.util.ArrayList;

/**
 * Created by Solang on 2017/7/3.
 */

public class HighBuyListAdapter extends RecyclerView.Adapter {
    private Context mCtx;
    private ArrayList<StoreInfo> datas;

    public HighBuyListAdapter(Context context, ArrayList<StoreInfo> datas) {
        mCtx = context;
        this.datas = datas;
    }

    public void setDatas(ArrayList<StoreInfo> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.high_buy_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StoreInfo data = datas.get(position);
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.setItemClick(position);
        if (data.pictures != null && data.pictures.length > 0) {
            FresoUtils.loadImage(data.pictures[0], mHolder.bac);
        }
        FresoUtils.loadImage(data.logo, mHolder.icon);
        mHolder.tvName.setText(data.name);
        mHolder.tvCategory.setText(data.category);
        mHolder.tvPlace.setText(data.markPlace);
        mHolder.tvAverage.setText(data.avgprice);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView bac;
        private SimpleDraweeView icon;
        private TextView tvName;
        private TextView tvCategory;
        private TextView tvPlace;
        private TextView tvAverage;

        public ViewHolder(final View itemView) {
            super(itemView);
            bac = (SimpleDraweeView) itemView.findViewById(R.id.high_buy_bac);
            icon = (SimpleDraweeView) itemView.findViewById(R.id.high_buy_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_buy_list_name);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_buy_list_category);
            tvPlace = (TextView) itemView.findViewById(R.id.tv_buy_list_mark_place);
            tvAverage = (TextView) itemView.findViewById(R.id.tv_buy_list_average);
        }

        public void setItemClick(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, StoreInfoActivity.class);
                    intent.putExtra(Constant.STORE_ID, datas.get(position).id);
                    mCtx.startActivity(intent);
                }
            });
        }
    }
}
