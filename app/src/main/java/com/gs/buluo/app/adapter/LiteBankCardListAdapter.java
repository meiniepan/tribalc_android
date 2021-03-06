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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Solang on 2017/4/25.
 */

public class LiteBankCardListAdapter extends BaseAdapter {


    private List<BankCard> datas = new ArrayList<>();
    private Context mContext;
    private BankCardHolder holder;
    private int pos = -1;

    public LiteBankCardListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new BankCardHolder();
            convertView = holder.getHolderView(parent);
        } else {
            holder = (BankCardHolder) convertView.getTag();
        }
        BankCard card = datas.get(position);
        if (!(card.bindType == BankCardBindTypeEnum.NORMAL)) {
            holder.cantPayFlag.setText(R.string.bank_card_not_supported);
            holder.bankName.setTextColor(mContext.getResources().getColor(R.color.common_gray));
            holder.cardType.setTextColor(mContext.getResources().getColor(R.color.common_gray));
            holder.leftBracket.setTextColor(mContext.getResources().getColor(R.color.common_gray));
            holder.cardNum.setTextColor(mContext.getResources().getColor(R.color.common_gray));
            holder.rightBracket.setTextColor(mContext.getResources().getColor(R.color.common_gray));
        } else {
            holder.cantPayFlag.setText("该卡单笔最高消费金额" + (card.maxPaymentAmount == 0 ? 10000 : card.maxPaymentAmount) + "元");
            holder.bankName.setTextColor(mContext.getResources().getColor(R.color.common_dark));
            holder.cardType.setTextColor(mContext.getResources().getColor(R.color.common_dark));
            holder.leftBracket.setTextColor(mContext.getResources().getColor(R.color.common_dark));
            holder.cardNum.setTextColor(mContext.getResources().getColor(R.color.common_dark));
            holder.rightBracket.setTextColor(mContext.getResources().getColor(R.color.common_dark));
        }
        holder.bankName.setText(card.bankName);
        holder.cardNum.setText(card.bankCardNum.substring(card.bankCardNum.length() - 4, card.bankCardNum.length()));
        holder.leftBracket.setText("(");
        holder.rightBracket.setText(")");
        holder.cardType.setText("储蓄卡");
        if (card.bankCode == null) {
            holder.cardIcon.setImageResource(R.mipmap.bank_logo_default);
            card.bankIcon = R.mipmap.bank_logo_default;
        } else {
            switch (card.bankCode) {
                case "BOB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_bob);
                    card.bankIcon = R.mipmap.bank_logo_bob;
                    break;
                case "ICBC":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_icbc);
                    card.bankIcon = R.mipmap.bank_logo_icbc;
                    break;
                case "ABC":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_abc);
                    card.bankIcon = R.mipmap.bank_logo_abc;
                    break;
                case "CCB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_ccb);
                    card.bankIcon = R.mipmap.bank_logo_ccb;
                    break;
                case "BOC":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_boc);
                    card.bankIcon = R.mipmap.bank_logo_boc;
                    break;
                case "BCOM":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_bcom);
                    card.bankIcon = R.mipmap.bank_logo_bcom;
                    break;
                case "CIB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_cib);
                    card.bankIcon = R.mipmap.bank_logo_cib;
                    break;
                case "CITIC":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_citic);
                    card.bankIcon = R.mipmap.bank_logo_citic;
                    break;
                case "CEB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_ceb);
                    card.bankIcon = R.mipmap.bank_logo_ceb;
                    break;
                case "PAB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_pab);
                    card.bankIcon = R.mipmap.bank_logo_pab;
                    break;
                case "PSBC":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_psbc);
                    card.bankIcon = R.mipmap.bank_logo_psbc;
                    break;
                case "SHB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_shb);
                    card.bankIcon = R.mipmap.bank_logo_shb;
                    break;
                case "SPDB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_spdb);
                    card.bankIcon = R.mipmap.bank_logo_spdb;
                    break;
                case "CMBC":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmsb);
                    card.bankIcon = R.mipmap.bank_logo_cmsb;
                    break;
                case "CMB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmb);
                    card.bankIcon = R.mipmap.bank_logo_cmb;
                    break;
                case "GDB":
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_gdb);
                    card.bankIcon = R.mipmap.bank_logo_gdb;
                    break;
                default:
                    holder.cardIcon.setImageResource(R.mipmap.bank_logo_default);
                    card.bankIcon = R.mipmap.bank_logo_default;
                    break;
//            case "华夏银行":
//                holder.cardIcon.setImageResource(R.mipmap.bank_logo_hb);
//                break;
//            case "深圳发展银行":
//                holder.cardIcon.setImageResource(R.mipmap.bank_logo_sdb);
//                break;
//            case "恒丰银行":
//                holder.cardIcon.setImageResource(R.mipmap.bank_logo_egb);
//                break;
//            case "中国农业发展银行":
//                holder.cardIcon.setImageResource(R.mipmap.bank_logo_adbc);
//                break;
//            case "中国进出口银行":
//                holder.cardIcon.setImageResource(R.mipmap.bank_logo_eibc);
//                break;
            }
        }
        if (pos == position) {
            holder.PayOrder.setChecked(true);
        } else {
            holder.PayOrder.setChecked(false);
        }

        convertView.setTag(holder);
        return convertView;
    }


    public void setData(List<BankCard> data) {
        this.datas = data;
        notifyDataSetChanged();
    }

    public void setPos(int pos) {
        this.pos = pos;
        notifyDataSetChanged();
    }


    private class BankCardHolder {
        public TextView bankName;
        public TextView cardNum;
        public ImageView cardIcon;
        public TextView rightBracket;
        public TextView leftBracket;
        public TextView cantPayFlag;
        public TextView cardType;
        public RadioButton PayOrder;

        public View getHolderView(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.lite_bank_card_item, parent, false);
            cardType = (TextView) view.findViewById(R.id.card_type);
            bankName = (TextView) view.findViewById(R.id.card_bank_name);
            cardNum = (TextView) view.findViewById(R.id.card_number);
            cardIcon = (ImageView) view.findViewById(R.id.card_icon);
            cantPayFlag = (TextView) view.findViewById(R.id.tv_cant_pay_flag);
            leftBracket = (TextView) view.findViewById(R.id.tv_left_bracket);
            rightBracket = (TextView) view.findViewById(R.id.tv_right_bracket);
            PayOrder = (RadioButton) view.findViewById(R.id.recharge_pay_order);
            return view;
        }
    }


}
