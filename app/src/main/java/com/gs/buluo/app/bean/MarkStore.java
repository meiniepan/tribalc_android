package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hjn on 2016/11/24.
 */
public class MarkStore implements Parcelable {
    public String id;
    public String name;
    public String logo;
    public String phone;

    public MarkStore() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeString(this.phone);
    }

    protected MarkStore(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.logo = in.readString();
        this.phone = in.readString();
    }

    public static final Creator<MarkStore> CREATOR = new Creator<MarkStore>() {
        @Override
        public MarkStore createFromParcel(Parcel source) {
            return new MarkStore(source);
        }

        @Override
        public MarkStore[] newArray(int size) {
            return new MarkStore[size];
        }
    };
}
