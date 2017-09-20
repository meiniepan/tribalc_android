package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

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
    public List<String> tags;
    public float expressFee;

    public ListGoods() {
    }


    protected ListGoods(Parcel in) {
        id = in.readString();
        storeId = in.readString();
        name = in.readString();
        brand = in.readString();
        mainPicture = in.readString();
        originPrice = in.readString();
        salePrice = in.readString();
        saleQuantity = in.readString();
        standardSnapshot = in.readString();
        tags = in.createStringArrayList();
        expressFee = in.readFloat();
    }

    public static final Creator<ListGoods> CREATOR = new Creator<ListGoods>() {
        @Override
        public ListGoods createFromParcel(Parcel in) {
            return new ListGoods(in);
        }

        @Override
        public ListGoods[] newArray(int size) {
            return new ListGoods[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(storeId);
        dest.writeString(name);
        dest.writeString(brand);
        dest.writeString(mainPicture);
        dest.writeString(originPrice);
        dest.writeString(salePrice);
        dest.writeString(saleQuantity);
        dest.writeString(standardSnapshot);
        dest.writeStringList(tags);
        dest.writeFloat(expressFee);
    }
}
