package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListStoreSetMeal;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.activity.ServeDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServeListAdapter extends RecyclerAdapter<ListStoreSetMeal> {
    Context mCtx;
    private ServeItemHolder serveItemHolder;
    private boolean isFilter;

    public ServeListAdapter(Context context) {
        super(context);
        mCtx=context;
    }

    @Override
    public BaseViewHolder<ListStoreSetMeal> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        serveItemHolder = new ServeItemHolder(parent);
        return serveItemHolder;
    }

    class ServeItemHolder extends BaseViewHolder<ListStoreSetMeal>{
        TextView tags;
        TextView name;
        TextView money;
        SimpleDraweeView picture;
        ImageView seat;
        ImageView room;
        View line;
        public ServeItemHolder(ViewGroup itemView) {
            super(itemView, R.layout.serve_list_item);
        }

        @Override
        public void onInitializeView() {
            tags =findViewById(R.id.serve_list_tags);
            name=findViewById(R.id.serve_shop_name);
            picture=findViewById(R.id.serve_list_head);
            money=findViewById(R.id.serve_price);
            line=findViewById(R.id.serve_line);
//            room_select=findViewById(R.id.serve_book_room);
//            seat=findViewById(R.id.serve_book_seat);
        }

        @Override
        public void setData(ListStoreSetMeal entity) {
            super.setData(entity);
            if (entity==null||entity.store==null)return;
            name.setText(entity.name);
            money.setText(entity.personExpense);
            tags.setText(entity.store.markPlace);
            if (entity.tags!=null&&entity.tags.size()>0){
                tags.setText(entity.store.markPlace+" | "+entity.tags.get(0));
            }
            FresoUtils.loadImage(entity.mainPicture,picture);
            if (isFilter){
                picture.setColorFilter(0x70000000);
                line.setBackgroundColor(0x4000000);
            }else {
                picture.setColorFilter(0x00000000);
                line.setBackgroundColor(0xffdddddd);
            }
        }

        @Override
        public void onItemViewClick(ListStoreSetMeal entity) {
            Intent intent=new Intent(mCtx,ServeDetailActivity.class);
            intent.putExtra(Constant.SERVE_ID,entity.id);
            mCtx.startActivity(intent);
        }
    }
}
