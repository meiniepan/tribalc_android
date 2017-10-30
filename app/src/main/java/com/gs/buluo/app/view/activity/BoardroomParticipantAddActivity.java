package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ContactsAdapter;
import com.gs.buluo.app.utils.CommonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2017/10/23.
 */

public class BoardroomParticipantAddActivity extends BaseActivity {
    @BindView(R.id.rv_contacts)
    RecyclerView mRvContacts;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    ArrayList<ContactsPersonEntity> data;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_participant_add;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRvContacts.setLayoutManager(new LinearLayoutManager(getCtx()));
        data = CommonUtils.fillMaps(getCtx());
        ContactsAdapter adapter = new ContactsAdapter(R.layout.contacts_item, data);
        mRvContacts.setAdapter(adapter);
    }

    @OnClick({R.id.rv_contacts, R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                finish();
                ArrayList<ContactsPersonEntity> entities = new ArrayList<>();
                for (ContactsPersonEntity e: data
                     ) {
                    if (e.checked) entities.add(e);
                }
                Intent intent = new Intent(this,BoardroomParticipantActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA,entities);
                startActivity(intent);
                break;
        }
    }
}
