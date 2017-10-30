package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class ContactsAdapter extends BaseQuickAdapter<ContactsPersonEntity, BaseHolder> {
    public ContactsAdapter(int layoutResId, @Nullable List<ContactsPersonEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, final ContactsPersonEntity item) {
        helper.setText(R.id.tv_name, item.name).setText(R.id.tv_number, item.phone);
        CheckBox checkBox = helper.getView(R.id.cb_check);
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.checked = isChecked;
                }
            });
        }
    }

}
