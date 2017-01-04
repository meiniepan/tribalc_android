package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fs on 2016/12/13.
 */
public class PropertyBeen implements Parcelable {
    public String communityID;
    public String enterpriseID;
    public String enterpriseName;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.communityID);
        dest.writeString(this.enterpriseID);
        dest.writeString(this.enterpriseName);
        dest.writeString(this.name);
    }

    public PropertyBeen() {
    }

    protected PropertyBeen(Parcel in) {
        this.communityID = in.readString();
        this.enterpriseID = in.readString();
        this.enterpriseName = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<PropertyBeen> CREATOR = new Parcelable.Creator<PropertyBeen>() {
        @Override
        public PropertyBeen createFromParcel(Parcel source) {
            return new PropertyBeen(source);
        }

        @Override
        public PropertyBeen[] newArray(int size) {
            return new PropertyBeen[size];
        }
    };
}
