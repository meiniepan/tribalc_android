package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.VisitorListAdapter;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.ResponseBody.VisitorListResponse;
import com.gs.buluo.app.bean.VisitorActiveBean;
import com.gs.buluo.app.network.DoorApis;
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
public class VisitorListActivity extends BaseActivity implements Callback<VisitorListResponse> {
    @Bind(R.id.visitor_list)
    ExpandableListView listView;
    private ArrayList<VisitorActiveBean> visitorList;
    private VisitorListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        visitorList = new ArrayList();
        ArrayList<LockKey> childList = new ArrayList<>();
        childList.add(new LockKey("d大门"));
        childList.add(new LockKey("梵蒂冈"));
        childList.add(new LockKey("大概是 "));
        childList.add(new LockKey("阿凡达"));
        visitorList.add(new VisitorActiveBean("张三","123456",childList));
        visitorList.add(new VisitorActiveBean("张是的","12412421",childList));
        visitorList.add(new VisitorActiveBean("张偶","241545",childList));
        visitorList.add(new VisitorActiveBean("张恩恩","3464363",childList));
        visitorList.add(new VisitorActiveBean("李元芳","3464363",childList));
        adapter = new VisitorListAdapter(getCtx(), visitorList);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent=new Intent(getCtx(),OpenDoorActivity.class);
                intent.putExtra(Constant.VISITOR, visitorList.get(groupPosition).keys.get(childPosition));
                startActivity(intent);
                return false;
            }
        });
        findViewById(R.id.add_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(),AddVisitorActivity.class);
                startActivity(intent);
            }
        });

        TribeRetrofit.getInstance().createApi(DoorApis.class).getVisitorList(TribeApplication.getInstance().getUserInfo().getId()).enqueue(this);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_visitor_list;
    }


    @Override
    public void onResponse(Call<VisitorListResponse> call, Response<VisitorListResponse> response) {
        if (response!=null &&response.code()==200 &&response.body()!=null){
            visitorList.addAll(response.body().data);
            adapter.notifyDataSetChanged();
        }else {
            ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
        }
    }

    @Override
    public void onFailure(Call<VisitorListResponse> call, Throwable t) {
        ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
    }
}
