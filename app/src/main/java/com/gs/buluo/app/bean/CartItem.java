package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by hjn on 2016/12/13.
 */
public class CartItem implements Parcelable{
    public String id;
    public String standardId;
    public int amount;
    public ListGoods goods;
    public int repertory;
    public boolean isSelected;
    public boolean isEdit;

    public CartItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.standardId);
        dest.writeInt(this.amount);
        dest.writeParcelable(this.goods, flags);
        dest.writeInt(this.repertory);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEdit ? (byte) 1 : (byte) 0);
    }

    protected CartItem(Parcel in) {
        this.id = in.readString();
        this.standardId = in.readString();
        this.amount = in.readInt();
        this.goods = in.readParcelable(ListGoods.class.getClassLoader());
        this.repertory = in.readInt();
        this.isSelected = in.readByte() != 0;
        this.isEdit = in.readByte() != 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel source) {
            return new CartItem(source);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
