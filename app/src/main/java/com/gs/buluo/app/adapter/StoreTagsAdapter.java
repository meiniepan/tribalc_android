package com.gs.buluo.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.app.R;

import java.util.List;

/**
 * Created by Solang on 2017/7/19.
 */

public class StoreTagsAdapter extends RecyclerView.Adapter<StoreTagsAdapter.ViewHolder> {
    private Context context;
    private List<String> data;
    public StoreTagsAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_tag,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.tvTag.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return 12;
//        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTag;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextView) itemView.findViewById(R.id.tv_item_store_tag);
        }
    }
}
