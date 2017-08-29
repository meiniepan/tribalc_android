package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/8/25.
 */

public class PushMessageBean {
    public PushMessageBody message;

    public class PushMessageBody {
        public HomeMessageEnum messageBodyType;
        public String body;
        public String referenceId;
    }
}
