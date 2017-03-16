package com.gs.buluo.app.adapter;

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
import com.gs.buluo.app.view.activity.VisitorListActivity;
import com.gs.buluo.app.view.widget.LoadingDialog;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListAdapter extends BaseExpandableListAdapter {
    VisitorListActivity mAct;
    ArrayList<VisitorActiveBean> list;
    public VisitorListAdapter(VisitorListActivity ctx, ArrayList<VisitorActiveBean> list) {
        this.mAct =ctx;
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
            convertView = LayoutInflater.from(mAct).inflate(R.layout.visitor_list_item,parent,false);
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
            convertView = LayoutInflater.from(mAct).inflate(R.layout.visitor_child_view,parent,false);
        }
        TextView doorName = (TextView) convertView.findViewById(R.id.visitor_child_name);
        final VisitorActiveBean activeBean = list.get(groupPosition);
        final LockKey lockKey = activeBean.keys.get(childPosition);
        doorName.setText(lockKey.equipName);
        convertView.findViewById(R.id.visitor_child_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.getInstance().show(mAct,R.string.loading,false);
                deleteDoor(activeBean,lockKey);
            }
        });
        return convertView;
    }

    private void deleteDoor(final VisitorActiveBean bean, final LockKey lockKey) {
        TribeRetrofit.getInstance().createApi(DoorApis.class).deleteEquip(lockKey.id,TribeApplication.getInstance().getUserInfo().getId()).enqueue(new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseResponse<CodeResponse>> response) {
                LoadingDialog.getInstance().dismissDialog();
                bean.keys.remove(lockKey);
                if (bean.keys.size()==0){
                    list.remove(bean);
                    mAct.showEmpty();
                }
                notifyDataSetChanged();
            }

            @Override
            public void onFail(int responseCode, BaseResponse<CodeResponse> body) {
                LoadingDialog.getInstance().dismissDialog();
                ToastUtils.ToastMessage(mAct,R.string.connect_fail);
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
