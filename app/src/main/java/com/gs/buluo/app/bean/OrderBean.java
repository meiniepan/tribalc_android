package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderBean implements Parcelable {
    public  String id;
    public String orderNum;
    public String ownerId;
    public String address;
    public String expressType;
    public float expressFee;
    public float totalFee;
    public String note;
    public  PayChannel payChannel;
    public OrderStatus status;
    public long createTime;
    public long settleTime;
    public long deliveryTime;
    public long receivedTime;
    public MarkStore store;
    public List<CartItem> itemList;

    public enum  PayChannel{
        BALANCE("余额支付") ,ALIPAY("支付宝"),WEICHAT("微信支付"),BANKCARD("银行卡");
        String status;

        PayChannel(String status) {
            this.status=status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    public enum  OrderStatus {
        NO_SETTLE("未付款") ,CANCEL("订单取消"),SETTLE("已付款"),DELIVERY("待收货"),RECEIVED("已完成");
        String status;

        OrderStatus(String status) {
            this.status=status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    public OrderBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.orderNum);
        dest.writeString(this.ownerId);
        dest.writeString(this.address);
        dest.writeString(this.expressType);
        dest.writeFloat(this.expressFee);
        dest.writeFloat(this.totalFee);
        dest.writeString(this.note);
        dest.writeInt(this.payChannel == null ? -1 : this.payChannel.ordinal());
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeLong(this.createTime);
        dest.writeLong(this.settleTime);
        dest.writeLong(this.deliveryTime);
        dest.writeLong(this.receivedTime);
        dest.writeParcelable(this.store, flags);
        dest.writeTypedList(this.itemList);
    }

    protected OrderBean(Parcel in) {
        this.id = in.readString();
        this.orderNum = in.readString();
        this.ownerId = in.readString();
        this.address = in.readString();
        this.expressType = in.readString();
        this.expressFee = in.readFloat();
        this.totalFee = in.readFloat();
        this.note = in.readString();
        int tmpPayChannel = in.readInt();
        this.payChannel = tmpPayChannel == -1 ? null : PayChannel.values()[tmpPayChannel];
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : OrderStatus.values()[tmpStatus];
        this.createTime = in.readLong();
        this.settleTime = in.readLong();
        this.deliveryTime = in.readLong();
        this.receivedTime = in.readLong();
        this.store = in.readParcelable(MarkStore.class.getClassLoader());
        this.itemList = in.createTypedArrayList(CartItem.CREATOR);
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel source) {
            return new OrderBean(source);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };
}
