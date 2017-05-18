package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.VisitorListAdapter;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListActivity extends BaseActivity {
    @Bind(R.id.visitor_list)
    ListView listView;
    private ArrayList<LockKey> visitorList;
    private VisitorListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        visitorList = new ArrayList<>();
        adapter = new VisitorListAdapter(this, visitorList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getCtx(), OpenDoorActivity.class);
                intent.putExtra(Constant.DOOR, visitorList.get(position));
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.add_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), AddVisitorActivity.class);
                startActivity(intent);
            }
        });

        TribeRetrofit.getInstance().createApi(DoorApis.class).getMultiKeyList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ArrayList<LockKey>>>() {
                    @Override
                    public void onNext(BaseResponse<ArrayList<LockKey>> listBaseResponse) {
                        if (listBaseResponse.data == null || listBaseResponse.data.size() == 0) {
                            findViewById(R.id.visitor_empty_view).setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.visitor_empty_view).setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            visitorList.addAll(listBaseResponse.data);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_visitor_list;
    }

    public void showEmpty() {
        findViewById(R.id.visitor_empty_view).setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }
}
