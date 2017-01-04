package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.NewReserveRequest;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.model.ReserveModel;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.widget.panel.CountPickerPanel;
import com.gs.buluo.app.view.widget.panel.DatePickerPanel;

import java.util.Date;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/1.
 */
public class BookingServeActivity extends BaseActivity implements View.OnClickListener, Callback<BaseCodeResponse<DetailReservation>> {
    @Bind(R.id.add_serve_count)
    TextView tvCount;
    @Bind(R.id.add_serve_name)
    EditText tvName;
    @Bind(R.id.add_serve_phone)
    TextView tvPhone;
    @Bind(R.id.add_serve_time)
    TextView tvTime;
    @Bind(R.id.add_serve_note)
    TextView tvNote;
    @Bind(R.id.add_serve_group)
    RadioGroup group;
    private UserInfoEntity.Gender sex;
    private long appointTime;
    private View updateArea;

    @Bind(R.id.add_serve_phone_new)
    EditText newPhone;
    @Bind(R.id.add_serve_verify)
    EditText vCode;
    private View phoneArea;
    private String serveId;
    private TextView cancel;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        serveId = getIntent().getStringExtra(Constant.SERVE_ID);
        findViewById(R.id.add_serve_update_phone).setOnClickListener(this);
        findViewById(R.id.add_serve_people_select).setOnClickListener(this);
        findViewById(R.id.add_serve_time_select).setOnClickListener(this);
        findViewById(R.id.add_serve_finish).setOnClickListener(this);
        findViewById(R.id.add_serve_back).setOnClickListener(this);
        cancel = (TextView) findViewById(R.id.add_serve_cancel);
        cancel.setOnClickListener(this);
        updateArea = findViewById(R.id.add_serve_update_area);
        phoneArea = findViewById(R.id.add_serve_phone_area);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.add_serve_radio_male) {
                    sex = UserInfoEntity.Gender.MALE;
                } else {
                    sex = UserInfoEntity.Gender.FEMALE;
                }
            }
        });

        UserSensitiveEntity first = new UserSensitiveDao().findFirst();
        tvPhone.setText(first.getPhone());
        if (first.getName() != null) {
            tvName.setText(first.getName());
        }

        newPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newPhone.getText().length() == 0) {
                    cancel.setText(R.string.cancel_update);
                } else {
                    cancel.setText(R.string.send_verify);
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_serve;
    }

    @Override
    public void onClick(View v) {
        String newNum = newPhone.getText().toString().trim();
        switch (v.getId()) {
            case R.id.add_serve_update_phone:
                phoneArea.setVisibility(View.GONE);
                updateArea.setVisibility(View.VISIBLE);
                break;
            case R.id.add_serve_people_select:
                showCountPicker();
                break;
            case R.id.add_serve_back:
                finish();
                break;
            case R.id.add_serve_cancel:
                if (newNum.length() == 0) {
                    updateArea.setVisibility(View.GONE);
                    phoneArea.setVisibility(View.VISIBLE);
                } else {
                    if (CommonUtils.checkPhone("86", newNum, this)) return;
                    sendVerify();
                }
                break;
            case R.id.add_serve_time_select:
                showDatePicker();
                break;
            case R.id.add_serve_finish:
                String phone = tvPhone.getText().toString().trim();
                String name = tvName.getText().toString().trim();
                String count = tvCount.getText().toString().trim();
                String note = tvNote.getText().toString().trim();

                if (appointTime == 0 || tvCount.length() == 0) {
                    ToastUtils.ToastMessage(this, R.string.not_empty);
                    return;
                }
                if (!CommonUtils.checkPhone("86", phone, this)) return;

                NewReserveRequest reservation = new NewReserveRequest();
                reservation.linkman = name;
                reservation.phone = phone;
                reservation.personNum = count;
                reservation.note = note;
                reservation.appointTime = appointTime;
                reservation.sex = sex;
                reservation.storeSetMealId = serveId;
                if (updateArea.getVisibility() == View.VISIBLE) {
                    reservation.phone = phone;
                    reservation.vcode = vCode.getText().toString().trim();
                }
                createReserve(reservation);
                break;
        }
    }

    private void sendVerify() {
        new MainModel().doVerify(newPhone.getText().toString().trim(), new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseCodeResponse<CodeResponse>> response) {
                dealWithIdentify();
            }

            @Override
            public void onFail(int responseCode, BaseCodeResponse<CodeResponse> body) {
                if (responseCode == 400) {
                    ToastUtils.ToastMessage(BookingServeActivity.this, getString(R.string.wrong_number));
                    return;
                }
                ToastUtils.ToastMessage(BookingServeActivity.this, R.string.connect_fail);
            }
        });
    }

    private void createReserve(NewReserveRequest reservation) {
        new ReserveModel().createReserve(TribeApplication.getInstance().getUserInfo().getId(), reservation, this);
    }

    private void showCountPicker() {
        CountPickerPanel pickerPanel = new CountPickerPanel(this, new CountPickerPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String string) {
                tvCount.setText(string);
            }
        });
        pickerPanel.show();
    }

    private void showDatePicker() {
        DatePickerPanel pickerPanel = new DatePickerPanel(this, new DatePickerPanel.OnSelectedFinished() {
            @Override
            public void onSelected(long time) {
                appointTime = time;
                tvTime.setText(TribeDateUtils.dateFormat3(new Date(time)));
            }
        });
        pickerPanel.show();
    }


    @Override
    public void onResponse(Call<BaseCodeResponse<DetailReservation>> call, Response<BaseCodeResponse<DetailReservation>> response) {
        if (response.body() != null && response.body().code == 201) {
            Intent intent = new Intent(this, ReserveDetailActivity.class);
            intent.putExtra(Constant.SERVE_ID, response.body().data);
            startActivity(intent);
            finish();
        } else {
            ToastUtils.ToastMessage(this, "数据不正确");
        }
    }

    @Override
    public void onFailure(Call<BaseCodeResponse<DetailReservation>> call, Throwable t) {
        ToastUtils.ToastMessage(this, R.string.connect_fail);
    }

    private void dealWithIdentify() {
        cancel.setText("60s");
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                cancel.setClickable(false);
                cancel.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                cancel.setText("获取验证码");
                cancel.setClickable(true);
            }
        }.start();
    }
}
