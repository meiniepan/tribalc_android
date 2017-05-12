package com.gs.buluo.app.view.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.BankListAdapter;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class BankPickActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.bank_pick_list)
    ListView mListView;
    private List<String> mList;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String[] arr = new String[]{"工商银行","中国银行","建设银行","交通银行","邮政储蓄银行","光大银行","平安银行","浦发银行","中信银行",
                "上海银行","北京银行","兴业银行","农业银行"};
        mList = Arrays.asList(arr);
        BankListAdapter adapter = new BankListAdapter(this);
        mListView.setAdapter(adapter);
        adapter.setData(mList);
        mListView.setOnItemClickListener(this);

        findViewById(R.id.card_bind_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_back_pick;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(Constant.ForIntent.FLAG,mList.get(position));
        setResult(RESULT_OK,intent);
        finish();
    }
}
