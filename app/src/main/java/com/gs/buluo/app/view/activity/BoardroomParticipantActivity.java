package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ContactsDoneAdapter;
import com.gs.buluo.app.adapter.ContactsEditAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2017/10/24.
 */

class BoardroomParticipantActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_edit)
    TextView mTvEdit;
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.rv_contacts)
    RecyclerView mRvContacts;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    ArrayList<ContactsPersonEntity> data;
    ContactsDoneAdapter adapterDone;
    ContactsEditAdapter adapterEdit;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_participant;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        data = getIntent().getParcelableArrayListExtra(Constant.CONTACTS_DATA);
        mRvContacts.setLayoutManager(new LinearLayoutManager(getCtx()));
        adapterDone = new ContactsDoneAdapter(R.layout.contacts_done_item, data);
        mRvContacts.setAdapter(adapterDone);
        mBack.setOnClickListener(this);
    }


    @OnClick({R.id.tv_edit, R.id.rv_contacts, R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent(this, BoardroomReserveActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, data);
                startActivity(intent);
                break;
            case R.id.tv_edit:
                if (mTvEdit.getText().equals("编辑"))
                    startEdit();
                else finishEdit();
                break;
            case R.id.rv_contacts:
                break;
            case R.id.btn_confirm:
                if (mBtnConfirm.getText().equals("添加参会人"))
                    startAdd();
                else delete();
                break;
        }
    }

    private void delete() {
        ArrayList<ContactsPersonEntity> dataDelete = new ArrayList<>();
        for (ContactsPersonEntity e : data
                ) {
            if (e.deleted) dataDelete.add(e);
        }
        data.removeAll(dataDelete);
        adapterEdit.notifyDataSetChanged();
    }

    private void startAdd() {
        startActivity(new Intent(this, BoardroomParticipantAddActivity.class));
    }

    private void finishEdit() {

        mTvEdit.setText("编辑");
        adapterDone = new ContactsDoneAdapter(R.layout.contacts_done_item, data);
        mRvContacts.setAdapter(adapterDone);
        mBtnConfirm.setText("添加参会人");
    }

    private void startEdit() {
        mTvEdit.setText("完成");
        adapterEdit = new ContactsEditAdapter(R.layout.contacts_edit_item, data);
        mRvContacts.setAdapter(adapterEdit);
        mBtnConfirm.setText("删 除");
    }
}
