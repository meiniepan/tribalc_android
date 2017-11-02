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

    @Override
    protected void bindView(Bundle savedInstanceState) {
        recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BoardroomRecordAdapter(R.layout.item_room_record, data);
        recyclerView.setAdapter(adapter);
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
                        statusLayout.showErrorView(getString(R.string.connect_fail));
                    }

                    @Override
                    public void onNext(BaseResponse<ConferenceRoomResponse> response) {
                        nextSkip = response.data.nextSkip;
                        statusLayout.showContentView();
                        adapter.addData(response.data.content);
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
                        adapter.addData(response.data.content);
                        if (!response.data.hasMore) {
                            adapter.loadMoreComplete();
                        }
                    }
                });
    }
}
