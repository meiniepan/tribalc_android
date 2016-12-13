package com.gs.buluo.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.view.activity.PropertyActivity;

import butterknife.Bind;

/**
 * Created by admin on 2016/11/1.
 */
public class UsualFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "UsualFragment";
    @Bind(R.id.usual_fix_textView)
    TextView mFixTextView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_usual;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mFixTextView.setOnClickListener(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.usual_fix_textView:
                startActivity(new Intent(getActivity(), PropertyActivity.class));
                break;
        }

    }

}
