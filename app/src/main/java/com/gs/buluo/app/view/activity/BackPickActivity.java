package com.gs.buluo.app.view.activity;
import android.os.Bundle;
import android.widget.ListView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BankListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class BackPickActivity extends BaseActivity {

    @Bind(R.id.bank_pick_list)
    ListView mListView;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String[] arr = new String[]{"中国农业银行","中国银行","中国建设银行","上海浦发银行","广东发展银行","中国光大银行","中国招商银行","华夏银行","深圳发展银行",
                "兴业银行","民生银行","恒丰银行","中国农业发展银行","中国进出口银行","中信银行"};
        List<String> list = Arrays.asList(arr);
        BankListAdapter adapter = new BankListAdapter(this);
        mListView.setAdapter(adapter);
        adapter.setData(list);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_back_pick;
    }

}
