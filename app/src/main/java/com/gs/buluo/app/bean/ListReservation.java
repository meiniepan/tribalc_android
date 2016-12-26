package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/29.
 */
public class ListReservation implements Parcelable, IBaseResponse {
    public String id;
    public String storeId;
    public String storeSetMealId;
    public String storeName;
    public String mainPicture;
    public String markPlace;
    public long appointTime;
    public String personNum;
    public List<String>  tags;
    public  ReserveStatus status;

    public enum ReserveStatus {
        PROCESSING("PROCESSING"),FAILURE("FAILURE"),SUCCEED("SUCCEED"),CANCEL("CANCEL");
        public String status;
        ReserveStatus(String processing) {
            status=processing;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.storeId);
        dest.writeString(this.storeSetMealId);
        dest.writeString(this.storeName);
        dest.writeString(this.mainPicture);
        dest.writeString(this.markPlace);
        dest.writeLong(this.appointTime);
        dest.writeString(this.personNum);
        dest.writeStringList(this.tags);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    public ListReservation() {
    }

    protected ListReservation(Parcel in) {
        this.id = in.readString();
        this.storeId = in.readString();
        this.storeSetMealId = in.readString();
        this.storeName = in.readString();
        this.mainPicture = in.readString();
        this.markPlace = in.readString();
        this.appointTime = in.readLong();
        this.personNum = in.readString();
        this.tags = in.createStringArrayList();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : ReserveStatus.values()[tmpStatus];
    }

    public static final Parcelable.Creator<ListReservation> CREATOR = new Parcelable.Creator<ListReservation>() {
        @Override
        public ListReservation createFromParcel(Parcel source) {
            return new ListReservation(source);
        }

        @Override
        public ListReservation[] newArray(int size) {
            return new ListReservation[size];
        }
    };
}
