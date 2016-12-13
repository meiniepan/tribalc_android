package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2016/11/16.
 */
public class ListGoods implements Parcelable {
    public String id;
    public String storeId;
    public String name;
    public String brand;
    public String mainPicture;
    public String originPrice;
    public String salePrice;
    public String saleQuantity;
    public String standardSnapshot;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.storeId);
        dest.writeString(this.name);
        dest.writeString(this.brand);
        dest.writeString(this.mainPicture);
        dest.writeString(this.originPrice);
        dest.writeString(this.salePrice);
        dest.writeString(this.saleQuantity);
        dest.writeString(this.standardSnapshot);
    }

    public ListGoods() {
    }

    protected ListGoods(Parcel in) {
        this.id = in.readString();
        this.storeId = in.readString();
        this.name = in.readString();
        this.brand = in.readString();
        this.mainPicture = in.readString();
        this.originPrice = in.readString();
        this.salePrice = in.readString();
        this.saleQuantity = in.readString();
        this.standardSnapshot = in.readString();
    }

    public static final Parcelable.Creator<ListGoods> CREATOR = new Parcelable.Creator<ListGoods>() {
        @Override
        public ListGoods createFromParcel(Parcel source) {
            return new ListGoods(source);
        }

        @Override
        public ListGoods[] newArray(int size) {
            return new ListGoods[size];
        }
    };
}
