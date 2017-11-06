package com.gs.buluo.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/11/2.
 */

public class ConferenceReserveDetail {
    public String id;
    public String name;
    public String floor;
    public String communityName;
    public String galleryful;
    public String maxGalleryful;
    public String personId;
    public String personName;
    public String personPhone;
    public String subject;
    public String reservationNum;
    public String totalFee;
    public String picture;
    public long createTime;
    public long conferenceBeginTime;
    public long conferenceEndTime; //会议实际结束时间
    public long planEndTime;//会议原定结束时间
    public long openTime;
    public long closeTime;
    public List<ConferenceEquipment> equipmentList;
    public ArrayList<ContactsPersonEntity> conferenceParticipants;
    public ConferenceReservation.BoardroomOrderStatus status;
    public long reminderTime;

}
