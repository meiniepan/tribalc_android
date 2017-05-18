package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2017/3/10.
 */

public class LockEquip implements Parcelable {
    public String id;
    public String communityId;
    public long createTime;
    public String managerId;
    public String name;
    public String desc;
    public List<String> activityTime;
    public boolean actived;
    public boolean selected;

    public LockEquip(String name) {
        this.name =name;
    }
    public LockEquip() {
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.communityId);
        dest.writeLong(this.createTime);
        dest.writeString(this.managerId);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeStringList(this.activityTime);
        dest.writeByte(this.actived ? (byte) 1 : (byte) 0);
    }

    protected LockEquip(Parcel in) {
        this.id = in.readString();
        this.communityId = in.readString();
        this.createTime = in.readLong();
        this.managerId = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.activityTime = in.createStringArrayList();
        this.actived = in.readByte() != 0;
    }

    public static final Parcelable.Creator<LockEquip> CREATOR = new Parcelable.Creator<LockEquip>() {
        @Override
        public LockEquip createFromParcel(Parcel source) {
            return new LockEquip(source);
        }

        @Override
        public LockEquip[] newArray(int size) {
            return new LockEquip[size];
        }
    };
}
