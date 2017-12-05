package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.ContactsDoneAdapter;
import com.gs.buluo.app.bean.ContactsPersonEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2017/10/24.
 */

public class BoardroomParticipantActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.rv_contacts)
    RecyclerView mRvContacts;
    @BindView(R.id.ll_bottom)
    View bottom;
    @BindView(R.id.layout_none)
    View layoutNone;
    ArrayList<ContactsPersonEntity> importData;
    ArrayList<ContactsPersonEntity> allData = new ArrayList<>();
    ArrayList<ContactsPersonEntity> inputData = new ArrayList<>();
    ContactsDoneAdapter adapterDone;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_participant;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        allData = getIntent().getParcelableArrayListExtra(Constant.CONTACTS_DATA);
        mRvContacts.setLayoutManager(new LinearLayoutManager(getCtx()));
        adapterDone = new ContactsDoneAdapter(R.layout.contacts_done_item, allData);
        mRvContacts.setAdapter(adapterDone);
        initUI();
        mBack.setOnClickListener(this);
        if (getIntent().getBooleanExtra(Constant.CONTACT_FLAG, false)) {
            bottom.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        if (allData.size() > 0) {
            hideNone();
        } else {
            showNone();
        }
    }

    private void showNone() {
        layoutNone.setVisibility(View.VISIBLE);
    }

    private void hideNone() {
        layoutNone.setVisibility(View.GONE);
    }


    @OnClick({R.id.rv_contacts})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent(this, BoardroomReserveActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, allData);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.rv_contacts:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT_ADD) {
                allData.clear();
                allData.addAll((List) data.getParcelableArrayListExtra(Constant.CONTACTS_DATA));
                for (ContactsPersonEntity e : inputData
                        ) {
                    boolean isHave = false;
                    for (ContactsPersonEntity e1 : allData) {
                        if (e.phone.equals(e1.phone)) {
                            e1.name = e.name;
                            isHave = true;
                        }
                    }
                    if (!isHave) allData.add(e);
                }
                initUI();
                adapterDone.notifyDataSetChanged();
            } else if (requestCode == Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT_INPUT) {
                ContactsPersonEntity entity = data.getParcelableExtra(Constant.CONTACT_DATA);
                boolean isHave = false;
                for (ContactsPersonEntity e : inputData
                        ) {
                    if (e.phone.equals(entity.phone)) isHave = true;
                }
                if (!isHave) inputData.add(entity);
                isHave = false;
                for (ContactsPersonEntity e : allData
                        ) {
                    if (e.phone.equals(entity.phone)) {
                        isHave = true;
                        e.name = entity.name;
                    }
                }
                if (!isHave) allData.add(entity);
                initUI();
                adapterDone.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, BoardroomReserveActivity.class);
        intent.putExtra(Constant.CONTACTS_DATA, allData);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void imports(View view) {
        Intent intent = new Intent(this, BoardroomParticipantAddActivity.class);
        intent.putExtra(Constant.CONTACTS_DATA, allData);
        startActivityForResult(intent,
                Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT_ADD);
    }

    public void input(View view) {
        startActivityForResult(new Intent(this, BoardroomParticipantInputActivity.class),
                Constant.ForIntent.REQUEST_CODE_BOARDROOM_PARTICIPANT_INPUT);
    }
}
