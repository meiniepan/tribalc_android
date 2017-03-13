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
import com.gs.buluo.app.bean.RequestBodyBean.LockRequest;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.LockEquipResponse;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/3/9.
 */
public class DoorListActivity extends BaseActivity implements Callback<LockEquipResponse> {
    @Bind(R.id.door_list)
    ListView listView;
    private DoorListAdapter listAdapter;
    private ArrayList<LockEquip> list;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list = new ArrayList<>();
        list.add(new LockEquip("一楼门锁"));
        list.add(new LockEquip("儿楼门锁"));
        list.add(new LockEquip("三楼门锁"));
        list.add(new LockEquip("死楼门锁"));
        listAdapter = new DoorListAdapter(getCtx(), list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDoorKey(list.get(position).id);
            }
        });
        TribeRetrofit.getInstance().createApi(DoorApis.class).getEquipList(TribeApplication.getInstance().getUserInfo().getId()).enqueue(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_door_list;
    }

    @Override
    public void onResponse(Call<LockEquipResponse> call, Response<LockEquipResponse> response) {
        if (response!=null&&response.code()==200&&response.body()!=null){
            list.addAll(response.body().data);
            listAdapter.notifyDataSetChanged();
        }else {
            ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
        }
    }

    @Override
    public void onFailure(Call<LockEquipResponse> call, Throwable t) {
        ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
    }

    public void getDoorKey(String id) {
        LockRequest request= new LockRequest();
        request.equipId =id;
        TribeRetrofit.getInstance().createApi(DoorApis.class).getLockKey(TribeApplication.getInstance().getUserInfo().getId(),request)
                .enqueue(new TribeCallback<LockKey>() {
            @Override
            public void onSuccess(Response<BaseResponse<LockKey>> response) {
                Intent intent=new Intent(getCtx(),OpenDoorActivity.class);
                intent.putExtra(Constant.DOOR,response.body().data);
                startActivity(intent);
            }

            @Override
            public void onFail(int responseCode, BaseResponse<LockKey> body) {
                ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
            }
        });

    }
}
