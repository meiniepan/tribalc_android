package com.gs.buluo.app.view.widget;

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
import com.gs.buluo.app.utils.DensityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/12/7.
 */
public class PayPanel extends Dialog{
    private final OnPayPanelDismissListener onDismissListener;
    private Context mContext;
    @Bind(R.id.pay_way)
    TextView tvWay;
    @Bind(R.id.pay_money)
    TextView total;
    private OnPayListener onPayListener;

    public PayPanel(Context context,OnPayPanelDismissListener onDismissListener) {
        super(context ,R.style.my_dialog);
        mContext=context;
        this.onDismissListener=onDismissListener;
        initView();
    }

    public void setData(String way,String price,OnPayListener onPayListener){
        tvWay.setText(way);
        total.setText(price);
        this.onPayListener=onPayListener;
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pay_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext,400);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        findViewById(R.id.pay_ask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.pay_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.pay_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPayListener.onPaySuccess();

            }
        });
    }

    public interface OnPayListener{
        void onPaySuccess();
        void onPayFail();
    }

    public interface OnPayPanelDismissListener {
        void onPayPanelDismiss();
    }

    @Override
    public void dismiss() {
        onDismissListener.onPayPanelDismiss();
        super.dismiss();
    }
}
