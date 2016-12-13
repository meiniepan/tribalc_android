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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
    }

    public MarkStore() {
    }

    protected MarkStore(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.logo = in.readString();
    }

    public static final Parcelable.Creator<MarkStore> CREATOR = new Parcelable.Creator<MarkStore>() {
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
