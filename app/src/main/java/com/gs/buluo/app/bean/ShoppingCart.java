package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCart implements Parcelable {
    public String id;
    public List<CartItem> goodsList;
    public MarkStore store;
    public boolean isSelected;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(this.goodsList);
        dest.writeParcelable(this.store, flags);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public ShoppingCart() {
    }

    protected ShoppingCart(Parcel in) {
        this.id = in.readString();
        this.goodsList = in.createTypedArrayList(CartItem.CREATOR);
        this.store = in.readParcelable(MarkStore.class.getClassLoader());
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
