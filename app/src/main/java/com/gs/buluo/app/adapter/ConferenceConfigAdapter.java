package com.gs.buluo.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ConferenceEquipment;

import java.util.List;

/**
 * Created by Solang on 2017/11/21.
 */

public class ConferenceConfigAdapter extends RecyclerView.Adapter<ConferenceConfigAdapter.ViewHolder> {
    private Context context;
    private List<ConferenceEquipment> data;
    public ConferenceConfigAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_conference_config,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTag.setText(data.get(position).name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTag;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_item_conference_config);
        }
    }
}
