package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.app.R;

import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankPickPanel extends Dialog implements View.OnClickListener {
    Context mCtx;

    private OnSelectedFinished onSelectedFinished;
    public BankPickPanel(Context context,OnSelectedFinished onSelectedFinished) {
        super(context, R.style.my_dialog);
        this.onSelectedFinished=onSelectedFinished;
        mCtx=context;
        initView();
    }

    private void initView() {
        View view= LayoutInflater.from(mCtx).inflate(R.layout.bank_choose,null);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        ButterKnife.bind(this, view);

        findViewById(R.id.bank_build).setOnClickListener(this);
        findViewById(R.id.bank_business).setOnClickListener(this);
        findViewById(R.id.bank_china).setOnClickListener(this);
        findViewById(R.id.bank_icbc).setOnClickListener(this);
        findViewById(R.id.bank_farm).setOnClickListener(this);
        findViewById(R.id.bank_light).setOnClickListener(this);
        findViewById(R.id.bank_pufa).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bank_build:
                onSelectedFinished.onSelected(R.string.build_bank);
                dismiss();
                break;
            case R.id.bank_business:
                onSelectedFinished.onSelected(R.string.business_bank);
                dismiss();
                break;
            case R.id.bank_china:
                onSelectedFinished.onSelected(R.string.china_bank);
                dismiss();
                break;
            case R.id.bank_icbc:
                onSelectedFinished.onSelected(R.string.ic_bank);
                dismiss();
                break;
            case R.id.bank_farm:
                onSelectedFinished.onSelected(R.string.farm_bank);
                dismiss();
                break;
            case R.id.bank_light:
                onSelectedFinished.onSelected(R.string.light_bank);
                dismiss();
                break;
            case R.id.bank_pufa:
                onSelectedFinished.onSelected(R.string.pufa_bank);
                dismiss();
                break;
        }
    }

    public interface OnSelectedFinished{
        void onSelected(int res);
    }
}
