package com.gs.buluo.app.dao;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserInfo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by hjn on 2016/11/3.
 */
public class UserInfoDao{
    private DbManager db;

    public UserInfoDao(){
        db = x.getDb(TribeApplication.getInstance().getDaoConfig());
    }

    public void clear(){
        try {
            db.delete(UserInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveBindingId(UserInfo userInfo){
        try {
            db.saveBindingId(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void update(UserInfo userInfo){
        try {
            db.update(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public UserInfo findFirst(){
        try {
            return db.findFirst(UserInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
