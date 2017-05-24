package com.gs.buluo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.activity.VisitorListActivity;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListAdapter extends BaseAdapter {
    VisitorListActivity mAct;
    ArrayList<LockKey> list;

    public VisitorListAdapter(VisitorListActivity ctx, ArrayList<LockKey> list) {
        this.mAct = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView ==null){
            convertView = LayoutInflater.from(mAct).inflate(R.layout.visitor_list_item, parent, false);
        }
        final LockKey key = list.get(position);
        TextView tv = (TextView) convertView.findViewById(R.id.item_door_name);
        ImageView del = (ImageView) convertView.findViewById(R.id.visitor_delete);
        tv.setText(key.name);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.getInstance().show(mAct,R.string.loading,true);
                deleteDoor(key);
            }
        });
        return convertView;
    }

    private void deleteDoor(final LockKey lockKey) {
        TribeRetrofit.getInstance().createApi(DoorApis.class).deleteEquip(lockKey.id, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        LoadingDialog.getInstance().dismissDialog();
                        list.remove(lockKey);
                        notifyDataSetChanged();
                        if (list.size()==0){
                            mAct.showEmpty();
                        }
                    }
                });
    }
}
