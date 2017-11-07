package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/10/31.
 */

public class ConferenceReservation implements Parcelable {
    public String id;
    public String reservationNum;
    public String name;
    public String floor;
    public String picture;
    public String totalFee;
    public long conferenceBeginTime;
    public long conferenceEndTime;
    public BoardroomOrderStatus status;

    public enum BoardroomOrderStatus {
        RESERVED("已预定"), CANCEL("已取消"),PAYED("已付款"), FINISHED("已结束"),PROGRESS("已开始");
        public String status;

        BoardroomOrderStatus(String status) {
            this.status = status;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.reservationNum);
        dest.writeString(this.name);
        dest.writeString(this.floor);
        dest.writeString(this.picture);
        dest.writeString(this.totalFee);
        dest.writeLong(this.conferenceBeginTime);
        dest.writeLong(this.conferenceEndTime);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    public ConferenceReservation() {
    }

    protected ConferenceReservation(Parcel in) {
        this.id = in.readString();
        this.reservationNum = in.readString();
        this.name = in.readString();
        this.floor = in.readString();
        this.picture = in.readString();
        this.totalFee = in.readString();
        this.conferenceBeginTime = in.readLong();
        this.conferenceEndTime = in.readLong();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : BoardroomOrderStatus.values()[tmpStatus];
    }

    public static final Parcelable.Creator<ConferenceReservation> CREATOR = new Parcelable.Creator<ConferenceReservation>() {
        @Override
        public ConferenceReservation createFromParcel(Parcel source) {
            return new ConferenceReservation(source);
        }

        @Override
        public ConferenceReservation[] newArray(int size) {
            return new ConferenceReservation[size];
        }
    };
}
