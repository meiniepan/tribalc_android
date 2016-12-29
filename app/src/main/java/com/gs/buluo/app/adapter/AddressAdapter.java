package com.gs.buluo.app.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.view.activity.AddAddressActivity;
import com.gs.buluo.app.view.activity.AddressListActivity;
import com.gs.buluo.app.view.widget.CustomAlertDialog;

import java.util.List;

/**
 * Created by hjn on 2016/11/14.
 */
public class AddressAdapter extends  RecyclerView.Adapter<AddressAdapter.AddressHolder>{
    private  List<UserAddressEntity> mDatas;
    private String defaultAddressID;
    private AddressListActivity mCtx;
    private final  int REQUEST_UPDATE= 201;
    private boolean fromOrder;

    public AddressAdapter(AddressListActivity context, List<UserAddressEntity> datas) {
        mCtx=context;
        mDatas = datas;
        UserSensitiveEntity first = new UserSensitiveDao().findFirst();
        defaultAddressID = first.getAddressID();
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.address_item, parent,false);
        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddressHolder holder, int position) {
        final UserAddressEntity entity = mDatas.get(position);
        holder.name.setText(entity.getName());
        holder.address.setText(entity.getArea()+entity.getAddress());
        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AddAddressActivity.class);
                intent.putExtra(Constant.ADDRESS,entity);
                mCtx.startActivityForResult(intent,REQUEST_UPDATE);
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(entity);
            }
        });

        if (entity!=null&&entity.getId().equals(defaultAddressID)){
            holder.mSelect.setImageResource(R.mipmap.address_selected);
        }else {
            holder.mSelect.setImageResource(R.mipmap.address_normal);
        }
        holder.mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity!=null&&!entity.getId().equals(defaultAddressID)){
                    mCtx.getAddressPresenter().updateDefaultAddress(entity);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromOrder){
                    Intent intent=new Intent();
                    intent.putExtra(Constant.ADDRESS,entity.getArea()+entity.getAddress());
                    intent.putExtra(Constant.RECEIVER,entity.getName());
                    intent.putExtra(Constant.PHONE,entity.getPhone());
                    intent.putExtra(Constant.ADDRESS_ID,entity.getId());
                    mCtx.setResult(Activity.RESULT_OK,intent);
                    mCtx.finish();
                }
            }
        });
    }

    private void showDeleteDialog(final UserAddressEntity entity) {
        new CustomAlertDialog.Builder(mCtx).setTitle("确定删除?").setPositiveButton(mCtx.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCtx.getAddressPresenter().deleteAddress(TribeApplication.getInstance().getUserInfo().getId(),entity);
            }
        }).setNegativeButton(mCtx.getString(R.string.cancel),null).create().show();
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(List<UserAddressEntity> datas) {
        this.mDatas = datas;
    }

    public void setFromOrder(boolean fromOrder) {
        this.fromOrder = fromOrder;
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

    public void setDefaultAddressID(String id){
        defaultAddressID=id;
    }
}
