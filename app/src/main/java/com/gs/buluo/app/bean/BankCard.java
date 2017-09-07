package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCard implements Parcelable {
    public String id;
    public String ownerId;
    public long createTime;

    public String userName;
    public String bankAddress;
    public String bankName;
    public String bankCardNum;
    public String phone;
    public String bankCardType;
    public String bankCode;
    public int limit;
    public boolean personal;
    public BankCardBindTypeEnum bindType;

    public BankCard() {
    }

    public BankCard(String name) {
        bankName = name;
    }

    protected BankCard(Parcel in) {
        id = in.readString();
        ownerId = in.readString();
        createTime = in.readLong();
        userName = in.readString();
        bankAddress = in.readString();
        bankName = in.readString();
        bankCardNum = in.readString();
        phone = in.readString();
        bankCardType = in.readString();
        bankCode = in.readString();
        limit = in.readInt();
        personal = in.readByte() != 0;
    }

    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel in) {
            return new BankCard(in);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(ownerId);
        dest.writeLong(createTime);
        dest.writeString(userName);
        dest.writeString(bankAddress);
        dest.writeString(bankName);
        dest.writeString(bankCardNum);
        dest.writeString(phone);
        dest.writeString(bankCardType);
        dest.writeString(bankCode);
        dest.writeInt(limit);
        dest.writeByte((byte) (personal ? 1 : 0));
    }


}
