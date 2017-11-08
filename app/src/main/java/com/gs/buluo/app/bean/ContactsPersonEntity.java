package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Solang on 2017/10/24.
 */

public class ContactsPersonEntity implements Parcelable{
    public String id;
    public String conferenceReservationId;
    public String name;
    public String phone;
    public String photoUri;
    public boolean checked;
    public boolean deleted;


    public ContactsPersonEntity() {
    }


    protected ContactsPersonEntity(Parcel in) {
        id = in.readString();
        conferenceReservationId = in.readString();
        name = in.readString();
        phone = in.readString();
        photoUri = in.readString();
        checked = in.readByte() != 0;
        deleted = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(conferenceReservationId);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(photoUri);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (deleted ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactsPersonEntity> CREATOR = new Creator<ContactsPersonEntity>() {
        @Override
        public ContactsPersonEntity createFromParcel(Parcel in) {
            return new ContactsPersonEntity(in);
        }

        @Override
        public ContactsPersonEntity[] newArray(int size) {
            return new ContactsPersonEntity[size];
        }
    };
}
