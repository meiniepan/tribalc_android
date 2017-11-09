package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.CompanyBoardroomRecordAdapter;
import com.gs.buluo.app.bean.ConferenceReservation;
import com.gs.buluo.app.bean.ResponseBody.CompanyBoardroomResponse;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/10/31.
 */

public class CompanyBoardroomRecordActivity extends BaseActivity {
    @BindView(R.id.record_status)
    StatusLayout statusLayout;
    @BindView(R.id.company_boardroom_record)
    RecyclerView recyclerView;
    private CompanyBoardroomRecordAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new CompanyBoardroomRecordAdapter(R.layout.item_company_boardroom_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getMore();
            }
        }, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getCtx(),BoardroomRecordDetailActivity.class);
                intent.putExtra(Constant.BOARD_RESERVE_ID,((ConferenceReservation)adapter.getData().get(position)).id);
                startActivity(intent);
            }
        });
        statusLayout.showProgressView();
        TribeRetrofit.getInstance().createApi(BoardroomApis.class)
                .getCompanyBoardroomRecord(TribeApplication.getInstance().getUserInfo().getCompanyID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CompanyBoardroomResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CompanyBoardroomResponse> conferenceReserveEntityBaseResponse) {
                        setData(conferenceReserveEntityBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(getString(R.string.connect_fail));
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_boardroom_record;
    }

    public void setData(CompanyBoardroomResponse data) {
        statusLayout.showContentView();
        adapter.setNewData(data.content);
        nextSkip = data.nextSkip;
        if (adapter.getData().size() == 0) {
            statusLayout.showEmptyView("公司尚无会议室记录");
        }
        if (!data.hasMore){
            adapter.loadMoreEnd(true);
        }
    }

    private String nextSkip;

    public void getMore() {
        TribeRetrofit.getInstance().createApi(BoardroomApis.class)
                .getCompanyBoardroomRecordMore(TribeApplication.getInstance().getUserInfo().getCompanyID(), nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CompanyBoardroomResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CompanyBoardroomResponse> conferenceReserveEntityBaseResponse) {
                        nextSkip = conferenceReserveEntityBaseResponse.data.nextSkip;
                        if (conferenceReserveEntityBaseResponse.data.content!=null) adapter.addData(conferenceReserveEntityBaseResponse.data.content);
                        if (!conferenceReserveEntityBaseResponse.data.hasMore) {
                            adapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(getString(R.string.connect_fail));
                    }
                });

    }
}
