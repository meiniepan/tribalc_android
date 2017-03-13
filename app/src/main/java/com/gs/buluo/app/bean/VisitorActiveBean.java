package com.gs.buluo.app.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/3/10.
 */

public class VisitorActiveBean {
    public String phone;
    public String name;
    public List<LockKey> keys;

    public VisitorActiveBean(String name, String phone, ArrayList<LockKey> keys) {
        this.phone = phone;
        this.name = name;
        this.keys = keys;
    }
}
