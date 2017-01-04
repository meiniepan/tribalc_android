package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fs on 2016/12/15.
 */
public class ListPropertyManagement implements Parcelable ,IBaseResponse {
    public String id;
    public String communityName;
    public String companyName;
    public String applyPersonName;
    public String floor;
    public String fixProject;
    public long appointTime;
    public String masterPersonName;
    public String phone;
    public long doorTime;
    public List<String> pictures;
    public String problemDesc;
    public String status;
    public String propertyNum;
    public String  totalFee;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.communityName);
        dest.writeString(this.companyName);
        dest.writeString(this.applyPersonName);
        dest.writeString(this.floor);
        dest.writeString(this.fixProject);
        dest.writeLong(this.appointTime);
        dest.writeString(this.masterPersonName);
        dest.writeString(this.phone);
        dest.writeLong(this.doorTime);
        dest.writeStringList(this.pictures);
        dest.writeString(this.problemDesc);
        dest.writeString(this.status);
        dest.writeString(this.propertyNum);
        dest.writeString(this.totalFee);
    }

    public ListPropertyManagement() {
    }

    protected ListPropertyManagement(Parcel in) {
        this.id = in.readString();
        this.communityName = in.readString();
        this.companyName = in.readString();
        this.applyPersonName = in.readString();
        this.floor = in.readString();
        this.fixProject = in.readString();
        this.appointTime = in.readLong();
        this.masterPersonName = in.readString();
        this.phone = in.readString();
        this.doorTime = in.readLong();
        this.pictures = in.createStringArrayList();
        this.problemDesc = in.readString();
        this.status = in.readString();
        this.propertyNum = in.readString();
        this.totalFee = in.readString();
    }

    public static final Parcelable.Creator<ListPropertyManagement> CREATOR = new Parcelable.Creator<ListPropertyManagement>() {
        @Override
        public ListPropertyManagement createFromParcel(Parcel source) {
            return new ListPropertyManagement(source);
        }

        @Override
        public ListPropertyManagement[] newArray(int size) {
            return new ListPropertyManagement[size];
        }
    };
}
