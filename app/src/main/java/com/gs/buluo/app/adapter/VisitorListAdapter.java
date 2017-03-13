package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.VisitorActiveBean;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListAdapter extends BaseExpandableListAdapter {
    Context mCtx;
    ArrayList<VisitorActiveBean> list;
    public VisitorListAdapter(Context ctx, ArrayList<VisitorActiveBean> list) {
        this.mCtx =ctx;
        this.list=list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).keys.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).keys.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.visitor_list_item,parent,false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.item_door_name);
        ImageView sign = (ImageView) convertView.findViewById(R.id.visitor_arrow);
        ImageView icon = (ImageView) convertView.findViewById(R.id.visitor_icon);
        if (isExpanded){
            sign.setImageResource(R.mipmap.indicator_up);
            icon.setImageResource(R.mipmap.visitor_head_active);
        }else {
            sign.setImageResource(R.mipmap.indicator_down);
            icon.setImageResource(R.mipmap.visitor_head);
        }

        final VisitorActiveBean visitorBean = list.get(groupPosition);
        tv.setText(visitorBean.name);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.visitor_child_view,parent,false);
        }
        TextView doorName = (TextView) convertView.findViewById(R.id.visitor_child_name);
        final VisitorActiveBean activeBean = list.get(groupPosition);
        final LockKey lockKey = activeBean.keys.get(childPosition);
        doorName.setText(lockKey.equipName);
        convertView.findViewById(R.id.visitor_child_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeBean.keys.remove(lockKey);
                if (activeBean.keys.size()==0){
                    list.remove(activeBean);
                }
                notifyDataSetChanged();
                deleteDoor(activeBean,lockKey);
            }
        });
        return convertView;
    }

    private void deleteDoor(final VisitorActiveBean bean, final LockKey lockKey) {
        TribeRetrofit.getInstance().createApi(DoorApis.class).deleteEquip(TribeApplication.getInstance().getUserInfo().getId(),lockKey.id).enqueue(new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                bean.keys.remove(lockKey);
                if (bean.keys.size()==0){
                    list.remove(bean);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                ToastUtils.ToastMessage(mCtx,R.string.connect_fail);
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
