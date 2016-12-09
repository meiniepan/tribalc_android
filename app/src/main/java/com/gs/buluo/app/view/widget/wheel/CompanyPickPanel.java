package com.gs.buluo.app.view.widget.wheel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by fs on 2016/12/7.
 */
public class CompanyPickPanel extends Dialog implements View.OnClickListener {

    private final BaseActivity mActivity;
    private WheelView mViewProvince;
    private WheelView mViewCommunity;
    private OnSelectedFinished onSelectedFinished;

    /**
     * 解析省社区
     */
    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCommunitysMap = new HashMap<String, String[]>();
    protected Map<String,String> mZipcodeDatasMap=new HashMap<String,String>();
    protected String mCurrentProvinceName;
    protected String mCurrentCommunityName;
    protected String mCurrentZipcode;
    private View rootView;
    private TextView mCancel;
    private View mOK;


    public CompanyPickPanel(BaseActivity activity, OnSelectedFinished onSelectedFinished) {
        super(activity, R.style.my_dialog);
        mActivity = activity;
        this.onSelectedFinished=onSelectedFinished;
        initView();
        setUpViews();
        setUpListener();
    }

    private void setUpListener() {
        mCancel.setOnClickListener(this);
        mOK.setOnClickListener(this);
    }


    private void initView() {
        rootView = LayoutInflater.from(mActivity).inflate(R.layout.picker_community_board, null);
        setContentView(rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        ButterKnife.bind(this, rootView);
    }

    private void setUpViews() {
        mViewProvince= (WheelView) rootView.findViewById(R.id.pick_community_province);
        mViewCommunity=((WheelView) rootView.findViewById(R.id.pick_community_community));
        mCancel = (TextView) rootView.findViewById(R.id.community_cancel);
        mOK = rootView.findViewById(R.id.community_ok);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.community_ok:
                onSelectedFinished.onSelected(mCurrentProvinceName+"-"+mCurrentCommunityName);
                dismiss();
                break;
            case R.id.community_cancel:
                dismiss();
                break;
        }
    }


    public interface OnSelectedFinished {
        void onSelected(String string);
    }

}
