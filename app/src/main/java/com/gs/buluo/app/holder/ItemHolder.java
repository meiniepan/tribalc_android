package com.gs.buluo.app.holder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;


public class ItemHolder extends CustomHolder<String> {
    private AlertDialog dialog = null;
    public ItemHolder(View itemView) {
        super(itemView);
    }

    public ItemHolder(List datas, View itemView) {
        super(datas, itemView);
    }

    public ItemHolder(Context context, List lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(int position, List datas, Context context) {
        TextView textView = (TextView) itemView.findViewById(R.id.tv);
        TextView textView2 = (TextView) itemView.findViewById(R.id.tv0);
        final String msg = (String) datas.get(position);
        textView.setText(msg);
        textView.setHeight(300);
        textView.setPadding(20, 10, 20, 10);
        if (position % 2 == 0) {
            textView.setBackgroundColor(Color.parseColor("#abcdef"));
        } else {
            textView.setBackgroundColor(Color.parseColor("#fedcba"));
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.ToastMessage(getContext(),msg);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }
    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.myCorDialog1);
        View view = View
                .inflate(getContext(), R.layout.main_frag_list_button, null);
        builder.setView(view);
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = CommonUtils.getScreenWidth(getContext())-40;
        params.height = DensityUtils.dip2px(getContext(), 80);
        dialog.getWindow().setAttributes(params);
    }
}
