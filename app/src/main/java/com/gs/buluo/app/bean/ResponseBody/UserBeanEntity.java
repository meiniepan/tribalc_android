package com.gs.buluo.app.bean.ResponseBody;

/**
 * Created by hjn on 2016/11/9.
 */
public class UserBeanEntity {
    private String expired;
    private String assigned;
    private String token;
    private UserActivities activities;

    public UserActivities getActivities() {
        return activities;
    }

    public void setActivities(UserActivities activities) {
        this.activities = activities;
    }

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
