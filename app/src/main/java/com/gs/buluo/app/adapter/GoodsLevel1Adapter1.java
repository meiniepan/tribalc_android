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
 * Created by hjn on 2016/11/18.
 */
public class GoodsLevel1Adapter1 extends RecyclerView.Adapter<GoodsLevel1Adapter1.Level1Holder>{

    private final List<String> mDatas;
    private final Context mCtx;
    private int nowPos =0;
    private OnLevelClickListener onLevelClickListener;

    public GoodsLevel1Adapter1(Context context, List datas){
        mCtx = context;
        mDatas = datas;
    }

    @Override
    public Level1Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.goods_level_item, parent,false);
        return new Level1Holder(view);
    }

    @Override
    public void onBindViewHolder(final Level1Holder holder, final int position) {
        holder.text.setText(mDatas.get(position));
        if (position==nowPos){
            holder.text.setBackgroundResource(R.drawable.board_choose);
            holder.text.setTextColor(0xff51c7d1);
        }else {
            holder.text.setBackgroundResource(R.drawable.board_un_choose);
            holder.text.setTextColor(0xffb2b2b2);
        }
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowPos =position;
                notifyDataSetChanged();
                onLevelClickListener.onClick(mDatas.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class Level1Holder extends RecyclerView.ViewHolder {
        public TextView text;
        public Level1Holder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.goods_level_item_text);
        }
    }

    public interface OnLevelClickListener {
        void onClick(String s);
    }

    public void setOnLevelClickListener(OnLevelClickListener onLevelClickListener){
        this.onLevelClickListener=onLevelClickListener;
    }
}
