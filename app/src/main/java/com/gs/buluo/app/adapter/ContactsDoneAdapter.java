package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.view.widget.SwipeMenuLayout;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class ContactsDoneAdapter extends BaseQuickAdapter<ContactsPersonEntity, BaseHolder> {
    private final ArrayList<ContactsPersonEntity> inputData;
    List<ContactsPersonEntity> data;
    private boolean swipeEnable = true;

    public ContactsDoneAdapter(int layoutResId, @Nullable List<ContactsPersonEntity> data, ArrayList<ContactsPersonEntity> inputData) {
        super(layoutResId, data);
        this.data = data;
        this.inputData = inputData;
    }

    @Override
    protected void convert(final BaseHolder helper, final ContactsPersonEntity item) {
        if (!swipeEnable) {
            SwipeMenuLayout sml = helper.getView(R.id.sml_layout);
            sml.setSwipeEnable(false);
        }
        helper.setText(R.id.tv_name, item.name).setText(R.id.tv_number, item.phone);
        helper.getView(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(item);
                ContactsPersonEntity entity = null;
                for (ContactsPersonEntity e :inputData
                        ) {
                    if (e.phone.equals(item.phone)){
                        entity = e;
                        break;
                    }
                }
                if (entity != null) inputData.remove(entity);
                notifyItemRemoved(helper.getAdapterPosition());
            }
        });
    }

    public void setSwipeEnable(boolean b) {
        swipeEnable = b;
    }
}
