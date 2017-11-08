package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.ConferenceReservation;

import java.util.List;

/**
 * Created by hjn on 2017/11/8.
 */

public class CompanyBoardroomResponse {
    public List<ConferenceReservation> content;
    public boolean hasMore;
    public String nextSkip;
}
