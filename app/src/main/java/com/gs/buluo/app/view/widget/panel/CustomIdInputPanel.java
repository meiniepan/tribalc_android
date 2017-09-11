package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gs.buluo.app.R;

import butterknife.ButterKnife;

/**
 * Created by Solang on 2017/9/9.
 */

public class CustomIdInputPanel extends Dialog implements View.OnClickListener {
    private final Context mContext;
    private final EditText mEditText;
    private View rootView;
    Button btn0;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btnX;
    LinearLayout delete;

    public CustomIdInputPanel(@NonNull Context context, EditText editText) {
        super(context, R.style.my_dialog);
        mContext = context;
        mEditText = editText;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.custom_key_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        window.setDimAmount(0);
        initKeyBoard();
    }

    private void initKeyBoard() {
        btn0 = (Button) rootView.findViewById(R.id.btn_0);
        btn1 = (Button) rootView.findViewById(R.id.btn_1);
        btn2 = (Button) rootView.findViewById(R.id.btn_2);
        btn3 = (Button) rootView.findViewById(R.id.btn_3);
        btn4 = (Button) rootView.findViewById(R.id.btn_4);
        btn5 = (Button) rootView.findViewById(R.id.btn_5);
        btn6 = (Button) rootView.findViewById(R.id.btn_6);
        btn7 = (Button) rootView.findViewById(R.id.btn_7);
        btn8 = (Button) rootView.findViewById(R.id.btn_8);
        btn9 = (Button) rootView.findViewById(R.id.btn_9);
        btnX = (Button) rootView.findViewById(R.id.btn_x);
        delete = (LinearLayout) rootView.findViewById(R.id.ll_delete);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnX.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int index = mEditText.getSelectionStart();
        Editable editable = mEditText.getText();
        mEditText.setCursorVisible(true);
        mEditText.moveCursorToVisibleOffset();
        switch (v.getId()) {
            case R.id.btn_0:
                editable.insert(index, btn0.getText());
                break;
            case R.id.btn_1:
                editable.insert(index, btn1.getText());
                break;
            case R.id.btn_2:
                editable.insert(index, btn2.getText());
                break;
            case R.id.btn_3:
                editable.insert(index, btn3.getText());
                break;
            case R.id.btn_4:
                editable.insert(index, btn4.getText());
                break;
            case R.id.btn_5:
                editable.insert(index, btn5.getText());
                break;
            case R.id.btn_6:
                editable.insert(index, btn6.getText());
                break;
            case R.id.btn_7:
                editable.insert(index, btn7.getText());
                break;
            case R.id.btn_8:
                editable.insert(index, btn8.getText());
                break;
            case R.id.btn_9:
                editable.insert(index, btn9.getText());
                break;
            case R.id.btn_x:
                editable.insert(index, btnX.getText());
                break;
            case R.id.ll_delete:
                if (index > 0)
                    editable.delete(index - 1, index);
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mEditText.setCursorVisible(false);
    }
}
