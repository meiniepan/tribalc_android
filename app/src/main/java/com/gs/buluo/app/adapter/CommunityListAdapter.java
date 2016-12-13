package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CommunityPlate;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.activity.CommunityDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class CommunityListAdapter extends RecyclerAdapter<CommunityPlate> {
    private List<CommunityPlate> mDatas;
    Context mCtx;

    public CommunityListAdapter(Context context) {
        super(context);
        mCtx=context;
    }

    @Override
    public BaseViewHolder<CommunityPlate> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new CommunityHolder(parent);
    }

    class CommunityHolder extends BaseViewHolder<CommunityPlate> {
        SimpleDraweeView picture;
        TextView name;
        TextView address;
        TextView phone;

        public CommunityHolder(ViewGroup parent) {
            super(parent, R.layout.community_list_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            name = findViewById(R.id.community_item_name);
            address = findViewById(R.id.community_item_address);
            phone = findViewById(R.id.community_item_phone);
            picture = findViewById(R.id.community_item_picture);
        }

        @Override
        public void setData(CommunityPlate entity) {
            super.setData(entity);
            name.setText(entity.name);
            address.setText(entity.address);
            phone.setText(entity.phone);
            FresoUtils.loadImage(entity.mainPicture, picture);
        }

        @Override
        public void onItemViewClick(CommunityPlate listGoods) {
            super.onItemViewClick(listGoods);
            Intent intent = new Intent(mCtx, CommunityDetailActivity.class);
            intent.putExtra(Constant.COMMUNITY_ID, listGoods.id);
            mCtx.startActivity(intent);
        }
    }
}
