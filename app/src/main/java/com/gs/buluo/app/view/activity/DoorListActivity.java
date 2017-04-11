package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.DoorListAdapter;
import com.gs.buluo.app.bean.LockEquip;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.RequestBodyBean.LockRequest;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.SharePreferenceManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/3/9.
 */
public class DoorListActivity extends BaseActivity  {
    @Bind(R.id.door_list)
    ListView listView;
    private DoorListAdapter listAdapter;
    private ArrayList<LockEquip> list;
    private View emptyView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list = new ArrayList<>();
        listAdapter = new DoorListAdapter(getCtx(), list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDoorKey(list.get(position).id);
            }
        });
        emptyView = findViewById(R.id.door_empty_view);
        getLockList();
        String array = SharePreferenceManager.getInstance(this).getStringValue(Constant.DOOR_LIST);
        List<LockEquip> lockEquips = JSON.parseArray(array, LockEquip.class);
        if (lockEquips != null) list.addAll(lockEquips);
    }

    private void getLockList() {
        TribeRetrofit.getInstance().createApi(DoorApis.class).getEquipList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<LockEquip>>>() {
                    @Override
                    public void onNext(BaseResponse<List<LockEquip>> lockList) {
                        super.onNext(lockList);
                        list.clear();
                        if (lockList != null && lockList.data.size() != 0) {
                            list.addAll(lockList.data);
                            listAdapter.notifyDataSetChanged();
                            SharePreferenceManager.getInstance(getCtx()).setValue(Constant.DOOR_LIST, JSON.toJSONString(list));
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_door_list;
    }


    public void getDoorKey(String id) {
        LockRequest request = new LockRequest();
        request.equipId = id;
        TribeRetrofit.getInstance().createApi(DoorApis.class).getLockKey(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<LockKey>>() {
                    @Override
                    public void onNext(BaseResponse<LockKey> lockKey) {
                        super.onNext(lockKey);
                        Intent intent = new Intent(getCtx(), OpenDoorActivity.class);
                        intent.putExtra(Constant.DOOR,lockKey.data);
                        startActivity(intent);
                        finish();
                    }
                });

    }
}
