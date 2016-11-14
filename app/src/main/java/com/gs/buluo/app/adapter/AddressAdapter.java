package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.presenter.AddressPresenter;
import com.gs.buluo.app.view.activity.AddAddressActivity;
import com.gs.buluo.app.view.activity.DetailAddressActivity;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressAdapter extends  RecyclerView.Adapter<AddressAdapter.AddressHolder>{
    private  List<UserAddressEntity> mDatas;
    private String defaultAddressID;
    private DetailAddressActivity mCtx;

    public AddressAdapter(DetailAddressActivity context, List<UserAddressEntity> datas) {
        mCtx=context;
        mDatas = datas;
        defaultAddressID = new UserSensitiveDao().findFirst().getAddressID();
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.address_item, null);
        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, final int position) {
        final UserAddressEntity entity = mDatas.get(position);
        holder.name.setText(entity.getName());
        holder.address.setText(entity.getArea()+entity.getDetailAddress());
        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AddAddressActivity.class);
                intent.putExtra(Constant.ADDRESS,entity);
                mCtx.startActivity(intent);
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCtx.getAddressPresenter().deleteAddress(TribeApplication.getInstance().getUserInfo().getId(),mDatas.get(position).getId());
            }
        });

        if (entity.getId().equals(defaultAddressID)){
            holder.mSelect.setImageResource(R.mipmap.address_selected);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

     class AddressHolder extends RecyclerView.ViewHolder  {
         TextView name;
         TextView address;
         ImageView mSelect;
         ImageView mEdit;
         ImageView mDelete;
        public AddressHolder(View view) {
            super(view);
            name= (TextView) view.findViewById(R.id.add_address_name);
            address= (TextView) view.findViewById(R.id.add_address_detail);
            mSelect= (ImageView) view.findViewById(R.id.add_address_select_icon);
            mEdit= (ImageView) view.findViewById(R.id.add_address_edit);
            mDelete= (ImageView) view.findViewById(R.id.add_address_delete);
        }
    }
}
