package com.gs.buluo.app.bean;

import android.net.Uri;
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
    public Uri photoUri;
    public boolean checked;
    public boolean deleted;


    public ContactsPersonEntity() {
    }


    protected ContactsPersonEntity(Parcel in) {
        name = in.readString();
        phone = in.readString();
        photoUri = in.readParcelable(Uri.class.getClassLoader());
        checked = in.readByte() != 0;
        deleted = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeParcelable(photoUri, flags);
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
