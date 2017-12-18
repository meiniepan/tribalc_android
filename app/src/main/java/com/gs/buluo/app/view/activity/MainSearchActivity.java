package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;

import butterknife.BindView;

/**
 * Created by Solang on 2017/7/17.
 */

public class MainSearchActivity extends BaseActivity {
    @BindView(R.id.search_edit)
    EditText etSearch;
    @BindView(R.id.search_content)
    TextView tvContent;
    @BindView(R.id.search_empty)
    View emptyView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==66){
                    emptyView.setVisibility(View.VISIBLE);
                    tvContent.setText(etSearch.getText().toString().trim());
                }
                return false;
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main_search;
    }
}
