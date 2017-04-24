/*
 * Copyright (c) 2015 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gs.buluo.app.utils.contacts;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.gs.buluo.app.R;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 列表适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 9/16/15.
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer {

    private ArrayList<Contact> datas;

    public ContactAdapter(AbsListView view, ArrayList<Contact> mDatas) {
//        super(view, mDatas, R.layout.item_list_contact);
        datas = mDatas;
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Collections.sort(datas);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        Contact item = datas.get(position);
        return item.getFirstChar();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = datas.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        holder.setText(R.id.contact_title, item.getName());
//        ImageView headImg = holder.getView(R.id.contact_head);
//        if (isScrolling) {
//            kjb.displayCacheOrDefult(headImg, item.getUrl(), R.drawable.default_head_rect);
//        } else {
//            kjb.displayWithLoadBitmap(headImg, item.getUrl(), R.drawable.default_head_rect);
//        }
//
//        TextView tvLetter = holder.getView(R.id.contact_catalog);
//        TextView tvLine = holder.getView(R.id.contact_line);
//
//        //如果是第0个那么一定显示#号
//        if (position == 0) {
//            tvLetter.setVisibility(View.VISIBLE);
//            tvLetter.setText("#");
//            tvLine.setVisibility(View.VISIBLE);
//        } else {
//
//            //如果和上一个item的首字母不同，则认为是新分类的开始
//            Contact prevData = datas.get(position - 1);
//            if (item.getFirstChar() != prevData.getFirstChar()) {
//                tvLetter.setVisibility(View.VISIBLE);
//                tvLetter.setText("" + item.getFirstChar());
//                tvLine.setVisibility(View.VISIBLE);
//            } else {
//                tvLetter.setVisibility(View.GONE);
//                tvLine.setVisibility(View.GONE);
//            }
//        }
        return null;
    }
}
