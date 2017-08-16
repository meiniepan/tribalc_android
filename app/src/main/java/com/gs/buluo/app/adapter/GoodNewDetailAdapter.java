package com.gs.buluo.app.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/4/17.
 */

public class GoodNewDetailAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> datas;
    boolean noDel;

    public GoodNewDetailAdapter(Context ctx, ArrayList<String> intro) {
        context = ctx;
        datas = intro;
    }

    public GoodNewDetailAdapter(Context ctx, ArrayList<String> intro, boolean noDelete) {
        context = ctx;
        datas = intro;
        noDel = noDelete;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        GoodsNewDetailHolder holder;
        if (convertView == null) {
            holder = new GoodsNewDetailHolder();
            convertView = holder.getConvertView(parent);
        } else {
            holder = (GoodsNewDetailHolder) convertView.getTag();
        }
        String url = datas.get(position);
        int screenWidth = CommonUtils.getScreenWidth(context);
        if (noDel) {
            float scale = Float.parseFloat(url.split("=")[1]);
            convertView.setLayoutParams(new AbsListView.LayoutParams(screenWidth, (int) (screenWidth * scale)));
        }
        FresoUtils.loadImage(url,holder.img);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                notifyDataSetChanged();
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    private class GoodsNewDetailHolder {
        public SimpleDraweeView img;
        public View delete;

        public View getConvertView(ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.add_detail_item, parent, false);
            img = (SimpleDraweeView) view.findViewById(R.id.new_detail_picture);
            delete = view.findViewById(R.id.new_detail_delete);
            if (noDel) delete.setVisibility(View.GONE);
            return view;
        }
    }
}
