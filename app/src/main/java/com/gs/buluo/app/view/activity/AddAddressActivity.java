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
import com.gs.buluo.app.presenter.AddressPresenter;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IAddressView;
import com.gs.buluo.app.widget.PickPanel;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/8.
 */
public class AddAddressActivity extends BaseActivity implements IAddressView {
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
                entity.setName(mName.getText().toString().trim());
                entity.setPhone(mNumber.getText().toString().trim());
                entity.setUid(TribeApplication.getInstance().getUserInfo().getId());
                entity.setProvice(province);
                entity.setCity(city);
                entity.setDistrict(district);
                entity.setDetailAddress(mDetail.getText().toString().trim());
                if (null==mEntity){
                    ((AddressPresenter)mPresenter).addAddress(TribeApplication.getInstance().getUserInfo().getId(),entity);
                }else {
                    ((AddressPresenter)mPresenter).updateAddress(TribeApplication.getInstance().getUserInfo().getId(),mEntity.getId(),mEntity);
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
        return new AddressPresenter();
    }

    @Override
    public void setSuccessInfo(UserAddressEntity data) {
        Intent intent=new Intent();
        intent.putExtra(Constant.ADDRESS,data);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,res);
    }

    private void initAddressPicker() {
        PickPanel pickPanel = new PickPanel(this, new PickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String res) {
                mAddress.setText(res);
                String str[]=res.split(",");
                province = str[0];
                city = str[1];
                district = str[2];
            }
        });
        pickPanel.show();
    }
}
