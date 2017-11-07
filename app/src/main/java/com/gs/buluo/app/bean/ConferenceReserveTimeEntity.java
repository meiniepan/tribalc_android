package com.gs.buluo.app.bean;

/**
 * Created by Solang on 2017/10/20.
 */

public class ConferenceReserveTimeEntity {
    public long date;
    public int start;
    public int end;
    public boolean checked;
    public boolean isHaveBegin;

    public ConferenceReserveTimeEntity(long startDate) {
        date = startDate;
    }
}
