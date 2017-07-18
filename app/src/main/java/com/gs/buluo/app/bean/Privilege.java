package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/7/17.
 */

public class Privilege implements Parcelable {
    public String id;
    public String ownerId;
    public PrivilegeType type;
    public BigDecimal condition;
    public BigDecimal value;
    public List<Integer> activityTime;
    public long startDate;
    public long endDate;

    public enum PrivilegeType {
        DISCOUNT, REDUCE, ALIQUOT
    }

    public Privilege() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.ownerId);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeSerializable(this.condition);
        dest.writeSerializable(this.value);
        dest.writeList(this.activityTime);
        dest.writeLong(this.startDate);
        dest.writeLong(this.endDate);
    }

    protected Privilege(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : PrivilegeType.values()[tmpType];
        this.condition = (BigDecimal) in.readSerializable();
        this.value = (BigDecimal) in.readSerializable();
        this.activityTime = new ArrayList<Integer>();
        in.readList(this.activityTime, Integer.class.getClassLoader());
        this.startDate = in.readLong();
        this.endDate = in.readLong();
    }

    public static final Creator<Privilege> CREATOR = new Creator<Privilege>() {
        @Override
        public Privilege createFromParcel(Parcel source) {
            return new Privilege(source);
        }

        @Override
        public Privilege[] newArray(int size) {
            return new Privilege[size];
        }
    };
}
