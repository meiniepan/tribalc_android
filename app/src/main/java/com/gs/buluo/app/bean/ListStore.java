package com.gs.buluo.app.bean;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/11/24.
 */
public class ListStore extends MarkStore{
    public StoreCategory category;
    public String brand;
    public String thumbnail;
    public String mainPicture;
    public List<Double> coordinate;
    public String address;
    public String district;
    public String discount;
    public String markPlace;
    public List<String> cookingStyle;

    public enum StoreCategory {
        FOOD("食品"), GIFT("礼品"), OFFICE("办公用品"), LIVING("生活用品"), HOUSE("家居用品"), MAKEUP("化妆品"), PENETRATION("妇婴用品"),
        REPAST("餐饮"), HAIRDRESSING("美容"), FITNESS("健身"), ENTERTAINMENT("休闲娱乐"), KEEPHEALTHY("养生");
        String type;

        StoreCategory(String s) {
            type=s;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.category == null ? -1 : this.category.ordinal());
        dest.writeString(this.brand);
        dest.writeString(this.thumbnail);
        dest.writeString(this.mainPicture);
        dest.writeList(this.coordinate);
        dest.writeString(this.address);
        dest.writeString(this.district);
        dest.writeString(this.discount);
        dest.writeString(this.markPlace);
        dest.writeStringList(this.cookingStyle);
    }

    public ListStore() {
    }

    protected ListStore(Parcel in) {
        super(in);
        int tmpCategory = in.readInt();
        this.category = tmpCategory == -1 ? null : StoreCategory.values()[tmpCategory];
        this.brand = in.readString();
        this.thumbnail = in.readString();
        this.mainPicture = in.readString();
        this.coordinate = new ArrayList<Double>();
        in.readList(this.coordinate, Double.class.getClassLoader());
        this.address = in.readString();
        this.district = in.readString();
        this.discount = in.readString();
        this.markPlace = in.readString();
        this.cookingStyle = in.createStringArrayList();
    }

    public static final Creator<ListStore> CREATOR = new Creator<ListStore>() {
        @Override
        public ListStore createFromParcel(Parcel source) {
            return new ListStore(source);
        }

        @Override
        public ListStore[] newArray(int size) {
            return new ListStore[size];
        }
    };
}
