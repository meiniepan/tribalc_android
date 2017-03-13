package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

/**
 * Created by hjn on 2017/3/10.
 */

public class LockKey implements IBaseResponse, Parcelable {
    public String id;
    public String ownerId;
    public String equipId;
    public long createTime;
    public long beginTime;
    public long endTime;
    public String key ;
    public String phone;
    public String name;
    public String equipName;

    public LockKey(String name) {
        this.equipName =name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.ownerId);
        dest.writeString(this.equipId);
        dest.writeLong(this.createTime);
        dest.writeLong(this.beginTime);
        dest.writeLong(this.endTime);
        dest.writeString(this.key);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeString(this.equipName);
    }

    protected LockKey(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        this.equipId = in.readString();
        this.createTime = in.readLong();
        this.beginTime = in.readLong();
        this.endTime = in.readLong();
        this.key = in.readString();
        this.phone = in.readString();
        this.name = in.readString();
        this.equipName = in.readString();
    }

    public static final Parcelable.Creator<LockKey> CREATOR = new Parcelable.Creator<LockKey>() {
        @Override
        public LockKey createFromParcel(Parcel source) {
            return new LockKey(source);
        }

        @Override
        public LockKey[] newArray(int size) {
            return new LockKey[size];
        }
    };
}
