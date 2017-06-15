package com.gs.buluo.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.FresoUtils;

import java.util.List;

/**
 * Created by hjn on 2017/5/24.
 */

public class HorPictureAdapter extends RecyclerView.Adapter<HorPictureAdapter.PictureHolder> {
    Context mCtx;
    List<String> list;

    public HorPictureAdapter(Context context, List<String> list) {
        mCtx = context;
        this.list = list;
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.pic_item, parent, false);
        return new PictureHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        FresoUtils.loadImage(list.get(position), holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PictureHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView image;

        public PictureHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.proper_img);
        }
    }
}
