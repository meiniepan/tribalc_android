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

/**
 * Created by fs on 2016/12/20.
 */

public class SimpleChoosePanel extends Dialog {


    public SimpleChoosePanel(Context context) {
        super(context, R.style.my_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }


    public interface OnSelectedFinished{
        void onSelected(String string);
    }


    public static class Builder implements View.OnClickListener, OnWheelChangedListener {

        private final Context mContext;
        private final OnSelectedFinished mOnSelectedFinished;
        private String title="请选择";
        private int max=20;
        private int position=1;
        private SimpleChoosePanel mSimpleChoosePanel;


        public Builder(Context context, OnSelectedFinished onSelectedFinished) {
            mContext = context;
            mOnSelectedFinished = onSelectedFinished;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        public SimpleChoosePanel build(){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.simple_choose_board, null);
            ((TextView) view.findViewById(R.id.simple_choose_title)).setText(title);
            WheelView wheelView = (WheelView) view.findViewById(R.id.simple_choose_wheel);
            wheelView.addChangingListener(this);
            view.findViewById(R.id.simple_choose_confirm).setOnClickListener(this);

            List<Integer> list =new ArrayList<>();
            for (int i =1;i<=max;i++){
                list.add(i);
            }
            ArrayWheelAdapter<Object> viewAdapter = new ArrayWheelAdapter<>(mContext, list.toArray());
            wheelView.setViewAdapter(viewAdapter);

            mSimpleChoosePanel = new SimpleChoosePanel(mContext);
            mSimpleChoosePanel.setContentView(view);

            return mSimpleChoosePanel;
        }



        @Override
        public void onClick(View v) {
            mOnSelectedFinished.onSelected(position+"");
            mSimpleChoosePanel.dismiss();
        }


        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            position = newValue+1;
        }
    }


}
