package com.gs.buluo.app.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.List;

/**
 * Created by fs on 2016/12/15.
 */

public class PropertyFixListAdapter extends RecyclerAdapter<ListPropertyManagement> {

    Context mContext;
    private PropertyFixHolder mPropertyFixHolder;

    public PropertyFixListAdapter(Context context) {
        super(context);
        mContext = context;
    }


    @Override
    public BaseViewHolder<ListPropertyManagement> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        mPropertyFixHolder=new PropertyFixHolder(parent,R.layout.item_property_fix);
        return mPropertyFixHolder;
    }


    class PropertyFixHolder extends BaseViewHolder<ListPropertyManagement> {
        public TextView companyName;
        public TextView communityName;
        public TextView applyPersonName;
        public TextView floor;
        public TextView appointTime;
        public TextView masterPersonName;
        public TextView phone;
        public TextView doorTime;
        public ViewGroup chooseArea;

        public PropertyFixHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        @Override
        public void onInitializeView() {
            companyName = findViewById(R.id.item_property_fix_companyName);
            communityName = findViewById(R.id.item_property_fix_communityName);
            applyPersonName = findViewById(R.id.item_property_fix_applyPersonName);
            floor = findViewById(R.id.item_property_fix_floor);
            appointTime = findViewById(R.id.item_property_fix_appointTime);
            masterPersonName = findViewById(R.id.item_property_fix_masterPersonName);
            phone = findViewById(R.id.item_property_fix_phone);
            doorTime = findViewById(R.id.item_property_fix_doorTime);
            chooseArea = findViewById(R.id.item_property_fix_chooseArea);
        }

        @Override
        public void setData(ListPropertyManagement entity) {
            companyName.setText(entity.companyName);
            communityName.setText(entity.communityName);
            applyPersonName.setText(entity.applyPersonName);
            floor.setText(entity.floor);
            appointTime.setText(String.valueOf(entity.appointTime));
            if (TextUtils.isEmpty(entity.masterPersonName)){
                chooseArea.setVisibility(View.GONE);
            }else {

            }
        }


        @Override
        public void onItemViewClick(ListPropertyManagement entity) {
            ToastUtils.ToastMessage(mContext,entity.companyName);
        }
    }
}
