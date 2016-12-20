package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

import com.gs.buluo.app.view.activity.PropertyActivity;
import com.gs.buluo.app.view.widget.OpenPanel;

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
        switch (view.getId()){
            case R.id.usual_property:
                startActivity(new Intent(getActivity(), PropertyActivity.class));
                break;
            case R.id.usual_open_door:
                break;
        }
    }

}
