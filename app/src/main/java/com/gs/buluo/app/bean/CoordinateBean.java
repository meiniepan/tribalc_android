package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/2/15.
 */

public class CoordinateBean implements Parcelable {
    public double longitude;
    public double latitude;
    public String storeId;
    public MarkStore store;


    public CoordinateBean(double lon, double lan) {
        longitude = lon;
        latitude= lan;
    }

    public CoordinateBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.storeId);
        dest.writeParcelable(this.store, flags);
    }

    protected CoordinateBean(Parcel in) {
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.storeId = in.readString();
        this.store = in.readParcelable(MarkStore.class.getClassLoader());
    }

    public static final Creator<CoordinateBean> CREATOR = new Creator<CoordinateBean>() {
        @Override
        public CoordinateBean createFromParcel(Parcel source) {
            return new CoordinateBean(source);
        }

        @Override
        public CoordinateBean[] newArray(int size) {
            return new CoordinateBean[size];
        }
    };
}
