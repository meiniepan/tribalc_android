package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.ArrayList;

/**
 * Created by hjn on 2016/11/18.
 */
public class WalletAccount implements IBaseResponse, Parcelable {
    private enum WalletStatus {
        NORMAL("NORMAL"), LOCKED("LOCKED");
        String status;

        WalletStatus(String status) {
            this.status = status;
        }
    }

    private enum AccountType {
        CARD, //会员卡模式，仅用于商户，此时"商户余额"计算公式为 balance - creditBalance，其他额度信息无效,
        PROTOCOL    //(协议模式，通用，各项余额正常显示)
    }

    public enum CreditStatus {
        NONE("未开通"), NORMAL("正常"), OVERDUE("预期"), DISABLED("停用");

        CreditStatus(String status) {

        }
    }

    public float withdrawCharge; //手续费
    public String id;
    public double balance;
    public WalletStatus state;
    public String lastTrading;
    public String password;

    public float limitedBalance;
    public float creditBalance;
    public float creditLimit;
    public CreditStatus creditStatus;
    public int billDay;
    public int repayDay;

    public ArrayList<BankCard> bankCards;

    public WalletAccount() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.withdrawCharge);
        dest.writeString(this.id);
        dest.writeDouble(this.balance);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeString(this.lastTrading);
        dest.writeString(this.password);
        dest.writeFloat(this.limitedBalance);
        dest.writeFloat(this.creditBalance);
        dest.writeFloat(this.creditLimit);
        dest.writeInt(this.creditStatus == null ? -1 : this.creditStatus.ordinal());
        dest.writeInt(this.billDay);
        dest.writeInt(this.repayDay);
        dest.writeTypedList(this.bankCards);
    }

    protected WalletAccount(Parcel in) {
        this.withdrawCharge = in.readFloat();
        this.id = in.readString();
        this.balance = in.readDouble();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : WalletStatus.values()[tmpState];
        this.lastTrading = in.readString();
        this.password = in.readString();
        this.limitedBalance = in.readFloat();
        this.creditBalance = in.readFloat();
        this.creditLimit = in.readFloat();
        int tmpCreditStatus = in.readInt();
        this.creditStatus = tmpCreditStatus == -1 ? null : CreditStatus.values()[tmpCreditStatus];
        this.billDay = in.readInt();
        this.repayDay = in.readInt();
        this.bankCards = in.createTypedArrayList(BankCard.CREATOR);
    }

    public static final Creator<WalletAccount> CREATOR = new Creator<WalletAccount>() {
        @Override
        public WalletAccount createFromParcel(Parcel source) {
            return new WalletAccount(source);
        }

        @Override
        public WalletAccount[] newArray(int size) {
            return new WalletAccount[size];
        }
    };
}
