package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/10/23.
 */

public class ConferenceRoom implements Parcelable {
    public String id;
    public String reservationId;
    public String communityId;
    public long beginTime;
    public String name;
    public String fee;
    public int floor;
    public int galleryful;
    public int maxGalleryful;
    public int openTime;
    public int closeTime;
    public List<ConferenceEquipment> equipments;
    public String picture;

    public long startDate;
    public long endDate;

    public long reminderTime;
    public String subject;
    public boolean isUpdate;
    public ArrayList<ContactsPersonEntity> conferenceParticipants;
    public long conferenceEndTime; //会议实际结束时间

    public ConferenceRoom() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.reservationId);
        dest.writeString(this.communityId);
        dest.writeLong(this.beginTime);
        dest.writeString(this.name);
        dest.writeString(this.fee);
        dest.writeInt(this.floor);
        dest.writeInt(this.galleryful);
        dest.writeInt(this.maxGalleryful);
        dest.writeInt(this.openTime);
        dest.writeInt(this.closeTime);
        dest.writeTypedList(this.equipments);
        dest.writeString(this.picture);
        dest.writeLong(this.startDate);
        dest.writeLong(this.endDate);
        dest.writeLong(this.reminderTime);
        dest.writeString(this.subject);
        dest.writeByte(this.isUpdate ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.conferenceParticipants);
        dest.writeLong(this.conferenceEndTime);
    }

    protected ConferenceRoom(Parcel in) {
        this.id = in.readString();
        this.reservationId = in.readString();
        this.communityId = in.readString();
        this.beginTime = in.readLong();
        this.name = in.readString();
        this.fee = in.readString();
        this.floor = in.readInt();
        this.galleryful = in.readInt();
        this.maxGalleryful = in.readInt();
        this.openTime = in.readInt();
        this.closeTime = in.readInt();
        this.equipments = in.createTypedArrayList(ConferenceEquipment.CREATOR);
        this.picture = in.readString();
        this.startDate = in.readLong();
        this.endDate = in.readLong();
        this.reminderTime = in.readLong();
        this.subject = in.readString();
        this.isUpdate = in.readByte() != 0;
        this.conferenceParticipants = in.createTypedArrayList(ContactsPersonEntity.CREATOR);
        this.conferenceEndTime = in.readLong();
    }

    public static final Creator<ConferenceRoom> CREATOR = new Creator<ConferenceRoom>() {
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
