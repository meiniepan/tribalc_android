package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.CommonUtils;

/**
 * Created by jingnan on 2016/5/26.
 */

public class ModifyInfoPanel extends Dialog implements View.OnClickListener {
    public  final static int SEX = 1;
    public  final static int MOTION = 2;
    public static final int NAME = 3;
    public static final int ADDRESS = 4;

    private final Context mContext;
    private OnSelectedFinished onSelectedFinished;
    private int mType;
    private EditText editName;
    private View rootView ;
    private String phone="";

    public ModifyInfoPanel(Context context, int type, OnSelectedFinished onSelectedFinished){
        super(context,R.style.sheet_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        mContext = context;
        this.onSelectedFinished =onSelectedFinished;
        mType=type;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (mType==SEX){
            rootView= layoutInflater.inflate(R.layout.modify_board_sex, null);
            rootView.findViewById(R.id.self_male).setOnClickListener(this);
            rootView.findViewById(R.id.self_female).setOnClickListener(this);
        }else if (mType==MOTION){
            rootView= layoutInflater.inflate(R.layout.modify_board_motion, null);
            rootView.findViewById(R.id.self_married).setOnClickListener(this);
            rootView.findViewById(R.id.self_single).setOnClickListener(this);
            rootView.findViewById(R.id.self_double).setOnClickListener(this);
        }else if (mType==NAME){
            rootView= layoutInflater.inflate(R.layout.modify_board_name, null);
            rootView.findViewById(R.id.self_edit_nickname).setOnClickListener(this);
            rootView.findViewById(R.id.self_edit_yes).setOnClickListener(this);
            rootView.findViewById(R.id.self_edit_no).setOnClickListener(this);
            editName = (EditText) rootView.findViewById(R.id.self_edit_nickname);
        }
        setContentView(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.self_male:
                onSelectedFinished.onSelected(mContext.getResources().getString(R.string.male));
                dismiss();
                break;
            case R.id.self_female:
                onSelectedFinished.onSelected(mContext.getResources().getString(R.string.female));
                dismiss();
                break;
            case R.id.self_married:
                onSelectedFinished.onSelected(mContext.getResources().getString(R.string.married));
                dismiss();
                break;
            case R.id.self_single:
                onSelectedFinished.onSelected(mContext.getResources().getString(R.string.single));
                dismiss();
                break;
            case R.id.self_double:
                onSelectedFinished.onSelected(mContext.getResources().getString(R.string.loving));
                dismiss();
                break;
            case R.id.self_edit_yes:
                onSelectedFinished.onSelected(editName.getText().toString().trim());
                dismiss();
                break;
            case R.id.self_edit_no:
                dismiss();
                break;
            case R.id.phone_bind_next:
                EditText editText= (EditText) rootView.findViewById(R.id.bind_edit_phone);
                phone=editText.getText().toString().trim();
                if (!CommonUtils.checkPhone("86",phone,mContext))return;
                rootView.findViewById(R.id.phone_new_phone).setVisibility(View.GONE);
                rootView.findViewById(R.id.phone_bind_next).setVisibility(View.GONE);
                break;
        }
    }

    public interface OnSelectedFinished{
        void onSelected(String string);
    }
}
