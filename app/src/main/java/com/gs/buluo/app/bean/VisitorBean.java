package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.gs.buluo.app.Constant;

/**
 * Created by hjn on 2017/3/9.
 */

public class VisitorBean implements Parcelable {
    public String name ;
    public String phone;
    public String door;
    public String code = Constant.APK_URL;

    public VisitorBean(String name, String phone, String gate) {
        this.name=name;
        this.phone=phone;
        door =gate;
    }

    public VisitorBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.door);
        dest.writeString(this.code);
    }

    protected VisitorBean(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.door = in.readString();
        this.code = in.readString();
    }

    public static final Creator<VisitorBean> CREATOR = new Creator<VisitorBean>() {
        @Override
        public VisitorBean createFromParcel(Parcel source) {
            return new VisitorBean(source);
        }

        @Override
        public VisitorBean[] newArray(int size) {
            return new VisitorBean[size];
        }
    };
}
