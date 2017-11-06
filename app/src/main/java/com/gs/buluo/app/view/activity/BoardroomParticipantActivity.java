package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ContactsDoneAdapter;
import com.gs.buluo.app.adapter.ContactsEditAdapter;
import com.gs.buluo.app.bean.ContactsPersonEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2017/10/24.
 */

public class BoardroomParticipantActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_edit)
    TextView mTvEdit;
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.rv_contacts)
    RecyclerView mRvContacts;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.layout_none)
    View layoutNone;
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
        initUI();
        mBack.setOnClickListener(this);
        if (getIntent().getBooleanExtra(Constant.CONTACT_FLAG,false)){
            mBtnConfirm.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        if (data.size() > 0) {
            hideNone();
        } else {
            showNone();
        }
    }

    private void showNone() {
        layoutNone.setVisibility(View.VISIBLE);
        mTvEdit.setVisibility(View.GONE);
    }

    private void hideNone() {
        layoutNone.setVisibility(View.GONE);
        mTvEdit.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.tv_edit, R.id.rv_contacts, R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent(this, BoardroomReserveActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, data);
                setResult(RESULT_OK,intent);
                finish();
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
        if (data.size() == 0) showNone();
    }

    private void startAdd() {
        startActivityForResult(new Intent(this, BoardroomParticipantAddActivity.class), Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT_ADD);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT_ADD) {
                this.data.addAll((ArrayList)data.getParcelableArrayListExtra(Constant.CONTACTS_DATA));
                initUI();
                adapterDone.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, BoardroomReserveActivity.class);
        intent.putExtra(Constant.CONTACTS_DATA, data);
        setResult(RESULT_OK,intent);
        finish();
    }
}
