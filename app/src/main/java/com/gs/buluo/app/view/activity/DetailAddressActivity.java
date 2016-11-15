package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.AddressAdapter;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.presenter.AddressPresenter;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IAddressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by hjn on 2016/11/2.
 */
public class DetailAddressActivity extends BaseActivity implements IAddressView {
    @Bind(R.id.address_list)
    RecyclerView mRecView;

    List<UserAddressEntity> mDatas=new ArrayList<>();
    private final  int REQUEST_ADD= 200;
    private AddressAdapter mAdapter;
    private AddressInfoDao addressInfoDao;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.address_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(DetailAddressActivity.this,AddAddressActivity.class),REQUEST_ADD);
            }
        });
        findViewById(R.id.address_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addressInfoDao = new AddressInfoDao();
        mDatas= addressInfoDao.findAll(TribeApplication.getInstance().getUserInfo().getId());
        if (null==mDatas||mDatas.size()==0){
            return;
        }
        mAdapter = new AddressAdapter(this,mDatas);
        mRecView.setAdapter(mAdapter);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_detail_address;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new AddressPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_ADD&&resultCode==RESULT_OK){
            UserAddressEntity entity= (UserAddressEntity) data.getSerializableExtra(Constant.ADDRESS);
            mDatas.add(entity);
            mAdapter.notifyItemInserted(mDatas.size()-1);
        }
    }

    public AddressPresenter getAddressPresenter(){
        return  ((AddressPresenter)mPresenter);
    }

    @Override
    public void setSuccessInfo(UserAddressEntity data) {
        mDatas.remove(data);
        mAdapter.notifyDataSetChanged();
        addressInfoDao.deleteAddress(data);
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(res));
    }
}
