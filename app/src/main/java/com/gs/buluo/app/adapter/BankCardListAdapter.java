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
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.app.view.widget.LoadingDialog;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCardListAdapter extends BaseAdapter {


    private List<BankCard> datas = new ArrayList<>();
    private Context mContext;
    private BankCardHolder holder;
    private boolean showDelete = false;

    public BankCardListAdapter(Context context) {
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
            convertView = holder.getHolderView();
        } else {
            holder = (BankCardHolder) convertView.getTag();
        }
        BankCard card = datas.get(position);
        holder.bankName.setText(card.bankName);
        holder.cardNum.setText(card.bankCardNum.substring(card.bankCardNum.length() - 4, card.bankCardNum.length()));

        switch (card.bankCode) {
            case "ICBC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_icbc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "ABC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_abc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_03);
                break;
            case "CCB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_ccb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "BOC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_bc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "BCOM":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_bcom);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "CIB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cib);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "CITIC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_citic);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "CEB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_ceb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_04);
                break;
            case "PAB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_pab);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "PSBC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_psbc);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_03);
                break;
            case "SHB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_shb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "SPDB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_spdb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "CMBC":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmsb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "CMB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_cmb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "GDB":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_gdb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "华夏银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_hb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_02);
                break;
            case "深圳发展银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_sdb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_01);
                break;
            case "恒丰银行":
                holder.cardIcon.setImageResource(R.mipmap.bank_logo_egb);
                holder.card.setBackgroundResource(R.mipmap.bank_bg_04);
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

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < card.bankCardNum.length() - 4; i++) {
            buffer.append("*");
            if (i % 4 == 3) {
                buffer.append(" ");
            }
        }
        holder.cardStar.setText(buffer.toString());
        if (showDelete) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(datas.get(position));
                }
            });
        } else {
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
        showDelete = true;
        notifyDataSetChanged();
    }

    public void hideDelete() {
        showDelete = false;
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.bank_card_item, null);
            card = ((RelativeLayout) view.findViewById(R.id.card_bg));
            bankName = (TextView) view.findViewById(R.id.card_bank_name);
            cardStar = (TextView) view.findViewById(R.id.card_number_star);
            cardNum = (TextView) view.findViewById(R.id.card_number);
            delete = (TextView) view.findViewById(R.id.card_delete);
            cardIcon = (ImageView) view.findViewById(R.id.card_icon);
            return view;
        }
    }

    private void deleteBankCard(final BankCard card) {
        LoadingDialog.getInstance().show(mContext, R.string.loading, true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                deleteCard(TribeApplication.getInstance().getUserInfo().getId(), card.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        LoadingDialog.getInstance().dismissDialog();
                        datas.remove(card);
                        notifyDataSetChanged();
                    }
                });
    }
}
