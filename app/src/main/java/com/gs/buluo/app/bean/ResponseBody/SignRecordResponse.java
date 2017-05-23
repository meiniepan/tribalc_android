package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.SignRecord;

import java.util.List;

/**
 * Created by hjn on 2017/5/19.
 */

public class SignRecordResponse {
    public long lastTimestamp;
    public String continuityDays;
    public int monthNumber;
    public List<SignRecord> monthRecords;
}
