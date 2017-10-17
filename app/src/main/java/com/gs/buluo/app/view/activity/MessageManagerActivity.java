package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.MessageManagerAdapter;
import com.gs.buluo.app.bean.ResponseBody.MessageToggleResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/8/24.
 */

public class MessageManagerActivity extends BaseActivity {
    @BindView(R.id.msg_manager_list)
    ListView msgManagerList;
    @BindView(R.id.msg_manager_list_additional)
    ListView msgManagerList2;
    @BindView(R.id.status_layout)
    StatusLayout statusLayout;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        statusLayout.showProgressView();
        UserInfoEntity userInfo = TribeApplication.getInstance().getUserInfo();
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageToggleList(userInfo.getId(), userInfo.getRole().contains(UserInfoEntity.Admin.AGENT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<MessageToggleResponse>>() {
                    @Override
                    public void onNext(BaseResponse<MessageToggleResponse> messageToggleBeen) {
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

    public void setData(final MessageToggleResponse data) {
        if (data == null || (data.primary.size() == 0 && data.additional.size() == 0)) {
            statusLayout.showEmptyView("当前无消息");
            return;
        }
        statusLayout.showContentView();
        MessageManagerAdapter adapter = new MessageManagerAdapter(this, data.primary, false);
        msgManagerList.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnChildren(msgManagerList);
        MessageManagerAdapter adapter2 = new MessageManagerAdapter(this, data.additional, true);
        msgManagerList2.setAdapter(adapter2);
        CommonUtils.setListViewHeightBasedOnChildren(msgManagerList2);
    }
}
