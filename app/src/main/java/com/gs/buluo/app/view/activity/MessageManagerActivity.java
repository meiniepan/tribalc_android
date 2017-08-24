package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.MessageManagerAdapter;
import com.gs.buluo.app.bean.MessageToggleBean;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/8/24.
 */

public class MessageManagerActivity extends BaseActivity {
    @Bind(R.id.msg_manager_list)
    ListView msgManagerList;
    @Bind(R.id.status_layout)
    StatusLayout statusLayout;
    private MessageManagerAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        statusLayout.showProgressView();
        UserInfoEntity userInfo = TribeApplication.getInstance().getUserInfo();
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageToggleList(userInfo.getId(), userInfo.getRole().contains(UserInfoEntity.Admin.AGENT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<MessageToggleBean>>>() {
                    @Override
                    public void onNext(BaseResponse<List<MessageToggleBean>> messageToggleBeen) {
                        setData(messageToggleBeen.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        statusLayout.showErrorView(getString(R.string.connect_fail));
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_message_manager;
    }

    public void setData(final List<MessageToggleBean> data) {
        if (data == null && data.size() == 0) {
            statusLayout.showEmptyView("当前无消息");
            return;
        }
        statusLayout.showContentView();
        adapter = new MessageManagerAdapter(this, data);
        msgManagerList.setAdapter(adapter);
    }
}
