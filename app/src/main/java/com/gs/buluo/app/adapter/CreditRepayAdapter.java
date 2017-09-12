package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankCardBindTypeEnum;

import java.util.List;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditRepayAdapter extends BaseAdapter {
    Context mCtx;
    List<BankCard> list;
    private int pos = -1;

    public CreditRepayAdapter(Context context, List<BankCard> list) {
        mCtx = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private BankCardHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new BankCardHolder();
            convertView = holder.getHolderView(parent);
        } else {
            holder = (BankCardHolder) convertView.getTag();
        }
        BankCard card = list.get(position);
        if (!(card.bindType == BankCardBindTypeEnum.NORMAL)) {
            holder.cantPayFlag.setVisibility(View.VISIBLE);
            holder.bankName.setTextColor(mCtx.getResources().getColor(R.color.common_gray));
            holder.cardType.setTextColor(mCtx.getResources().getColor(R.color.common_gray));
            holder.leftBracket.setTextColor(mCtx.getResources().getColor(R.color.common_gray));
            holder.cardNum.setTextColor(mCtx.getResources().getColor(R.color.common_gray));
            holder.rightBracket.setTextColor(mCtx.getResources().getColor(R.color.common_gray));
        } else {
            holder.cantPayFlag.setVisibility(View.GONE);
            holder.bankName.setTextColor(mCtx.getResources().getColor(R.color.common_dark));
            holder.cardType.setTextColor(mCtx.getResources().getColor(R.color.common_dark));
            holder.leftBracket.setTextColor(mCtx.getResources().getColor(R.color.common_dark));
            holder.cardNum.setTextColor(mCtx.getResources().getColor(R.color.common_dark));
            holder.rightBracket.setTextColor(mCtx.getResources().getColor(R.color.common_dark));
        }
        holder.bankName.setText(card.bankName);
        holder.cardNum.setText(card.bankCardNum.substring(card.bankCardNum.length() - 4, card.bankCardNum.length()));
        if (position == pos) {
            holder.wayButton.setChecked(true);
        } else {
            holder.wayButton.setChecked(false);
        }
        setIcon(card);
        convertView.setTag(holder);
        return convertView;
    }

    public void setIcon(BankCard card) {
        switch (card.bankCode) {
            case "BOB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_bob);
                break;
            case "ICBC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_icbc);
                break;
            case "ABC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_abc);
                break;
            case "CCB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_ccb);
                break;
            case "BOC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_boc);
                break;
            case "BCOM":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_bcom);
                break;
            case "CIB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cib);
                break;
            case "CITIC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_citic);
                break;
            case "CEB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_ceb);
                break;
            case "PAB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_pab);
                break;
            case "PSBC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_psbc);
                break;
            case "SHB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_shb);
                break;
            case "SPDB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_spdb);
                break;
            case "CMBC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmsb);
                break;
            case "CMB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmb);
                break;
            case "GDB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_gdb);
                break;
        }
    }

    public void setPos(int pos) {
        this.pos = pos;
        notifyDataSetChanged();
    }

    private class BankCardHolder {
        public TextView bankName;
        public TextView cardNum;
        public ImageView cardIcon;
        public RadioButton wayButton;
        public TextView rightBracket;
        public TextView leftBracket;
        public TextView cantPayFlag;
        public TextView cardType;

        public View getHolderView(ViewGroup parent) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.lite_bank_card_item, parent, false);
            bankName = (TextView) view.findViewById(R.id.card_bank_name);
            cardNum = (TextView) view.findViewById(R.id.card_number);
            cardIcon = (ImageView) view.findViewById(R.id.card_icon);
            wayButton = (RadioButton) view.findViewById(R.id.recharge_pay_order);
            cantPayFlag = (TextView) view.findViewById(R.id.tv_cant_pay_flag);
            leftBracket = (TextView) view.findViewById(R.id.tv_left_bracket);
            rightBracket = (TextView) view.findViewById(R.id.tv_right_bracket);
            cardType = (TextView) view.findViewById(R.id.card_type);
            return view;
        }
    }
}
