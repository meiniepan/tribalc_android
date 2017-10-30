package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class ContactsDoneAdapter extends BaseQuickAdapter<ContactsPersonEntity,BaseHolder> {
    public ContactsDoneAdapter(int layoutResId, @Nullable List<ContactsPersonEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, final ContactsPersonEntity item) {
        helper.setText(R.id.tv_name, item.name).setText(R.id.tv_number, item.phone);
        SimpleDraweeView simpleDraweeView = helper.getView(R.id.sdv_icon);
        simpleDraweeView.setImageURI(item.photoUri);
    }

}
