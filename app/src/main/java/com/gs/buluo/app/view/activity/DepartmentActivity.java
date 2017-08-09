package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.DepartmentListAdapter;
import com.gs.buluo.app.bean.RentProtocol;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/6/23.
 */

public class DepartmentActivity extends BaseActivity {
    @Bind(R.id.department_status)
    StatusLayout statusLayout;
    @Bind(R.id.department_list)
    ListView listView;
    @Bind(R.id.department_head)
    SimpleDraweeView head;
    @Bind(R.id.department_title)
    View titleBar;
    private ArrayList<RentProtocol> departments;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.custom_color2);
        departments = new ArrayList<>();
        statusLayout.showProgressView();
        FresoUtils.loadImage(TribeApplication.getInstance().getUserInfo().getPicture(),head);
        String uid = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).getDepartmentList(uid, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<RentProtocol>>>() {
                    @Override
                    public void onNext(BaseResponse<List<RentProtocol>> listBaseResponse) {
                        setData(listBaseResponse.data);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_department;
    }

    public void setData(List<RentProtocol> data) {
        if (data==null||data.size()==0){
            statusLayout.showEmptyView("尚无公寓");
            return;
        }
        departments.addAll(data);
        if (departments.size()>1){
            findViewById(R.id.department_bottom).setVisibility(View.GONE);
        }
        statusLayout.showContentView();
        listView.setAdapter(new DepartmentListAdapter(this, departments));
        CommonUtils.setListViewHeightBasedOnChildren(listView);
    }
}
