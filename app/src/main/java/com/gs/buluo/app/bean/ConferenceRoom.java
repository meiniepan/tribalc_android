package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/10/23.
 */

public class ConferenceRoom implements Parcelable {
    public String id;
    public String communityId;
    public String name;
    public String fee;
    public int floor;
    public int galleryful;
    public int maxGalleryful;
    public int openTime;
    public int closeTime;

    public long startDate;
    public long endDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.communityId);
        dest.writeString(this.name);
        dest.writeString(this.fee);
        dest.writeInt(this.floor);
        dest.writeInt(this.galleryful);
        dest.writeInt(this.maxGalleryful);
        dest.writeInt(this.openTime);
        dest.writeInt(this.closeTime);
        dest.writeLong(this.startDate);
        dest.writeLong(this.endDate);
    }

    public ConferenceRoom() {
    }

    protected ConferenceRoom(Parcel in) {
        this.id = in.readString();
        this.communityId = in.readString();
        this.name = in.readString();
        this.fee = in.readString();
        this.floor = in.readInt();
        this.galleryful = in.readInt();
        this.maxGalleryful = in.readInt();
        this.openTime = in.readInt();
        this.closeTime = in.readInt();
        this.startDate = in.readLong();
        this.endDate = in.readLong();
    }

    public static final Parcelable.Creator<ConferenceRoom> CREATOR = new Parcelable.Creator<ConferenceRoom>() {
        @Override
        public ConferenceRoom createFromParcel(Parcel source) {
            return new ConferenceRoom(source);
        }

        @Override
        public ConferenceRoom[] newArray(int size) {
            return new ConferenceRoom[size];
        }
    };
}
