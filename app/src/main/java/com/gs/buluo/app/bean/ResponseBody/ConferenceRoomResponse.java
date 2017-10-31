package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.BoardroomOrder;
import com.gs.buluo.app.bean.ConferenceRoom;

import java.util.List;

/**
 * Created by hjn on 2017/10/25.
 */

public class ConferenceRoomResponse {
    public String nextSkip;
    public String prevSkip;
    public List<BoardroomOrder> content;
    public boolean hasMore;
}
