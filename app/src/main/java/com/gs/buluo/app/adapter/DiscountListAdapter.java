package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.Privilege;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by hjn on 2017/7/17.
 */

public class DiscountListAdapter extends BaseAdapter {
    private Context mCtx;
    private ArrayList<Privilege> data;

    public DiscountListAdapter(Context context, ArrayList<Privilege> data) {
        mCtx = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private String currentMoney = "-1";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DiscountHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.discount_item, parent, false);
            holder = new DiscountHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DiscountHolder) convertView.getTag();
        }
        Privilege privilege = data.get(position);
        switch (privilege.type) {
            case DISCOUNT:
                holder.tvReduce.setVisibility(View.GONE);
                holder.tvResult2.setVisibility(View.GONE);
                holder.discountView.setVisibility(View.VISIBLE);
                holder.tvDiscount.setText("满"+privilege.condition+"元 "+privilege.value.multiply(new BigDecimal(10)).toString());
                if (position == currentPos) {
                    holder.button.setChecked(true);
                    holder.tvReduce.setTextColor(mCtx.getResources().getColor(R.color.common_dark));
                } else {
                    holder.button.setChecked(false);
                    holder.tvReduce.setTextColor(mCtx.getResources().getColor(R.color.common_gray));
                }
                break;
            case REDUCE:
                holder.tvReduce.setVisibility(View.VISIBLE);
                holder.discountView.setVisibility(View.GONE);
                holder.tvResult2.setText("- ¥ " + privilege.value.toString());
                holder.tvReduce.setText("满" + privilege.condition + "减" + privilege.value + "元");
                if (position == currentPos) {
                    holder.button.setChecked(true);
                    holder.tvReduce.setTextColor(mCtx.getResources().getColor(R.color.common_dark));
                    holder.tvResult2.setVisibility(View.VISIBLE);
                } else {
                    holder.button.setChecked(false);
                    holder.tvReduce.setTextColor(mCtx.getResources().getColor(R.color.common_gray));
                    holder.tvResult2.setVisibility(View.GONE);
                }
                break;
            case ALIQUOT:
                holder.tvReduce.setVisibility(View.VISIBLE);
                holder.discountView.setVisibility(View.GONE);

                BigDecimal multiple = new BigDecimal(currentMoney).divide(privilege.condition).setScale(0, BigDecimal.ROUND_DOWN);
                holder.tvReduce.setText("每满" + privilege.condition + "减" + privilege.value + "元");
                holder.tvResult2.setText("- ¥ " + privilege.value.multiply(multiple).intValue() + "");
                break;
        }
        return convertView;
    }

    private int currentPos = -1;

    public void setAmount(String value) {
        currentMoney = value;
        BigDecimal current = new BigDecimal(currentMoney);
        if (data.get(0).type == Privilege.PrivilegeType.REDUCE || data.get(0).type == Privilege.PrivilegeType.DISCOUNT) {
            if (current.compareTo(data.get(0).condition) == -1) {
                currentPos = -1;
                notifyDataSetChanged();
                return;
            }
            if (current.compareTo(data.get(data.size() - 1).condition) >= 0) {
                currentPos = data.size() - 1;
                notifyDataSetChanged();
                return;
            }
            for (int i = 0; i < data.size(); i++) {
                if (i == 0 && current.compareTo(data.get(0).condition) == -1) {
                    currentPos = -1;
                } else if (i == 0 && current.compareTo(data.get(0).condition) > -1) {
                    currentPos = 0;
                } else if (current.compareTo(data.get(i - 1).condition) == 1 && current.compareTo(data.get(i).condition) == -1) {
                    currentPos = i - 1;
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    private class DiscountHolder {
        View discountView;
        TextView tvDiscount;
        TextView tvResult2;
        TextView tvReduce;
        RadioButton button;

        public DiscountHolder(View convertView) {
            button = (RadioButton) convertView.findViewById(R.id.discount_item_button);
            discountView = convertView.findViewById(R.id.discount_item_discount_type);
            tvDiscount = (TextView) convertView.findViewById(R.id.discount_item_discount);
            tvResult2 = (TextView) convertView.findViewById(R.id.discount_item_third);
            tvReduce = (TextView) convertView.findViewById(R.id.discount_item_first);
        }
    }
}
