package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/10/26.
 */

public class BoardroomFilterBean implements Parcelable {
    public String startFloor;
    public String endFloor;
    public String attendance;
    public long startDate;
    public long endDate;
    public String equipments;
    public String duration;

    public BoardroomFilterBean() {
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
        dest.writeString(this.equipments);
        dest.writeString(this.duration);
    }

    protected BoardroomFilterBean(Parcel in) {
        this.startFloor = in.readString();
        this.endFloor = in.readString();
        this.attendance = in.readString();
        this.startDate = in.readLong();
        this.endDate = in.readLong();
        this.equipments = in.readString();
        this.duration = in.readString();
    }

    public static final Creator<BoardroomFilterBean> CREATOR = new Creator<BoardroomFilterBean>() {
        @Override
        public BoardroomFilterBean createFromParcel(Parcel source) {
            return new BoardroomFilterBean(source);
        }

        @Override
        public BoardroomFilterBean[] newArray(int size) {
            return new BoardroomFilterBean[size];
        }
    };
}
