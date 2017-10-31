package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/10/31.
 */

public class BoardroomOrder {
    public String id;
    public String reservationNum;
    public String name;
    public String floor;
    public String picture;
    public String totalFee;
    public long conferenceBeginTime;
    public long conferenceEndTime;
    public BoardroomOrderStatus status;

    enum BoardroomOrderStatus {
        RESERVED("已预定"), CANCEL("已取消"), FINISHED("已完成");

        BoardroomOrderStatus(String status) {
        }
    }
}
