package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCart implements Parcelable {
    public String id;
    public List<ListGoodsListItem> goodsList;
    public MarkStore store;
    public boolean isSelected;

    public static class ListGoodsListItem implements Parcelable {
        public int amount;
        public ListGoods goods;
        public boolean isSelected;
        public boolean isEdit;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.amount);
            dest.writeSerializable(this.goods);
            dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
        }

        public ListGoodsListItem() {
        }

        protected ListGoodsListItem(Parcel in) {
            this.amount = in.readInt();
            this.goods = (ListGoods) in.readSerializable();
            this.isSelected = in.readByte() != 0;
            this.isEdit = in.readByte() != 0;
        }

        public static final Creator<ListGoodsListItem> CREATOR = new Creator<ListGoodsListItem>() {
            @Override
            public ListGoodsListItem createFromParcel(Parcel source) {
                return new ListGoodsListItem(source);
            }

            @Override
            public ListGoodsListItem[] newArray(int size) {
                return new ListGoodsListItem[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeList(this.goodsList);
        dest.writeSerializable(this.store);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public ShoppingCart() {
    }

    protected ShoppingCart(Parcel in) {
        this.id = in.readString();
        this.goodsList = new ArrayList<ListGoodsListItem>();
        in.readList(this.goodsList, ListGoodsListItem.class.getClassLoader());
        this.store = (MarkStore) in.readSerializable();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ShoppingCart> CREATOR = new Parcelable.Creator<ShoppingCart>() {
        @Override
        public ShoppingCart createFromParcel(Parcel source) {
            return new ShoppingCart(source);
        }

        @Override
        public ShoppingCart[] newArray(int size) {
            return new ShoppingCart[size];
        }
    };
}
