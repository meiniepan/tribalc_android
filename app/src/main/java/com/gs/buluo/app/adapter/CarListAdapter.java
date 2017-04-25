package com.gs.buluo.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CartItem;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.RequestBodyBean.ShoppingCartGoodsItem;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ShoppingCart;
import com.gs.buluo.app.network.GoodsApis;
import com.gs.buluo.app.network.ShoppingApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.activity.GoodsDetailActivity;
import com.gs.buluo.app.view.widget.SwipeMenuLayout;
import com.gs.buluo.app.view.widget.panel.GoodsChoosePanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.CustomAlertDialog;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/2.
 */
public class CarListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ShoppingCart> cartList;
    private CheckInterface checkInter;
    private boolean isEdit;
    private UpdateInterface updateInterface;

    public CarListAdapter(Context context, List<ShoppingCart> content) {
        this.context = context;
        cartList = content;
    }

    public void addCheckInterface(CheckInterface checkInter) {
        this.checkInter = checkInter;
    }

    public void addGoodsChangedInterface(UpdateInterface updateInterface) {
        this.updateInterface = updateInterface;
    }


    @Override
    public int getGroupCount() {
        return cartList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return cartList.get(groupPosition).goodsList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cartList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return cartList.get(groupPosition).goodsList.get(childPosition);
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
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = holder.getConvertView();
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        final ShoppingCart cart = (ShoppingCart) getGroup(groupPosition);
        holder.name.setText(cart.store.name);
        holder.check.setChecked(cart.isSelected);
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.isSelected = ((CheckBox) v).isChecked();
                holder.check.setChecked(((CheckBox) v).isChecked());
                for (CartItem item : cart.goodsList) {
                    item.isSelected = ((CheckBox) v).isChecked();
                }
                notifyDataSetChanged();
                checkInter.checkGroup(groupPosition, ((CheckBox) v).isChecked());
            }
        });
        convertView.setTag(holder);
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder holder;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = holder.getConvertView();
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        if (isEdit) {
            holder.hideView1.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.hideView3.setVisibility(View.VISIBLE);
            holder.commonView1.setVisibility(View.GONE);
            holder.commonView2.setVisibility(View.GONE);
        } else {
            holder.hideView1.setVisibility(View.GONE);
            holder.arrow.setVisibility(View.GONE);
            holder.hideView3.setVisibility(View.GONE);
            holder.commonView1.setVisibility(View.VISIBLE);
            holder.commonView2.setVisibility(View.VISIBLE);
        }

        final CartItem itemGoods = (CartItem) getChild(groupPosition, childPosition);

        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoodsStandardInfo(itemGoods.standardId, groupPosition, childPosition, itemGoods.amount);
            }
        });
        holder.price.setText(itemGoods.goods.salePrice);
        holder.priceEdit.setText(itemGoods.goods.salePrice);
        holder.amount.setText(itemGoods.amount + "");
        holder.boardAmount.setText(itemGoods.amount + "");

        String mainPicture = itemGoods.goods.mainPicture;
        if (holder.pictrue.getTag() == null || !TextUtils.equals(mainPicture, holder.pictrue.getTag().toString())) {
            holder.pictrue.setTag(mainPicture);
            FresoUtils.loadImage(mainPicture, holder.pictrue);
        }

        if (itemGoods.goods.standardSnapshot != null) {
            String[] arr1 = itemGoods.goods.standardSnapshot.split("\\|");
            if (arr1.length > 1) {
                holder.key1.setText(arr1[0].split(":")[0]);
                holder.value1.setText(arr1[0].split(":")[1]);
//                holder.sizeKey.setText(arr1[1].split(":")[0]);
                holder.value2.setText(arr1[1].split(":")[1]);
            } else {
                holder.key1.setText(itemGoods.goods.standardSnapshot.split(":")[0]);
                holder.value1.setText(itemGoods.goods.standardSnapshot.split(":")[1]);
                holder.value2.setVisibility(View.GONE);
                holder.colon.setVisibility(View.GONE);
                holder.arrow.setVisibility(View.GONE);
            }
        } else {
            holder.key1.setVisibility(View.GONE);
            holder.value1.setVisibility(View.GONE);
            holder.value2.setVisibility(View.GONE);
            holder.colon.setVisibility(View.GONE);
            holder.arrow.setVisibility(View.GONE);
        }
        holder.name.setText(itemGoods.goods.name);
        holder.check.setChecked(itemGoods.isSelected);
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemGoods.isSelected = ((CheckBox) v).isChecked();
                holder.check.setChecked(((CheckBox) v).isChecked());
                checkInter.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());
            }
        });
        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoodsStandardInfo(itemGoods.standardId, groupPosition, childPosition, itemGoods.amount);
                holder.swipeMenuLayout.quickClose();
            }
        });
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeMenuLayout.quickClose();
                showDeleteDialog(itemGoods, groupPosition);
            }
        });
        holder.addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(holder.boardAmount.getText().toString().trim());
                i += 1;
                updateCarGoods(itemGoods.goods.id, i, groupPosition, childPosition);
            }
        });
        holder.reduceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(holder.boardAmount.getText().toString().trim());
                i -= 1;
                updateCarGoods(itemGoods.goods.id, i, groupPosition, childPosition);
            }
        });

        holder.jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) return;
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_ID, itemGoods.goods.id);
                context.startActivity(intent);
            }
        });

        convertView.setTag(holder);
        return convertView;
    }

    private void showDeleteDialog(final CartItem goods, final int groupPosition) {
        CustomAlertDialog.Builder builder = new CustomAlertDialog.Builder(context);
        builder.setTitle("确定删除?").setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGoods(groupPosition, goods);
            }
        }).setNegativeButton(context.getString(R.string.cancel), null).create().show();
    }

    private void deleteGoods(final int groupPosition, final CartItem goods) {
        String ids = goods.id;
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).deleteCart(TribeApplication.getInstance().getUserInfo().getId(), ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        List<CartItem> goodsList = ((ShoppingCart) getGroup(groupPosition)).goodsList;
                        goodsList.remove(goods);
                        if (goodsList.size() == 0) {
                            cartList.remove(groupPosition);
                        }
                        notifyDataSetChanged();
                        if (goods.isSelected) {
                            updateInterface.onUpdate();
                        }
                        if (cartList.size() == 0) {
                            updateInterface.onUpdate();
                        }
                    }
                });
    }

    private void getGoodsStandardInfo(String standardId, final int groupPosition, final int childPosition, int amount) {
        final GoodsChoosePanel panel = new GoodsChoosePanel(context, null);
        CartItem item = cartList.get(groupPosition).goodsList.get(childPosition);
        ListGoodsDetail detail = new ListGoodsDetail();
        detail.id = item.goods.id;
        detail.salePrice = item.goods.salePrice;
        detail.mainPicture = item.goods.mainPicture;
        detail.repertory = item.repertory;
        panel.setRepertory(detail);
        panel.setAmount(amount);
        if (standardId == null) panel.setData(null);
        panel.setFromShoppingCar(new GoodsChoosePanel.OnSelectFinish() {
            @Override
            public void onSelected(String newId, int nowNum) {
                panel.dismiss();
                updateCarGoods(newId, nowNum, groupPosition, childPosition);
            }
        });
        panel.show();

        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsStandard(standardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodsStandard>>() {
                    @Override
                    public void onNext(BaseResponse<GoodsStandard> response) {
                        panel.setData(response.data);
                    }
                });
    }

    private void updateCarGoods(String newId, int nowNum, final int groupPosition, final int childPosition) {
        ShoppingCartGoodsItem body = new ShoppingCartGoodsItem();
        final CartItem item = cartList.get(groupPosition).goodsList.get(childPosition);
        body.amount = nowNum;
        body.shoppingCartGoodsId = item.id;
        body.newGoodsId = newId;
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                updateCartItem(TribeApplication.getInstance().getUserInfo().getId(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CartItem>>() {
                    @Override
                    public void onNext(BaseResponse<CartItem> response) {
                        CartItem newItem = response.data;
                        item.goods = newItem.goods;
                        item.amount = newItem.amount;
                        if (item.amount == 0) {
                            showDeleteDialog(item, groupPosition);
                        } else {
                            cartList.get(groupPosition).goodsList.remove(childPosition);
                            cartList.get(groupPosition).goodsList.add(childPosition, item);
                            notifyDataSetChanged();
                            if (item.isSelected) {
                                updateInterface.onUpdate();
                            }
                        }
                    }
                });
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
        for (ShoppingCart cart : cartList) {
            for (CartItem item : cart.goodsList) {
                item.isSelected = allChecked;
                notifyDataSetChanged();
            }
            cart.isSelected = allChecked;
        }
    }

    public class GroupHolder {
        public CheckBox check;
        public TextView name;

        public View getConvertView() {
            View view = View.inflate(context, R.layout.car_list_item, null);
            check = (CheckBox) view.findViewById(R.id.car_item_select);
            name = (TextView) view.findViewById(R.id.car_item_name);
            return view;
        }
    }

    public class ChildHolder {
        public TextView name;
        public TextView price;
        public TextView priceEdit;
        public TextView amount;
        public TextView boardAmount;
        public TextView key1;
        public TextView value1;
        public TextView value2;
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
        public View colon;
        public View jump;
        public SimpleDraweeView pictrue;
//        public ImageView pictrue;

        public SwipeMenuLayout swipeMenuLayout;

        public View getConvertView() {
            View view = View.inflate(context, R.layout.car_item_goods_item, null);
            check = (CheckBox) view.findViewById(R.id.car_item_child_select);
            name = (TextView) view.findViewById(R.id.car_item_goods_name);
            price = (TextView) view.findViewById(R.id.car_item_good_price);
            priceEdit = (TextView) view.findViewById(R.id.car_item_good_price_edit);
            amount = (TextView) view.findViewById(R.id.car_item_amount);
            boardAmount = (TextView) view.findViewById(R.id.car_board_number);
            pictrue = (SimpleDraweeView) view.findViewById(R.id.car_item_picture);
            jump = view.findViewById(R.id.car_item_jump_view);

            hideView1 = view.findViewById(R.id.car_item_good_hidden_price);
            arrow = view.findViewById(R.id.car_item_good_arrow);
            hideView3 = view.findViewById(R.id.car_item_good_number_board);
            commonView1 = view.findViewById(R.id.car_item_good_number);
            commonView2 = view.findViewById(R.id.car_item_good_common_price);

            editView = view.findViewById(R.id.car_item_edit);
            deleteView = view.findViewById(R.id.car_item_delete);
            reduceView = view.findViewById(R.id.car_board_reduce);
            addView = view.findViewById(R.id.car_board_add);
            colon = view.findViewById(R.id.car_item_sigh);

            swipeMenuLayout = (SwipeMenuLayout) view.findViewById(R.id.car_item_swipe);

            key1 = (TextView) view.findViewById(R.id.car_item_colorKey1);
            value2 = (TextView) view.findViewById(R.id.car_item_value2);
            value1 = (TextView) view.findViewById(R.id.car_item_color_value1);
            return view;
        }
    }

    public interface CheckInterface {
        void checkGroup(int groupPosition, boolean isChecked);

        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    public interface UpdateInterface {
        void onUpdate();
    }
}
