package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;

import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.activity.OpenDoorActivity;
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
                Bitmap flur = CommonUtils.getFlur(getContext(),CommonUtils.getScreenshot(getContext(),getView()));
                Intent intent = new Intent(getActivity(), OpenDoorActivity.class);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                flur.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] bytes = outputStream.toByteArray();
                intent.putExtra(Constant.PICTURE, bytes);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
                break;
        }
    }
}
