package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/10/26.
 */

public class ConferenceFilterBean implements Parcelable {
    public String startFloor;
    public String endFloor;
    public String attendance;
    public long startDate;
    public long endDate;
    public ArrayList<ConferenceEquipment> equipments;
    public String duration;

    public ConferenceFilterBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startFloor);
        dest.writeString(this.endFloor);
        dest.writeString(this.attendance);
        dest.writeLong(this.startDate);
        dest.writeLong(this.endDate);
        dest.writeList(this.equipments);
        dest.writeString(this.duration);
    }

    protected ConferenceFilterBean(Parcel in) {
        this.startFloor = in.readString();
        this.endFloor = in.readString();
        this.attendance = in.readString();
        this.startDate = in.readLong();
        this.endDate = in.readLong();
        this.equipments = new ArrayList<ConferenceEquipment>();
        in.readList(this.equipments, ConferenceEquipment.class.getClassLoader());
        this.duration = in.readString();
    }

    public static final Creator<ConferenceFilterBean> CREATOR = new Creator<ConferenceFilterBean>() {
        @Override
        public ConferenceFilterBean createFromParcel(Parcel source) {
            return new ConferenceFilterBean(source);
        }

        @Override
        public ConferenceFilterBean[] newArray(int size) {
            return new ConferenceFilterBean[size];
        }
    };
}
