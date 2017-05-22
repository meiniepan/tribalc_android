package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.RequestBodyBean.MultiLockRequest;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.DoorListActivity;
import com.gs.buluo.app.view.activity.OpenDoorActivity;
import com.gs.buluo.app.view.activity.PropertyActivity;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/11/1.
 */
public class UsualFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_usual;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        getActivity().findViewById(R.id.usual_open_door).setOnClickListener(this);
        getActivity().findViewById(R.id.usual_property).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (!checkUser(getActivity())) return;
        switch (view.getId()) {
            case R.id.usual_property:
                startActivity(new Intent(getActivity(), PropertyActivity.class));
                break;
            case R.id.usual_open_door:
                if (checkQualification()) return;
                getLockInfo();
                break;
        }
    }

    public boolean checkQualification() {
        if (TribeApplication.getInstance().getUserInfo().getIdNo() == null) {
            ToastUtils.ToastMessage(getContext(), getString(R.string.no_identify));
            return true;
        }
        if (TribeApplication.getInstance().getUserInfo().getCompanyID() == null) {
            ToastUtils.ToastMessage(getContext(), getString(R.string.no_company_bind));
            return true;
        }
        return false;
    }

    public void getLockInfo() {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(DoorApis.class).getMultiKey(TribeApplication.getInstance().getUserInfo().getId(), new MultiLockRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<LockKey>>() {
                    @Override
                    public void onNext(BaseResponse<LockKey> response) {
                        dismissDialog();
                        if (response.code == 300) {
                            Intent intent = new Intent(getActivity(), DoorListActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), OpenDoorActivity.class);
                            intent.putExtra(Constant.DOOR, response.data);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFail(ApiException e) {
                        dismissDialog();
                        if (e.getCode() == 403) {
                            ToastUtils.ToastMessage(getActivity(), R.string.no_door);
                        } else {
                            ToastUtils.ToastMessage(getActivity(), R.string.connect_fail);
                        }
                    }
                });
    }
}
