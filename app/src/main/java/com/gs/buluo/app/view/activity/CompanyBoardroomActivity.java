package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

/**
 * Created by hjn on 2017/10/31.
 */

public class CompanyBoardroomActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.company_boardroom_history_bill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(),CompanyBoardroomRecordActivity.class));
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_boardroom;
    }

    public void boardroomRepay(View view) {
        Intent intent = new Intent(getCtx(),CreditRepaymentActivity.class);
        startActivity(intent);
    }
}
