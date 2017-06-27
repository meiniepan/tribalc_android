package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/6/26.
 */

public class RentProtocol {
    public enum ProtocolType{
        OFFICE("办公"),APARTMENT("公寓");
        ProtocolType(String type) {
        }
    }

    public enum RentStatus{
        NORMAL("正常"),CONFIRM("待确认"), OVERDUE("逾期");

        RentStatus(String status) {
        }
    }

    public enum ProtocolStatus{
        INACTIVED ("尚未生效"), ACTIVED("生效中或有欠缺"), FINISHED("已完成未欠款");

        ProtocolStatus(String status) {
        }
    }

    public String id;

    public String sourceId;

    public String ownerId;

    public ProtocolType type;

    public long createTime;

    public String roomNum;

    public float monthlyRent;

    public int payCycle;

    public long beginTime;

    public long endTime;

    public long imminentPayTime;

    public boolean renewed;

    public String companyName;

    public boolean lockAuthorized; //是否启用锁权限，关联信息

    public RentStatus rentStatus;  //缴租状态

    public ProtocolStatus status;  //租赁协议状态

    public String sourceName;     //房源名称

    public String sourceNum;      //房源编号

    public String pictures;        // 合同图片

    public String rentName;        //租客姓名

    public String rentSex;         //租客性别

    public String rentJob;         //租客职业

    public String rentPhone;       //租客手机号

    public String rentIdNo;        //租客身份证号

}
