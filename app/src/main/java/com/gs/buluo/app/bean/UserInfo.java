package com.gs.buluo.app.bean;

import com.alibaba.fastjson.JSON;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by hjn on 2016/11/3.
 */

@Table(name="user_info")
public class UserInfo {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "uid")
    private long uid;
    @Column(name = "iid")
    private String iid;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

}
