package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/10/23.
 */

public class ConferenceEquipment implements Parcelable {
    public String id;
    public String name;
    public boolean isSelected;

    public ConferenceEquipment(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected ConferenceEquipment(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ConferenceEquipment> CREATOR = new Parcelable.Creator<ConferenceEquipment>() {
        @Override
        public ConferenceEquipment createFromParcel(Parcel source) {
            return new ConferenceEquipment(source);
        }

        @Override
        public ConferenceEquipment[] newArray(int size) {
            return new ConferenceEquipment[size];
        }
    };
}
