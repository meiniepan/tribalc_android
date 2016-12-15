package com.gs.buluo.app.bean;

/**
 * Created by fs on 2016/12/15.
 */
public class ListPropertyManagement {
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

    @Override
    public String toString() {
        return "ListPropertyManagement{" +
                "id='" + id + '\'' +
                ", communityName='" + communityName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", applyPersonName='" + applyPersonName + '\'' +
                ", floor='" + floor + '\'' +
                ", fixProject='" + fixProject + '\'' +
                ", appointTime=" + appointTime +
                ", masterPersonName='" + masterPersonName + '\'' +
                ", phone='" + phone + '\'' +
                ", doorTime=" + doorTime +
                '}';
    }
}
