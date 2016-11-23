package com.gs.buluo.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.platform.comapi.map.E;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.LoadingDialog;

import org.w3c.dom.Text;

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
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("确定删除?").setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBankCard(card);
            }
        }).show();
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

        public View getHolderView() {
            View view= LayoutInflater.from(mContext).inflate(R.layout.bank_card_item,null);
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
        new MoneyModel().deleteCard(card.id, new Callback<SimpleCodeResponse>() {
            @Override
            public void onResponse(Call<SimpleCodeResponse> call, Response<SimpleCodeResponse> response) {
                if (response.body()!=null&&response.body().code==204){
                    LoadingDialog.getInstance().dismissDialog();
                    datas.remove(card);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SimpleCodeResponse> call, Throwable t) {
                ToastUtils.ToastMessage(mContext,R.string.connect_fail);
            }
        });

    }
}
