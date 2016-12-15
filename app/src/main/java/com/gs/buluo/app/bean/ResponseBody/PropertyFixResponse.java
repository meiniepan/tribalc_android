package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.PropertyManagement;

/**
 * Created by fs on 2016/12/13.
 */
public class PropertyFixResponse {
    public int code;
    public PropertyManagement mPropertyManagement;

    @Override
    public String toString() {
        return "PropertyFixResponse{" +
                "code=" + code +
                ", mPropertyManagement=" + mPropertyManagement +
                '}';
    }
}
