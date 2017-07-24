package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by fs on 2016/12/12.
 */
public class CompanyInfo implements Parcelable {

    private String id;
    private String logo;
    private String desc;
    private List<String> pictures;


    public String companyId;
    public String companyName;
    public float balance;
    public float creditLimit;
    public float creditBalance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return companyName;
    }

    public void setName(String name) {
        this.companyName = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public CompanyInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.logo);
        dest.writeString(this.desc);
        dest.writeStringList(this.pictures);
        dest.writeString(this.companyId);
        dest.writeString(this.companyName);
        dest.writeFloat(this.balance);
        dest.writeFloat(this.creditLimit);
        dest.writeFloat(this.creditBalance);
    }

    protected CompanyInfo(Parcel in) {
        this.id = in.readString();
        this.logo = in.readString();
        this.desc = in.readString();
        this.pictures = in.createStringArrayList();
        this.companyId = in.readString();
        this.companyName = in.readString();
        this.balance = in.readFloat();
        this.creditLimit = in.readFloat();
        this.creditBalance = in.readFloat();
    }

    public static final Creator<CompanyInfo> CREATOR = new Creator<CompanyInfo>() {
        @Override
        public CompanyInfo createFromParcel(Parcel source) {
            return new CompanyInfo(source);
        }

        @Override
        public CompanyInfo[] newArray(int size) {
            return new CompanyInfo[size];
        }
    };
}
