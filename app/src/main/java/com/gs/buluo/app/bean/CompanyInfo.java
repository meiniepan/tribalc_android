package com.gs.buluo.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fs on 2016/12/12.
 */
public class CompanyInfo implements Parcelable {

    /**
     * id : 6894287f0cf210fc9ceffghj
     * pictures : ["/company/pictures/5820539b8d6a4b5693f39beb/dfer45.jpg","/company/pictures/5820539b8d6a4b5693f39beb/dfer46.jpg"]
     * logo : /company/logo/5820539b8d6a4b5693f39beb/dfer46.jpg
     * name : 阿里巴巴
     * desc : 阿里巴巴集团经营多项业务，另外也从关联公司的业务和服务中取得经营商业生态系统上的支援。
     */

    private String id;
    private String logo;
    private String name;
    private String desc;
    private List<String> pictures;

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
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "CompanyInfo{" +
                "id='" + id + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", pictures=" + pictures +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.logo);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeStringList(this.pictures);
    }

    public CompanyInfo() {
    }

    protected CompanyInfo(Parcel in) {
        this.id = in.readString();
        this.logo = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.pictures = in.createStringArrayList();
    }

    public static final Parcelable.Creator<CompanyInfo> CREATOR = new Parcelable.Creator<CompanyInfo>() {
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
