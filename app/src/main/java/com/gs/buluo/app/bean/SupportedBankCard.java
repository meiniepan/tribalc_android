package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/6/30.
 */

public class SupportedBankCard implements Parcelable {
    public String name;
    public String code;
    public int limit;

    public SupportedBankCard(String name) {
        this.name =name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeInt(this.limit);
    }

    public SupportedBankCard() {
    }

    public SupportedBankCard(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
        this.limit = in.readInt();
    }

    public static final Parcelable.Creator<SupportedBankCard> CREATOR = new Parcelable.Creator<SupportedBankCard>() {
        @Override
        public SupportedBankCard createFromParcel(Parcel source) {
            return new SupportedBankCard(source);
        }

        @Override
        public SupportedBankCard[] newArray(int size) {
            return new SupportedBankCard[size];
        }
    };
}
