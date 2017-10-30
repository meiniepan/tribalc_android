package com.gs.buluo.app.bean.RequestBodyBean;

import com.gs.buluo.app.bean.ContactsPersonEntity;

import java.util.List;

/**
 * Created by Solang on 2017/10/30.
 */

public class BoardroomReserveEntity {
    public String reminderTime;
    public int attendance;//与会人数
    public long conferenceBeginTime;
    public long conferenceEndTime;
    public String subject;
    public List<ContactsPersonEntity> conferenceParticipants;
}
