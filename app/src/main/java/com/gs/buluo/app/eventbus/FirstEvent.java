package com.gs.buluo.app.eventbus;

/**
 * Created by hjn on 2016/11/9.
 */
public class FirstEvent {
        private String mMsg;
        public FirstEvent(String msg) {
            mMsg = msg;
        }
        public String getMsg(){
            return mMsg;
        }
}
