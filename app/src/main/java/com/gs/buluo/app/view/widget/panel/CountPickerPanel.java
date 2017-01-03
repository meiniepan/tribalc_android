package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.wheel.OnWheelChangedListener;
import com.gs.buluo.app.view.widget.wheel.WheelView;
import com.gs.buluo.app.view.widget.wheel.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/12/1.
 */
public class CountPickerPanel extends Dialog{
    private Context mCtx;
    private OnSelectedFinished onSelectedFinished;
    private View rootView;
    private WheelView mCount;
    private TextView mBtnConfirm;
    private int newCount;

    public CountPickerPanel(Context context, OnSelectedFinished onSelectedFinished) {
        super(context, R.style.my_dialog);
        mCtx = context;
        this.onSelectedFinished = onSelectedFinished;
        initView();
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void setUpData() {
        newCount = 1;
        List<Integer> list =new ArrayList<>();
        for (int i =1;i<51;i++){
            list.add(i);
        }
        ArrayWheelAdapter<Object> viewAdapter = new ArrayWheelAdapter<>(mCtx, list.toArray());

        mCount.setViewAdapter(viewAdapter);
    }

    private void setUpListener() {
        mCount.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                newCount=newValue+1;
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedFinished.onSelected(newCount+"");
                dismiss();
            }
        });
    }

    private void setUpViews() {
        mCount = (WheelView) rootView.findViewById(R.id.id_count);
        mBtnConfirm = (TextView) rootView.findViewById(R.id.btn_confirm);
    }

    private void initView() {
        rootView = LayoutInflater.from(mCtx).inflate(R.layout.count_oicker_board, null);
        setContentView(rootView);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        ButterKnife.bind(this, rootView);
    }


    public interface OnSelectedFinished {
        void onSelected(String string);
    }
}
