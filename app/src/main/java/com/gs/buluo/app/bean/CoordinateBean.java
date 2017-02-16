package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/2/15.
 */

public class CoordinateBean implements Parcelable {
    public double longitude;
    public double latitude;
    public ListStore store;
    public String serveId;


    public CoordinateBean(double lon, double lan, ListStore store,String serveId) {
        longitude = lon;
        latitude= lan;
        this.store =store;
        this.serveId =serveId;
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
        dest.writeParcelable(this.store, flags);
        dest.writeString(this.serveId);
    }

    protected CoordinateBean(Parcel in) {
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.store = in.readParcelable(ListStore.class.getClassLoader());
        this.serveId = in.readString();
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
