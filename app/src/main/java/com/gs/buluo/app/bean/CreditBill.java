package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hjn on 2017/7/24.
 */

public class CreditBill implements Parcelable {
    public String id;
    public String ownerId;
    public float amount;
    public float paidAmount;
    public long zeroDate;
    public long billDate;
    public long repayDate;
    public CreditBillStatus status;

    public  enum CreditBillStatus {
        CREATED("创建"), PAID("已还清"), OVERDUE("逾期"), FREEZING("逾期后冰洁，无法继续还款");
        public String status;

        CreditBillStatus(String s) {
            status = s;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.ownerId);
        dest.writeFloat(this.amount);
        dest.writeFloat(this.paidAmount);
        dest.writeLong(this.zeroDate);
        dest.writeLong(this.billDate);
        dest.writeLong(this.repayDate);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    public CreditBill(long o,long c) {
        zeroDate =o;
        billDate=c;
    }

    protected CreditBill(Parcel in) {
        this.id = in.readString();
        this.ownerId = in.readString();
        this.amount = in.readFloat();
        this.paidAmount = in.readFloat();
        this.zeroDate = in.readLong();
        this.billDate = in.readLong();
        this.repayDate = in.readLong();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : CreditBillStatus.values()[tmpStatus];
    }

    public static final Parcelable.Creator<CreditBill> CREATOR = new Parcelable.Creator<CreditBill>() {
        @Override
        public CreditBill createFromParcel(Parcel source) {
            return new CreditBill(source);
        }

        @Override
        public CreditBill[] newArray(int size) {
            return new CreditBill[size];
        }
    };
}
