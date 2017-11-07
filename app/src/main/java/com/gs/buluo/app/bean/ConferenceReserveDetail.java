package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/11/2.
 */

public class ConferenceReserveDetail implements Parcelable {
    public String id;
    public String name;
    public String floor;
    public String communityName;
    public int galleryful;
    public int maxGalleryful;
    public String personId;
    public String personName;
    public String personPhone;
    public String subject;
    public String reservationNum;
    public String totalFee;
    public String picture;
    public long createTime;
    public long conferenceBeginTime;
    public long conferenceEndTime; //会议实际结束时间
    public long planEndTime;//会议原定结束时间
    public int openTime;
    public int closeTime;
    public List<ConferenceEquipment> equipmentList;
    public ArrayList<ContactsPersonEntity> conferenceParticipants;
    public ConferenceReservation.BoardroomOrderStatus status;
    public long reminderTime;

    public ConferenceReserveDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.floor);
        dest.writeString(this.communityName);
        dest.writeInt(this.galleryful);
        dest.writeInt(this.maxGalleryful);
        dest.writeString(this.personId);
        dest.writeString(this.personName);
        dest.writeString(this.personPhone);
        dest.writeString(this.subject);
        dest.writeString(this.reservationNum);
        dest.writeString(this.totalFee);
        dest.writeString(this.picture);
        dest.writeLong(this.createTime);
        dest.writeLong(this.conferenceBeginTime);
        dest.writeLong(this.conferenceEndTime);
        dest.writeLong(this.planEndTime);
        dest.writeInt(this.openTime);
        dest.writeInt(this.closeTime);
        dest.writeTypedList(this.equipmentList);
        dest.writeTypedList(this.conferenceParticipants);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeLong(this.reminderTime);
    }

    protected ConferenceReserveDetail(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.floor = in.readString();
        this.communityName = in.readString();
        this.galleryful = in.readInt();
        this.maxGalleryful = in.readInt();
        this.personId = in.readString();
        this.personName = in.readString();
        this.personPhone = in.readString();
        this.subject = in.readString();
        this.reservationNum = in.readString();
        this.totalFee = in.readString();
        this.picture = in.readString();
        this.createTime = in.readLong();
        this.conferenceBeginTime = in.readLong();
        this.conferenceEndTime = in.readLong();
        this.planEndTime = in.readLong();
        this.openTime = in.readInt();
        this.closeTime = in.readInt();
        this.equipmentList = in.createTypedArrayList(ConferenceEquipment.CREATOR);
        this.conferenceParticipants = in.createTypedArrayList(ContactsPersonEntity.CREATOR);
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : ConferenceReservation.BoardroomOrderStatus.values()[tmpStatus];
        this.reminderTime = in.readLong();
    }

    public static final Creator<ConferenceReserveDetail> CREATOR = new Creator<ConferenceReserveDetail>() {
        @Override
        public ConferenceReserveDetail createFromParcel(Parcel source) {
            return new ConferenceReserveDetail(source);
        }

        @Override
        public ConferenceReserveDetail[] newArray(int size) {
            return new ConferenceReserveDetail[size];
        }
    };
}
