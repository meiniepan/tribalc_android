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
    public long maxWithdrawAmount;
    public long maxPaymentAmount;

    public BankCard() {
    }

    public BankCard(String name) {
        bankName = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.ownerId);
        dest.writeLong(this.createTime);
        dest.writeString(this.userName);
        dest.writeString(this.bankAddress);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCardNum);
        dest.writeString(this.phone);
        dest.writeString(this.bankCardType);
        dest.writeString(this.bankCode);
        dest.writeInt(this.limit);
        dest.writeByte(this.personal ? (byte) 1 : (byte) 0);
        dest.writeInt(this.bindType == null ? -1 : this.bindType.ordinal());
        dest.writeLong(this.maxWithdrawAmount);
        dest.writeLong(this.maxPaymentAmount);
    }

    protected BankCard(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        this.createTime = in.readLong();
        this.userName = in.readString();
        this.bankAddress = in.readString();
        this.bankName = in.readString();
        this.bankCardNum = in.readString();
        this.phone = in.readString();
        this.bankCardType = in.readString();
        this.bankCode = in.readString();
        this.limit = in.readInt();
        this.personal = in.readByte() != 0;
        int tmpBindType = in.readInt();
        this.bindType = tmpBindType == -1 ? null : BankCardBindTypeEnum.values()[tmpBindType];
        this.maxWithdrawAmount = in.readLong();
        this.maxPaymentAmount = in.readLong();
    }

    public static final Creator<BankCard> CREATOR = new Creator<BankCard>() {
        @Override
        public BankCard createFromParcel(Parcel source) {
            return new BankCard(source);
        }

        @Override
        public BankCard[] newArray(int size) {
            return new BankCard[size];
        }
    };
}
