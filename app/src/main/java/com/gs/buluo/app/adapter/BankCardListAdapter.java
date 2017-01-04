package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.LoadingDialog;
import com.gs.buluo.app.view.widget.CustomAlertDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCardListAdapter extends BaseAdapter {


    private List<BankCard> datas=new ArrayList<>();
    private Context mContext;
    private BankCardHolder holder;
    private boolean showDelete =false;

    public BankCardListAdapter(Context context){
        mContext=context;
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
            convertView = holder.getHolderView();
        }else {
            holder = (BankCardHolder) convertView.getTag();
        }
        BankCard card=datas.get(position);
        holder.bankName.setText(card.bankName);
        holder.cardNum.setText(card.bankCardNum.substring(card.bankCardNum.length()-4,card.bankCardNum.length()));

        switch (card.bankName) {
            case "中国农业银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_abc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_03);
                break;
            case "中国银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_bc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "中国建设银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_ccb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "上海浦发银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_spdb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "广东发展银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_gdb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "中国光大银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_ceb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_04);
                break;
            case "中国招商银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "华夏银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_hb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "深圳发展银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_sdb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "兴业银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cib);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "民生银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmsb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "恒丰银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_egb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_04);
                break;
            case "中信银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_citic);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "中国农业发展银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_adbc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_04);
                break;
            case "中国进出口银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_eibc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_03);
                break;
        }

        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<card.bankCardNum.length()-4;i++){
            buffer.append("*");
            if (i%4==3){
                buffer.append(" ");
            }
        }
        holder.cardStar.setText(buffer.toString());
        if (showDelete){
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(datas.get(position));
                }
            });
        }else {
            holder.delete.setVisibility(View.GONE);
        }
        convertView.setTag(holder);
        return convertView;
    }

    private void showDeleteDialog(final BankCard card) {
        CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(mContext);
        builder.setTitle("确定删除?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBankCard(card);
            }
        }).setNegativeButtonText("取消");
        builder.create().show();
    }

    public void setData(List<BankCard> data) {
        this.datas = data;
        notifyDataSetChanged();
    }

    public void showDelete() {
        showDelete=true;
        notifyDataSetChanged();
    }

    public void hideDelete() {
        showDelete=false;
        notifyDataSetChanged();
    }

    private class BankCardHolder {
        public TextView bankName;
        public TextView cardStar;
        public TextView cardNum;
        public ImageView cardIcon;
        public TextView delete;
        public RelativeLayout card;

        public View getHolderView() {
            View view= LayoutInflater.from(mContext).inflate(R.layout.bank_card_item,null);
            card = ((RelativeLayout) view.findViewById(R.id.card_bg));
            bankName= (TextView) view.findViewById(R.id.card_bank_name);
            cardStar= (TextView) view.findViewById(R.id.card_number_star);
            cardNum= (TextView) view.findViewById(R.id.card_number);
            delete= (TextView) view.findViewById(R.id.card_delete);
            cardIcon= (ImageView) view.findViewById(R.id.card_icon);
            return view;
        }
    }

    private void deleteBankCard(final BankCard card) {
        LoadingDialog.getInstance().show(mContext,R.string.loading,true);
        new MoneyModel().deleteCard(card.id, new Callback<BaseCodeResponse>() {
            @Override
            public void onResponse(Call<BaseCodeResponse> call, Response<BaseCodeResponse> response) {
                if (response.body()!=null&&response.body().code==204){
                    LoadingDialog.getInstance().dismissDialog();
                    datas.remove(card);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BaseCodeResponse> call, Throwable t) {
                ToastUtils.ToastMessage(mContext,R.string.connect_fail);
            }
        });
    }
}
