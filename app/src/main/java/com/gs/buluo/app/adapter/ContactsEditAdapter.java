package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class ContactsEditAdapter extends BaseQuickAdapter<ContactsPersonEntity,BaseHolder> {
    public ContactsEditAdapter(int layoutResId, @Nullable List<ContactsPersonEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, final ContactsPersonEntity item) {
        helper.setText(R.id.tv_name, item.name).setText(R.id.tv_number, item.phone);
        CheckBox checkBox = helper.getView(R.id.cb_check);
        checkBox.setChecked(item.deleted);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.deleted = !item.deleted;
                }
            });
    }

}
