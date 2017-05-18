package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.DoorListAdapter;
import com.gs.buluo.app.bean.LockEquip;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.RequestBodyBean.MultiLockRequest;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/3/9.
 */
public class DoorListActivity extends BaseActivity {
    @Bind(R.id.door_list)
    ListView listView;
    private DoorListAdapter listAdapter;
    private ArrayList<LockEquip> list;

    private int amount;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list = getIntent().getParcelableArrayListExtra(Constant.DOOR_LIST);
        listAdapter = new DoorListAdapter(getCtx(), list);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LockEquip lockEquip = list.get(position);
                if (lockEquip.selected) {
                    lockEquip.selected = false;
                    amount--;
                } else {
                    if (amount == 4) {
                        ToastUtils.ToastMessage(getCtx(), "您最多可选择4个设备");
                        return;
                    }
                    lockEquip.selected = true;
                    amount++;
                }
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.door_list_finish)
    public void open() {
        final ArrayList<String> requestList = new ArrayList<>();
        for (LockEquip equip : list) {
            if (equip.selected) requestList.add(equip.id);
        }
        MultiLockRequest request = new MultiLockRequest();
        request.equipIds = requestList;
        TribeRetrofit.getInstance().createApi(DoorApis.class).getMultiKey(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<LockKey>>() {
                    @Override
                    public void onNext(BaseResponse<LockKey> response) {
                        Intent intent = new Intent(getCtx(), OpenDoorActivity.class);
                        intent.putExtra(Constant.DOOR, response.data);
                        intent.putStringArrayListExtra(Constant.EQUIP_LIST,requestList);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_door_list;
    }
}
