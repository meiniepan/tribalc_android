package com.gs.buluo.app.bean.RequestBodyBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2017/5/18.
 */

public class MultiLockRequest implements Parcelable {
    public List<String> equipIds;
    public String phone;
    public String name;
    public long beginTime;
    public long endTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.equipIds);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeLong(this.beginTime);
        dest.writeLong(this.endTime);
    }

    public MultiLockRequest() {
    }

    protected MultiLockRequest(Parcel in) {
        this.equipIds = in.createStringArrayList();
        this.phone = in.readString();
        this.name = in.readString();
        this.beginTime = in.readLong();
        this.endTime = in.readLong();
    }

    public static final Parcelable.Creator<MultiLockRequest> CREATOR = new Parcelable.Creator<MultiLockRequest>() {
        @Override
        public MultiLockRequest createFromParcel(Parcel source) {
            return new MultiLockRequest(source);
        }

        @Override
        public MultiLockRequest[] newArray(int size) {
            return new MultiLockRequest[size];
        }
    };
}
