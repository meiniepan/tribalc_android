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
    public long conferenceBeginTime;
    public long conferenceEndTime;
    public long updateConferenceBeginTime;
    public long updateConferenceEndTime; //会议实际结束时间
    public String name;
    public String fee;
    public int floor;
    public int galleryful;
    public int maxGalleryful;
    public int openTime;
    public int closeTime;
    public List<ConferenceEquipment> equipments;
    public String pictures;

    public long startDate;
    public long endDate;
    public long reminderTime;
    public String subject;
    public boolean isUpdate;
    public ArrayList<ContactsPersonEntity> conferenceParticipants;

    public ConferenceRoom() {
    }


    protected ConferenceRoom(Parcel in) {
        id = in.readString();
        reservationId = in.readString();
        communityId = in.readString();
        conferenceBeginTime = in.readLong();
        conferenceEndTime = in.readLong();
        updateConferenceBeginTime = in.readLong();
        updateConferenceEndTime = in.readLong();
        name = in.readString();
        fee = in.readString();
        floor = in.readInt();
        galleryful = in.readInt();
        maxGalleryful = in.readInt();
        openTime = in.readInt();
        closeTime = in.readInt();
        equipments = in.createTypedArrayList(ConferenceEquipment.CREATOR);
        pictures = in.readString();
        startDate = in.readLong();
        endDate = in.readLong();
        reminderTime = in.readLong();
        subject = in.readString();
        isUpdate = in.readByte() != 0;
        conferenceParticipants = in.createTypedArrayList(ContactsPersonEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(reservationId);
        dest.writeString(communityId);
        dest.writeLong(conferenceBeginTime);
        dest.writeLong(conferenceEndTime);
        dest.writeLong(updateConferenceBeginTime);
        dest.writeLong(updateConferenceEndTime);
        dest.writeString(name);
        dest.writeString(fee);
        dest.writeInt(floor);
        dest.writeInt(galleryful);
        dest.writeInt(maxGalleryful);
        dest.writeInt(openTime);
        dest.writeInt(closeTime);
        dest.writeTypedList(equipments);
        dest.writeString(pictures);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeLong(reminderTime);
        dest.writeString(subject);
        dest.writeByte((byte) (isUpdate ? 1 : 0));
        dest.writeTypedList(conferenceParticipants);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConferenceRoom> CREATOR = new Creator<ConferenceRoom>() {
        @Override
        public ConferenceRoom createFromParcel(Parcel in) {
            return new ConferenceRoom(in);
        }

        @Override
        public ConferenceRoom[] newArray(int size) {
            return new ConferenceRoom[size];
        }
    };
}
