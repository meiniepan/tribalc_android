package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CompanyPlate;

import java.util.List;

/**
 * Created by fs on 2016/12/9.
 */
public class CompanyPickAdapter extends BaseAdapter {

    private final List<CompanyPlate> mList;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public CompanyPickAdapter(Context context, List<CompanyPlate> list) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = mInflater.inflate(R.layout.item_pick_company, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.mTextView.setText(mList.get(position).companyName);
        return convertView;
    }

    public static class ViewHolder {

        public TextView mTextView;

        public ViewHolder(View view) {
            mTextView = ((TextView) view.findViewById(R.id.item_pick_company_name));
        }
    }

}
