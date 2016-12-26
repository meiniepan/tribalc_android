package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.io.Serializable;

/**
 * Created by fs on 2016/12/12.
 */
public class CompanyDetail implements Parcelable, IBaseResponse {
    public CompanyInfo company;
    public String department;
    public String position;
    public String personNum;
    public String comfirmed;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.company, flags);
        dest.writeString(this.department);
        dest.writeString(this.position);
        dest.writeString(this.personNum);
        dest.writeString(this.comfirmed);
    }

    public CompanyDetail() {
    }

    protected CompanyDetail(Parcel in) {
        this.company = in.readParcelable(CompanyInfo.class.getClassLoader());
        this.department = in.readString();
        this.position = in.readString();
        this.personNum = in.readString();
        this.comfirmed = in.readString();
    }

    public static final Parcelable.Creator<CompanyDetail> CREATOR = new Parcelable.Creator<CompanyDetail>() {
        @Override
        public CompanyDetail createFromParcel(Parcel source) {
            return new CompanyDetail(source);
        }

        @Override
        public CompanyDetail[] newArray(int size) {
            return new CompanyDetail[size];
        }
    };
}
