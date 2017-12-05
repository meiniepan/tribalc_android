package com.gs.buluo.app.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;
import java.util.Locale;

/**
 * Created by Solang on 2017/10/24.
 */

public class ContactsAdapter extends BaseQuickAdapter<ContactsPersonEntity, BaseHolder> {
    private  int commonContactSize;
    public ContactsAdapter(int layoutResId, @Nullable List<ContactsPersonEntity> data, int size) {
        super(layoutResId, data);
        commonContactSize = size;
    }

    @Override
    protected void convert(BaseHolder helper, final ContactsPersonEntity item) {
        TextView tag = helper.getView(R.id.tv_tag);
        if (commonContactSize == 0){
            if (helper.getAdapterPosition() == 0){
                tag.setVisibility(View.VISIBLE);
                tag.setText("手机联系人");
            }else {
                tag.setVisibility(View.GONE);
            }
        }else if (commonContactSize > 0){
            if (helper.getAdapterPosition() == 0){
                tag.setVisibility(View.VISIBLE);
                tag.setText("常用联系人");
            }else if (helper.getAdapterPosition() == commonContactSize){
                tag.setVisibility(View.VISIBLE);
                tag.setText("手机联系人");
            }else {
                tag.setVisibility(View.GONE);
            }
        }
        helper.setText(R.id.tv_name, item.name).setText(R.id.tv_number, item.phone);
        CheckBox checkBox = helper.getView(R.id.cb_check);
        if (checkBox != null) {
            checkBox.setChecked(item.checked);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.checked = !item.checked;
                }
            });
        }
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        if (section == "☆".charAt(0)){
            return 0;
        }else {

            for (int i = 0; i < getItemCount(); i++) {
                String sortStr = getItem(i).sortLetters;
                char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }
        return -1;
    }
}
