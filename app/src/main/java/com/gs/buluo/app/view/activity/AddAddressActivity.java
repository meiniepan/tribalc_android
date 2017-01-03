package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.presenter.AddAddressPresenter;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IAddAddressView;
import com.gs.buluo.app.view.widget.LoadingDialog;
import com.gs.buluo.app.view.widget.panel.AddressPickPanel;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/8.
 */
public class AddAddressActivity extends BaseActivity implements IAddAddressView {
//    @Bind(R.id.et_address_code)
//    EditText mCode;
    @Bind(R.id.et_address_area_detail)
    EditText mDetail;
    @Bind(R.id.et_address_name)
    EditText mName;
    @Bind(R.id.tv_address_area)
    TextView mAddress;
    @Bind(R.id.et_address_number)
    EditText mNumber;
    private UserAddressEntity mEntity;
    private String province;
    private String city;
    private String district;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mEntity = (UserAddressEntity) getIntent().getSerializableExtra(Constant.ADDRESS);
        if (null!=mEntity){
            mName.setText(mEntity.getName());
            mNumber.setText(mEntity.getPhone());
            mAddress.setText(mEntity.getArea());
            mDetail.setText(mEntity.getAddress());
        }

        findViewById(R.id.add_address_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.add_address_complete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UserAddressEntity entity=new UserAddressEntity();
                String name = mName.getText().toString().trim();
                String detailAddress = mDetail.getText().toString().trim();
                String phone = mNumber.getText().toString().trim();
                String addr = mAddress.getText().toString().trim();
                entity.setName(name);
                entity.setPhone(phone);
                entity.setUid(TribeApplication.getInstance().getUserInfo().getId());
                String str[]=addr.split("-");
                entity.setProvice(str[0]);
                entity.setCity(str[1]);
                entity.setDistrict(str[2]);
                entity.setAddress(detailAddress);
                if (null==mEntity){
                    showLoadingDialog();
                    ((AddAddressPresenter)mPresenter).addAddress(TribeApplication.getInstance().getUserInfo().getId(),entity);
                }else {
                    showLoadingDialog();
                    entity.setMid(mEntity.getMid());
                    entity.setId(mEntity.getId());
                    ((AddAddressPresenter)mPresenter).updateAddress(TribeApplication.getInstance().getUserInfo().getId(),mEntity.getId(),entity);
                }

            }
        });

        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAddressPicker();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_address;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new AddAddressPresenter();
    }

    @Override
    public void addAddressSuccess(UserAddressEntity data) {
        LoadingDialog.getInstance().dismissDialog();
        Intent intent=new Intent();
        intent.putExtra(Constant.ADDRESS,data);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void updateAddressSuccess(UserAddressEntity entity) {
        LoadingDialog.getInstance().dismissDialog();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showError(int res) {
        LoadingDialog.getInstance().dismissDialog();
        ToastUtils.ToastMessage(this,res);
    }

    private void initAddressPicker() {
        AddressPickPanel pickPanel = new AddressPickPanel(this, new AddressPickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String res) {
                mAddress.setText(res);
            }
        });
        pickPanel.show();
    }
}
