package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.RequestBodyBean.MultiLockRequest;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.panel.DoubleTimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/3/10.
 */

public class AddVisitorActivity extends BaseActivity implements View.OnClickListener, DoubleTimePicker.OnSelectedFinished {
    @BindView(R.id.info_time_start)
    TextView tvStart;
    @BindView(R.id.info_time_finish)
    TextView tvFinish;
    @BindView(R.id.add_visitor_name)
    EditText etName;
    @BindView(R.id.add_visitor_phone)
    EditText etPhone;


    private long currentDate;
    private int year;
    private String beginTime;
    private long endTime;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        currentDate = System.currentTimeMillis();
        beginTime = TribeDateUtils.dateFormat10(currentDate);
        tvStart.setText(beginTime);      //01月01日12时

        findViewById(R.id.add_visitor_contacts).setOnClickListener(this);
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(currentDate));
        long newDate = currentDate;
        year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        ArrayList<String> dayList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            calendar.setTime(new Date(newDate + i * 24 * 3600 * 1000));
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            dayList.add(month + "月" + day + "日");
        }
        ArrayList<String> hourList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourList.add(i + "时");
        }

        new DoubleTimePicker.Builder<String>(this, this)
                .setFirstData(dayList)
                .setSecondData(hourList)
                .setSecondCurrent(hour)
                .build();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_visitor;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_visitor_contacts:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
        }

    }

    @OnClick(R.id.add_visitor_finish)
    void onFinish() {
        if (etName.length() == 0 || etPhone.length() == 0 || tvFinish.getText().toString().contains("结束时间")) {
            ToastUtils.ToastMessage(getCtx(), R.string.not_empty);
            return;
        }
        if (TextUtils.equals(etPhone.getText().toString().trim(), TribeApplication.getInstance().getUserInfo().getPhone())) {
            ToastUtils.ToastMessage(getCtx(), getString(R.string.visitor_check));
            return;
        }
        createVisitor();
    }

    private void createVisitor() {
        showLoadingDialog();
        MultiLockRequest request = new MultiLockRequest();
        request.beginTime = currentDate;
        request.endTime = endTime;
        request.name = etName.getText().toString().trim();
        request.phone = etPhone.getText().toString().trim();
        TribeRetrofit.getInstance().createApi(DoorApis.class).getMultiKey(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<LockKey>>() {
                    @Override
                    public void onNext(BaseResponse<LockKey> lockKey) {
                        if (lockKey.code == 300) {
                            ToastUtils.ToastMessage(getCtx(), getString(R.string.door_problem));
                            return;
                        }
                        openDoor(lockKey.data);
                    }
                });
    }

    private void openDoor(LockKey data) {
        Intent intent = new Intent(getCtx(), OpenDoorActivity.class);
        intent.putExtra(Constant.DOOR, data);
        startActivity(intent);
        finish();
        AppManager.getAppManager().finishActivity(VisitorListActivity.class);
    }


    @Override
    public void onSelected(String result) {
        String[] arr = result.split("-");
        String[] arr1 = arr[0].split("月");
        String month = arr1[0];
        String day = arr1[1].split("日")[0];

        String hour = arr[1].substring(0, arr[1].length() - 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(hour), 0);
        long time = calendar.getTimeInMillis();
        if (time < currentDate) {
            ToastUtils.ToastMessage(getCtx(), "结束时间需大于当前时间");
            return;
        }
        endTime = time;
        tvFinish.setText(TribeDateUtils.dateFormat10(time));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    setContact(cursor);
                }
                break;
        }
    }

    private void setContact(Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        if (phoneNum > 0) {
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//                    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE); //某些手机可能需要加type判断，因为有公司电话，家庭电话，手机等
                    String displayName = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phone.getString(index);
                    etPhone.setText(replaceBlank(phoneNumber));
                    etName.setText(displayName);
                    etPhone.requestFocus();
                    etPhone.setSelection(etPhone.length());
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("-");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
