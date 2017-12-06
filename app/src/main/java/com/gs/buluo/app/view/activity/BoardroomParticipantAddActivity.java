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
import com.gs.buluo.app.adapter.ContactsAdapter;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.network.BoardroomApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.PinyinComparator;
import com.gs.buluo.app.view.widget.SideBar;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/10/23.
 */

public class BoardroomParticipantAddActivity extends BaseActivity {
    @BindView(R.id.rv_contacts)
    RecyclerView mRvContacts;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.bar)
    SideBar indexBar;
    @BindView(R.id.dialog)
    TextView dialog;
    Map<String, ContactsPersonEntity> phoneContactDataMap = new HashMap<>();
    List<ContactsPersonEntity> commonContactData = new ArrayList<>();
    List<ContactsPersonEntity> phoneContactData = new ArrayList<>();
    List<ContactsPersonEntity> oldCheckedData;
    private PinyinComparator pinyinComparator= new PinyinComparator();
    private ContactsAdapter contactAdapter;
    private int commonSize;
    private LinearLayoutManager layoutManager;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_participant_add;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        indexBar.setTextView(dialog);
        indexBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = contactAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                     mRvContacts.scrollToPosition(position);
                }
            }
        });
        oldCheckedData = getIntent().getParcelableArrayListExtra(Constant.CONTACTS_DATA);
        getCommonContact();


    }

    private void getCommonContact() {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(BoardroomApis.class).getCommonContact()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<ContactsPersonEntity>>>() {
                    @Override
                    public void onNext(BaseResponse<List<ContactsPersonEntity>> listBaseResponse) {
                        commonContactData = listBaseResponse.data;
                        commonSize = commonContactData.size();
                        phoneContactDataMap = CommonUtils.fillMaps(getCtx());
                        initData();
                    }
                });

    }

    private void initCommonRv() {
        for (ContactsPersonEntity e : commonContactData
                ) {
            e.sortLetters = "#";
            if (!(oldCheckedData.size() > 0)) {
                continue;
            }
            ContactsPersonEntity entity = null;
            for (ContactsPersonEntity e1 : oldCheckedData
                    ) {
                if (e.phone.equals(e1.phone)) {
                    e.checked = true;
                    entity = e1;
                }
            }
            if (entity != null) oldCheckedData.remove(entity);
        }
    }

    private void initPhoneRv() {
        for (Map.Entry<String, ContactsPersonEntity> entry : phoneContactDataMap.entrySet()) {
            if ((oldCheckedData.size() > 0)) {
                ContactsPersonEntity entity = null;
                for (ContactsPersonEntity e1 : oldCheckedData
                        ) {
                    if (entry.getKey().equals(e1.phone)) {
                        entry.getValue().checked = true;
                        entity = e1;
                    }
                }
                if (entity != null) oldCheckedData.remove(entity);
            }
            phoneContactData.add(entry.getValue());
        }
        Collections.sort(phoneContactData, pinyinComparator);
        commonContactData.addAll(phoneContactData);
        layoutManager = new LinearLayoutManager(getCtx());
        mRvContacts.setLayoutManager(layoutManager);
        contactAdapter = new ContactsAdapter(R.layout.contacts_item, commonContactData, commonSize);
        mRvContacts.setAdapter(contactAdapter);
    }

    private void initData() {
        if (commonContactData.size() > 0) {
            for (ContactsPersonEntity e : commonContactData
                    ) {
                if (phoneContactDataMap.containsKey(e.phone)) {
                    phoneContactDataMap.remove(e.phone);
                }
            }
            initCommonRv();
        }
        initPhoneRv();
    }

    @OnClick({R.id.btn_confirm})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                ArrayList<ContactsPersonEntity> entities = new ArrayList<>();
                if (commonContactData.size() > 0)
                    for (ContactsPersonEntity e : commonContactData) {
                        if (e.checked) {
                            e.phone = CommonUtils.getPhoneString(e.phone);
                            entities.add(e);
                        }
                    }

                Intent intent = new Intent(this, BoardroomParticipantActivity.class);
                intent.putExtra(Constant.CONTACTS_DATA, entities);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
