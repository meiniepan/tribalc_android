package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.activity.BillDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hjn on 2016/11/17.
 */
public class BillListAdapter extends RecyclerAdapter<BillEntity> {
    private long today;
    private String currentMonth;
    Context context;
    public BillListAdapter(Context context, List<BillEntity> list) {
        super(context,list);
        this.context=context;
        today= System.currentTimeMillis();
        currentMonth= TribeDateUtils.dateFormat5(new Date(today)).split("-")[1];
    }

    @Override
    public BaseViewHolder<BillEntity> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BillHolder(parent,R.layout.bill_list_item);
    }

    class BillHolder extends BaseViewHolder<BillEntity>{
        TextView week;
        TextView time;
        TextView money;
        TextView detail;
        TextView month;

        public BillHolder(ViewGroup itemView, int res) {
            super(itemView,res);
        }
        @Override
        public void onInitializeView() {
            super.onInitializeView();
            week = findViewById(R.id.bill_week);
            time = findViewById(R.id.bill_time);
            money = findViewById(R.id.bill_money);
            detail = findViewById(R.id.bill_detail);
            month = findViewById(R.id.bill_item_month);
        }

        @Override
        public void setData(BillEntity entity) {
            super.setData(entity);
            long createTime = Long.parseLong(entity.createTime);
            Date date = new Date(createTime);

            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            int w=instance.get(Calendar.DAY_OF_WEEK);

            String s = TribeDateUtils.dateFormat5(date);
            week.setText(w+"");

            if (TribeDateUtils.getTimeIntervalByDay(createTime,today)<1){
                week.setText(R.string.today);
            }

            time.setText(TribeDateUtils.dateFormat6(date));
            money.setText(entity.amount);
            detail.setText(entity.title);
            String newMonth = s.split("-")[1];
            month.setText(newMonth+"月");
            if (TextUtils.equals(newMonth,currentMonth)){
                month.setVisibility(View.VISIBLE);
                month.setText(newMonth+"月");
                currentMonth=Integer.parseInt(currentMonth)+1+"";
            }else {
                month.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemViewClick(BillEntity entity) {
            super.onItemViewClick(entity);
            Intent intent=new Intent(context,BillDetailActivity.class);
            intent.putExtra(Constant.BILL,entity);
            context.startActivity(intent);
        }
    }
}
