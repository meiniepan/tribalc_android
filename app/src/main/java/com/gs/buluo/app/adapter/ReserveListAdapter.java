package com.gs.buluo.app.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.transition.Explode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListReservation;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.TribeDateUtils;
import com.gs.buluo.app.view.activity.ReserveDetailActivity;
import com.gs.buluo.app.view.widget.loadMoreRecycle.BaseViewHolder;
import com.gs.buluo.app.view.widget.loadMoreRecycle.RecyclerAdapter;

import java.util.Date;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReserveListAdapter extends RecyclerAdapter<ListReservation> {
    Activity mAct;
    public ReserveListAdapter(Activity context) {
        super(context);
        mAct =context;
    }

    @Override
    public BaseViewHolder<ListReservation> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ReserveItemHolder(parent);
    }

    class ReserveItemHolder extends BaseViewHolder<ListReservation>{
        TextView tags;
        TextView status;
        TextView name;
        TextView time;
        TextView count;
        SimpleDraweeView picture;
        View itemView;
        public ReserveItemHolder(ViewGroup itemView) {
            super(itemView, R.layout.reserve_list_item);
            this.itemView=itemView;
        }

        @Override
        public void onInitializeView() {
            status =findViewById(R.id.reserve_item_status);
            name=findViewById(R.id.reserve_item_name);
            picture=findViewById(R.id.reserve_item_picture);
            tags=findViewById(R.id.reserve_item_tags);
            time=findViewById(R.id.receive_item_time);
            count=findViewById(R.id.reserve_item_people_count);
        }

        @Override
        public void setData(ListReservation entity) {
            super.setData(entity);
            if (entity==null)return;
            name.setText(entity.storeName);
            setStatus(entity);
            tags.setText(entity.markPlace);
            if (entity.tags!=null&&entity.tags.size()>0){
                tags.setText(entity.tags.get(0)+"  |  "+entity.markPlace);
            }
            time.setText(TribeDateUtils.dateFormat9(new Date(entity.appointTime)));
            count.setText(entity.personNum);
            FresoUtils.loadImage(entity.mainPicture,picture);
        }

        private void setStatus(ListReservation entity) {
            if (entity.status== ListReservation.ReserveStatus.PROCESSING){
                status.setText(R.string.reserve_processing);
            }else if (entity.status== ListReservation.ReserveStatus.SUCCEED){
                status.setText(R.string.reserve_success);
            }else {
                status.setText(R.string.reserve_fail);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemViewClick(ListReservation entity) {
            Intent intent=new Intent(mAct,ReserveDetailActivity.class);
            intent.putExtra(Constant.SERVE_ID,entity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAct.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(mAct).toBundle());
            }else {
                mAct.startActivity(intent);
            }
        }
    }
}
