package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BoardroomRecordAdapter;
import com.gs.buluo.app.bean.ConferenceReservation;
import com.gs.buluo.app.bean.ResponseBody.ConferenceRoomResponse;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.app.view.widget.recyclerHelper.NewRefreshRecyclerView;
import com.gs.buluo.app.view.widget.recyclerHelper.OnRefreshListener;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/10/20.
 */

public class BoardroomRecordActivity extends BaseActivity {
    @BindView(R.id.room_record_list)
    NewRefreshRecyclerView recyclerView;
    @BindView(R.id.room_record_status)
    StatusLayout statusLayout;

    ArrayList<ConferenceReservation> data = new ArrayList<>();
    private BoardroomRecordAdapter adapter;
    private String nextSkip;

    private boolean fromCreation;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        fromCreation = getIntent().getBooleanExtra(Constant.BOARD_RESERVE_FLAG,false);
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BoardroomRecordAdapter(R.layout.item_room_record, data);
        recyclerView.setAdapter(adapter);
        adapter.disableLoadMoreIfNotFullPage(recyclerView.getRecyclerView());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getCtx(), BoardroomRecordDetailActivity.class);
                intent.putExtra(Constant.BOARD_RESERVE_ID,data.get(position).id);
                startActivity(intent);
            }
        });
        adapter.setNewData(data);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getMore();
            }
        }, recyclerView.getRecyclerView());
        recyclerView.setRefreshAction(new OnRefreshListener() {
            @Override
            public void onAction() {
                adapter.clearData();
                getData();
            }
        });
        getData();
    }

    private void getData() {
        statusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getRoomRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ConferenceRoomResponse>>() {
                    @Override
                    public void onFail(ApiException e) {
                        recyclerView.setRefreshFinished();
                        statusLayout.showErrorView(getString(R.string.connect_fail));
                    }

                    @Override
                    public void onNext(BaseResponse<ConferenceRoomResponse> response) {
                        recyclerView.setRefreshFinished();
                        adapter.loadMoreComplete();
                        nextSkip = response.data.nextSkip;
                        statusLayout.showContentView();
                        adapter.addData(response.data.content);
                        if (adapter.getData().size()==0){
                            statusLayout.showEmptyView("暂无会议室记录");
                        }
                        if (!response.data.hasMore) {
                            adapter.loadMoreEnd();
                        }
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_record;
    }

    public void getMore() {
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getRoomRecordMore(nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ConferenceRoomResponse>>() {
                    @Override
                    public void onFail(ApiException e) {
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(BaseResponse<ConferenceRoomResponse> response) {
                        adapter.loadMoreComplete();
                        nextSkip = response.data.nextSkip;
                        adapter.addData(response.data.content);
                        if (!response.data.hasMore) {
                            adapter.loadMoreEnd();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (fromCreation) {
            startActivity(new Intent(getCtx(),MainActivity.class));
            finish();
            return;
        }
        super.onBackPressed();
    }
}
