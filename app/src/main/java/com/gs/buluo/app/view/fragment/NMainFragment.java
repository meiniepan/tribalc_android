package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.HomeMessageAdapter;
import com.gs.buluo.app.bean.HomeMessage;
import com.gs.buluo.app.bean.HomeMessageResponse;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.activity.MainSearchActivity;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/12.
 */

public class NMainFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.home_rc)
    XRecyclerView mRefreshRecycleView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_n;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        getActivity().findViewById(R.id.tv_search).setOnClickListener(this);
        mRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_home_head, null);
        mRefreshRecycleView.addHeaderView(view);
        mRefreshRecycleView.setRefreshPosition(1);
        mRefreshRecycleView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mRefreshRecycleView.refreshComplete();
            }

            @Override
            public void onLoadMore() {

            }
        });


        getData();
    }

    private void initView(final ArrayList<HomeMessage> lists) {
        HomeMessageAdapter adapter = new HomeMessageAdapter(getActivity(), lists);
        mRefreshRecycleView.setAdapter(adapter);
    }

    private void getData() {
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessage(TribeApplication.getInstance().getUserInfo().getId(), 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        initView(response.data.content);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        ToastUtils.ToastMessage(mContext, "获取消息错误");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
