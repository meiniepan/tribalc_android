package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListStore;
import com.gs.buluo.app.utils.FresoUtils;

import java.util.List;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityDetailStoreAdapter extends BaseAdapter{
    Context mCtx;
    List<ListStore> list;
    public CommunityDetailStoreAdapter(Context context, List list){
        mCtx=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ListStore store=list.get(position);
        CommunityDetailStoreHolder holder=null;
        if (convertView == null) {
            holder=new CommunityDetailStoreHolder();
            convertView = holder.getHolderView();
        }else {
            holder = (CommunityDetailStoreHolder) convertView.getTag();
        }
        holder.distance.setText(store.discount+"km");
//        holder.money.setText(store.);
        holder.name.setText(store.name);
        holder.category.setText(store.brand);
        FresoUtils.loadImage(store.mainPicture,holder.picture);

        convertView.setTag(holder);
        return convertView;
    }

    public class CommunityDetailStoreHolder{
        public TextView name;
        public TextView category;
        public TextView money;
        public TextView distance;
        public SimpleDraweeView picture;

        public View getHolderView() {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.community_detail_store_item, null);
            category = (TextView) view.findViewById(R.id.community_detail_store_category);
//            money = (TextView) view.findViewById(R.id.community_detail_store_price);
            distance = (TextView) view.findViewById(R.id.community_detail_store_distance);
            picture = (SimpleDraweeView) view.findViewById(R.id.community_detail_store_picture);
            name = (TextView) view.findViewById(R.id.community_detail_store_name);
            return view;
        }
    }
}
