package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/5/23.
 */

public class PromotionInfo implements Parcelable {
    public String router;
    public boolean canSkip;
    public int skipSeconds;
    public String url;
    public long startTime;
    public long endTime;

    public PromotionInfo(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.router);
        dest.writeByte(this.canSkip ? (byte) 1 : (byte) 0);
        dest.writeInt(this.skipSeconds);
        dest.writeString(this.url);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
    }

    public PromotionInfo() {
    }

    public PromotionInfo(Parcel in) {
        this.router = in.readString();
        this.canSkip = in.readByte() != 0;
        this.skipSeconds = in.readInt();
        this.url = in.readString();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
    }

    public static final Parcelable.Creator<PromotionInfo> CREATOR = new Parcelable.Creator<PromotionInfo>() {
        @Override
        public PromotionInfo createFromParcel(Parcel source) {
            return new PromotionInfo(source);
        }

        @Override
        public PromotionInfo[] newArray(int size) {
            return new PromotionInfo[size];
        }
    };
}
