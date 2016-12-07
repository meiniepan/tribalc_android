package com.gs.buluo.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListGoods;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.ResponseBody.GoodsStandardResponse;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.model.GoodsModel;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.GoodsChoosePanel;
import com.gs.buluo.app.view.widget.SwipeMenuLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/2.
 */
public class CarListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ShoppingCart> groups;
    private CheckInterface checkInter;
    private boolean isEdit;
    private UpdateInterface updateInterface;

    public CarListAdapter(Context context, List<ShoppingCart> content) {
        this.context=context;
        groups=content;
    }

    public void addCheckInterface(CheckInterface checkInter){
        this.checkInter=checkInter;
    }

    public void addGoodsChangedInterface(UpdateInterface updateInterface){
        this.updateInterface=updateInterface;
    }


    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).goodsList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).goodsList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder holder;
        if (convertView==null){
            holder=new GroupHolder();
            convertView=holder.getConvertView();
        }else {
            holder= (GroupHolder) convertView.getTag();
        }
        final ShoppingCart cart= (ShoppingCart) getGroup(groupPosition);
        holder.name.setText(cart.store.name);
        holder.check.setChecked(cart.isSelected);
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.isSelected = ((CheckBox)v).isChecked();
                holder.check.setChecked( ((CheckBox)v).isChecked());
                for (ShoppingCart.ListGoodsListItem item:cart.goodsList){
                    item.isSelected=((CheckBox)v).isChecked();
                }
                notifyDataSetChanged();
                checkInter.checkGroup(groupPosition,((CheckBox)v).isChecked());
            }
        });
        convertView.setTag(holder);
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        if (convertView==null){
            holder=new ChildHolder();
            convertView=holder.getConvertView();
        }else {
            holder= (ChildHolder) convertView.getTag();
        }
        if (isEdit){
            holder.hideView1.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.hideView3.setVisibility(View.VISIBLE);
            holder.commonView1.setVisibility(View.GONE);
            holder.commonView2.setVisibility(View.GONE);
        }else {
            holder.hideView1.setVisibility(View.GONE);
            holder.arrow.setVisibility(View.GONE);
            holder.hideView3.setVisibility(View.GONE);
            holder.commonView1.setVisibility(View.VISIBLE);
            holder.commonView2.setVisibility(View.VISIBLE);
        }

        final ShoppingCart.ListGoodsListItem itemGoods= (ShoppingCart.ListGoodsListItem) getChild(groupPosition,childPosition);

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getGoodsStandardInfo(itemGoods.goods.id,groupPosition,childPosition);
            }
        });
        holder.price.setText(itemGoods.goods.salePrice);
        holder.priceEdit.setText(itemGoods.goods.salePrice);
        holder.amount.setText(itemGoods.amount+"");
        holder.boardAmount.setText(itemGoods.amount+"");
        if (itemGoods!=null){
            holder.name.setText(itemGoods.goods.name);
            holder.check.setChecked(itemGoods.isSelected);
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemGoods.isSelected=((CheckBox)v).isChecked();
                    holder.check.setChecked(((CheckBox)v).isChecked());
                    checkInter.checkChild(groupPosition,childPosition,((CheckBox)v).isChecked());
                }
            });
        }
        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoodsStandardInfo(itemGoods.goods.id, groupPosition, childPosition);
                holder.swipeMenuLayout.quickClose();
            }
        });
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeMenuLayout.quickClose();
                showDeleteDialog(itemGoods,groupPosition);
            }
        });
        holder.addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(holder.boardAmount.getText().toString().trim());
                i+=1;
                holder.boardAmount.setText(i+"");
                itemGoods.amount=i;
            }
        });
        holder.reduceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(holder.boardAmount.getText().toString().trim());
                i-=1;
                holder.boardAmount.setText(i+"");
                itemGoods.amount=i;
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    private void showDeleteDialog(final ShoppingCart.ListGoodsListItem goods, final int groupPosition) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("确定删除?").setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<ShoppingCart.ListGoodsListItem> goodsList = ((ShoppingCart) getGroup(groupPosition)).goodsList;
                goodsList.remove(goods);
                if (goodsList.size()==0){
                    groups.remove(groupPosition);
                }
                notifyDataSetChanged();
            }
        }).setNegativeButton(context.getString(R.string.cancel),null).show();
    }

    private void getGoodsStandardInfo(String id, final int groupPosition, final int childPosition) {
        id="583423a2c164749979d0e23d";
        new GoodsModel().getGoodsStandard(id, new Callback<GoodsStandardResponse>() {
            @Override
            public void onResponse(Call<GoodsStandardResponse> call, Response<GoodsStandardResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    final GoodsChoosePanel panel=new GoodsChoosePanel(context);
                    panel.setData(response.body().data);
                    panel.setFromShoppingCar(new GoodsChoosePanel.OnSelectFinish() {
                        @Override
                        public void onSelected(ListGoodsDetail goods) {
                            panel.dismiss();
                            updateCarGoods(goods,groupPosition,childPosition);
                        }
                    });
                    panel.show();
                }
            }

            @Override
            public void onFailure(Call<GoodsStandardResponse> call, Throwable t) {
                ToastUtils.ToastMessage(context,R.string.connect_fail);
            }
        });
    }

    private void updateCarGoods(ListGoodsDetail goods, int groupPosition, int childPosition) {
//        updateInterface.onUpdate(goods);
        ListGoods g=new ListGoods();
        g.name="哈哈哈";
        g.salePrice=goods.salePrice;
        ShoppingCart.ListGoodsListItem item=groups.get(groupPosition).goodsList.get(childPosition);
        item.goods=g;
        item.amount=10;
        groups.get(groupPosition).goodsList.set(childPosition,item);
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    public void setAllChecked(boolean allChecked) {
        for (ShoppingCart cart:groups){
            for (ShoppingCart.ListGoodsListItem item :cart.goodsList){
                item.isSelected=allChecked;
                notifyDataSetChanged();
            }
        }
    }

    public class GroupHolder{
        public CheckBox check;
        public TextView name;

        public View getConvertView() {
            View view=View.inflate(context,R.layout.car_list_item,null);
            check = (CheckBox) view.findViewById(R.id.car_item_select);
            name= (TextView) view.findViewById(R.id.car_item_name);
            return view;
        }
    }

    public class ChildHolder{
        public TextView name;
        public TextView price;
        public TextView priceEdit;
        public TextView amount;
        public TextView boardAmount;
        public CheckBox check;
        public View hideView1;
        public View arrow;
        public View hideView3;
        public View commonView1;
        public View commonView2;
        public View editView;
        public View deleteView;
        public View reduceView;
        public View addView;


        public SwipeMenuLayout swipeMenuLayout;

        public View getConvertView() {
            View view=View.inflate(context,R.layout.car_item_goods_item,null);
            check = (CheckBox) view.findViewById(R.id.car_item_child_select);
            name= (TextView) view.findViewById(R.id.car_item_goods_name);
            price= (TextView) view.findViewById(R.id.car_item_good_price);
            priceEdit= (TextView) view.findViewById(R.id.car_item_good_price_edit);
            amount= (TextView) view.findViewById(R.id.car_item_amount);
            boardAmount= (TextView) view.findViewById(R.id.car_board_number);

            hideView1=view.findViewById(R.id.car_item_good_hidden_price);
            arrow =view.findViewById(R.id.car_item_good_arrow);
            hideView3=view.findViewById(R.id.car_item_good_number_board);
            commonView1=view.findViewById(R.id.car_item_good_number);
            commonView2=view.findViewById(R.id.car_item_good_common_price);

            editView=view.findViewById(R.id.car_item_edit);
            deleteView=view.findViewById(R.id.car_item_delete);
            reduceView=view.findViewById(R.id.car_board_reduce);
            addView=view.findViewById(R.id.car_board_add);

            swipeMenuLayout= (SwipeMenuLayout) view.findViewById(R.id.car_item_swipe);
            return view;
        }
    }

    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    public interface UpdateInterface{
        void onUpdate(ListGoodsDetail item);
    }


}
