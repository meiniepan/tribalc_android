package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.ConferenceRoom;

import java.util.List;

/**
 * Created by hjn on 2017/10/25.
 */

public class ConferenceRoomResponse {
    public String nextSkip;
    public String preSkip;
    public boolean hasMore;
    public List<ConferenceRoom> content;
}
