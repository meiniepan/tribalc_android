package com.gs.buluo.app.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.PropertyBeen;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.view.widget.CustomAlertDialog;

import okhttp3.internal.framed.Variant;

public class PropertyActivity extends BaseActivity implements View.OnClickListener {
    private PropertyActivity mContext;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.property_part_fix).setOnClickListener(this);
        findViewById(R.id.property_public_light).setOnClickListener(this);
        findViewById(R.id.property_water_pipe_fix).setOnClickListener(this);
        findViewById(R.id.property_electric_fix).setOnClickListener(this);
        findViewById(R.id.property_other).setOnClickListener(this);
        findViewById(R.id.property_back).setOnClickListener(this);
        findViewById(R.id.property_fix_list).setOnClickListener(this);
        mContext = this;
    }

    @Override
    public void setBarColor(int colorInt) {
        super.setBarColor(colorInt);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_property;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.property_back:
                finish();
                break;
            case R.id.property_fix_list:
                startActivity(new Intent(mContext,PropertyListActivity.class));
                break;
            case R.id.property_part_fix:
                checkIsReady();
                break;
            case R.id.property_public_light:
                checkIsReady();
                break;
            case R.id.property_water_pipe_fix:
                checkIsReady();
                break;
            case R.id.property_electric_fix:
                checkIsReady();
                break;
            case R.id.property_other:
                checkIsReady();
                break;
        }
    }

    private void checkIsReady() {
        UserSensitiveDao dao = new UserSensitiveDao();
        UserSensitiveEntity entity = dao.findFirst();
        String name = entity.getName();

        if (TextUtils.isEmpty(name)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("您好").setMessage("请先进行个人实名认证");
            builder.setPositiveButton("去认证", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(mContext, VerifyActivity.class));
                }
            });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else {
            //判断用户是否绑定公司
            UserInfoDao userInfoDao = new UserInfoDao();
            UserInfoEntity userInfoEntity = userInfoDao.findFirst();
            String communityID = userInfoEntity.getCommunityID();
            String enterpriseID = entity.getCompanyID(); //企业id
            String companyName = entity.getCompanyName();

            if (TextUtils.isEmpty(communityID) || TextUtils.isEmpty(enterpriseID)) {
                CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(mContext);
                builder.setTitle("您好").setMessage("请先进行企业绑定");
                builder.setPositiveButton("去绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mContext, BindCompanyActivity.class));
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create().show();
            }else {
                //用户绑定和个人认证都进行了
                Intent intent = new Intent(this, AddPartFixActivity.class);
                PropertyBeen propertyBeen = new PropertyBeen();
                propertyBeen.communityID=communityID;
                propertyBeen.enterpriseID=enterpriseID;
                propertyBeen.name=name;
                propertyBeen.enterpriseName=companyName;
                intent.putExtra(Constant.ForIntent.PROPERTY_BEEN,propertyBeen);
                startActivity(intent);
            }
        }
    }
}
