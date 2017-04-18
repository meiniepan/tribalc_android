package com.gs.buluo.app.bean.ResponseBody;

/**
 * Created by hjn on 2016/11/9.
 */
public class UserBeanEntity {
        /**
         * expired : null
         * assigned : 5823d0240cf27d4aed427dbe
         * token : 2937cef8-2260-44bc-a70b-d690cf238368
         */
        private String expired;
        private String assigned;
        private String token;

        public void setExpired(String expired) {
            this.expired = expired;
        }

        public void setAssigned(String assigned) {
            this.assigned = assigned;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExpired() {
            return expired;
        }

        public String getAssigned() {
            return assigned;
        }

        public String getToken() {
            return token;
        }
    }
