package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListPropertyManagement;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.activity.PropertyFixDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.Date;
import java.util.List;

/**
 * Created by fs on 2016/12/15.
 */

public class PropertyFixListAdapter extends RecyclerAdapter<ListPropertyManagement> {

    Context mContext;
    private PropertyFixHolder mPropertyFixHolder;
    private static final String TAG = "PropertyFixListAdapter";

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
        public TextView status;
        public ImageView fixDone;

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
            status=findViewById(R.id.item_property_fix_status);
            fixDone=findViewById(R.id.fix_done);
        }

        @Override
        public void setData(ListPropertyManagement entity) {
            super.setData(entity);
            switch (entity.status){
                case "ORDER_ACCEPT":
                    status.setText("系统接单");
                    chooseArea.setVisibility(View.GONE);
                    break;
                case "TASK_CONFIRM":
                    status.setText("任务确认");
                    break;
                case "NOT_PAYING":
                    status.setText("代付款");
                    break;
                case "PAYED":
                    status.setText("已完成");
                    fixDone.setVisibility(View.VISIBLE);
                    status.setVisibility(View.INVISIBLE);
                    break;
            }
            companyName.setText(entity.companyName);
            communityName.setText(entity.communityName);
            applyPersonName.setText(entity.applyPersonName);
            floor.setText(entity.floor);
            String time = TribeDateUtils.dateFormat7(new Date(entity.appointTime));
            appointTime.setText(time);

        }


        @Override
        public void onItemViewClick(ListPropertyManagement entity) {
            super.onItemViewClick(entity);
            Intent intent = new Intent(mContext, PropertyFixDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.PROPERTY_MANAGEMENT,entity);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}
