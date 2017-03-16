package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.DoorListActivity;
import com.gs.buluo.app.view.activity.PropertyActivity;

import java.io.ByteArrayOutputStream;

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
        if (!checkUser(getActivity()))return;
        switch (view.getId()) {
            case R.id.usual_property:
                startActivity(new Intent(getActivity(), PropertyActivity.class));
                break;
            case R.id.usual_open_door:
                if (checkQualification())return;
                Intent intent = new Intent(getActivity(), DoorListActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean checkQualification() {
        if (TribeApplication.getInstance().getUserInfo().getIdNo()==null){
            ToastUtils.ToastMessage(getContext(),getString(R.string.no_identify));
            return true;
        }
        if (TribeApplication.getInstance().getUserInfo().getCompanyID()==null){
            ToastUtils.ToastMessage(getContext(),getString(R.string.no_company_bind));
            return true;
        }
        return false;
    }
}
