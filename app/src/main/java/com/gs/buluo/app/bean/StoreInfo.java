package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Solang on 2017/7/18.
 */

public class StoreInfo implements Parcelable{
    public String id;
    public String name;
    public String logo;
    public String[] pictures;
    public String category;
    public String[] sellingPoint;
    public String address;
    public String[] coordinate;
    public String avgprice;
    public String desc;
    public String businessHours;
    public String markPlace;
    public String serviceLine;
    public ArrayList<Privilege> privileges;


    protected StoreInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        logo = in.readString();
        pictures = in.createStringArray();
        category = in.readString();
        sellingPoint = in.createStringArray();
        address = in.readString();
        coordinate = in.createStringArray();
        avgprice = in.readString();
        desc = in.readString();
        businessHours = in.readString();
        markPlace = in.readString();
        serviceLine = in.readString();
        privileges = in.createTypedArrayList(Privilege.CREATOR);
    }

    public static final Creator<StoreInfo> CREATOR = new Creator<StoreInfo>() {
        @Override
        public StoreInfo createFromParcel(Parcel in) {
            return new StoreInfo(in);
        }

        @Override
        public StoreInfo[] newArray(int size) {
            return new StoreInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(logo);
        dest.writeStringArray(pictures);
        dest.writeString(category);
        dest.writeStringArray(sellingPoint);
        dest.writeString(address);
        dest.writeStringArray(coordinate);
        dest.writeString(avgprice);
        dest.writeString(desc);
        dest.writeString(businessHours);
        dest.writeString(markPlace);
        dest.writeString(serviceLine);
        dest.writeTypedList(privileges);
    }
}
