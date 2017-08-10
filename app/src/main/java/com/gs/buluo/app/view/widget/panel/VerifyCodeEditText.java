package com.gs.buluo.app.view.widget.panel;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.app.R;

/**
 * Created by hjn on 2017/8/10.
 */

public class VerifyCodeEditText extends RelativeLayout {
    private EditText editText;
    private TextView[] textViews;
    private StringBuffer stringBuffer;
    private int count;
    private String strPassword;
    private VerifyCodeEditText.InputCompleteListener inputCompleteListener;

    public VerifyCodeEditText(Context context) {
        this(context, null);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.stringBuffer = new StringBuffer();
        this.count = 6;
        this.textViews = new TextView[6];
        View.inflate(context, R.layout.custom_verify, this);
        this.editText = (EditText) this.findViewById(R.id.item_edittext);
        this.textViews[0] = (TextView) this.findViewById(R.id.item_password_iv1);
        this.textViews[1] = (TextView) this.findViewById(R.id.item_password_iv2);
        this.textViews[2] = (TextView) this.findViewById(R.id.item_password_iv3);
        this.textViews[3] = (TextView) this.findViewById(R.id.item_password_iv4);
        this.textViews[4] = (TextView) this.findViewById(R.id.item_password_iv5);
        this.textViews[5] = (TextView) this.findViewById(R.id.item_password_iv6);
        this.editText.setCursorVisible(false);
        this.setListener();
    }

    public void showKeyBoard() {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager m = (InputMethodManager) VerifyCodeEditText.this.editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.showSoftInput(VerifyCodeEditText.this.editText, 2);
            }
        }, 500L);
    }

    public void dismissKeyBoard() {
        InputMethodManager m = (InputMethodManager) this.editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
    }

    private void setListener() {
        this.editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    if (stringBuffer.length() > 5) {
                        editText.setText("");
                        return;
                    }

                    stringBuffer.append(editable);
                    editText.setText("");
                    count = stringBuffer.length();
                    strPassword = stringBuffer.toString();
                    if (stringBuffer.length() == 6 && inputCompleteListener != null) {
                        inputCompleteListener.inputComplete();
                    }

                    for (int i = 0; i < stringBuffer.length(); ++i) {
                        textViews[i].setText(stringBuffer.charAt(i)+"");
                    }
                }

            }
        });
        this.editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == 67 && event.getAction() == 0;
            }
        });
    }

    public boolean onKeyDelete() {
        if (this.count == 0) {
            this.count = 6;
            return true;
        } else {
            if (this.stringBuffer.length() > 0) {
                this.stringBuffer.delete(this.count - 1, this.count);
                --this.count;
                this.strPassword = this.stringBuffer.toString();
                this.textViews[this.stringBuffer.length()].setText("");
            }
            return false;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void setInputCompleteListener(VerifyCodeEditText.InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public EditText getEditText() {
        return this.editText;
    }

    public String getStrPassword() {
        return this.strPassword;
    }

    public void setContent(String content) {
        this.editText.setText(content);
    }

    public void clear() {
        for (int i = 0; i < 6; ++i) {
            this.textViews[i].setText("");
        }

        this.stringBuffer = new StringBuffer();
        this.strPassword = "";
    }

    public interface InputCompleteListener {
        void inputComplete();
    }
}
