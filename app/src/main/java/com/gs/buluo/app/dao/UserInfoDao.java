package com.gs.buluo.app.dao;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserInfoEntity;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
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
            db.delete(UserInfoEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveBindingId(UserInfoEntity userInfo){
        try {
            db.saveBindingId(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void update(UserInfoEntity userInfo){
        TribeApplication.getInstance().setUserInfo(userInfo);
        try {
            db.update(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public UserInfoEntity findFirst(){
        try {
            return db.findFirst(UserInfoEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserInfoEntity find(String assigned) {
        try {
            return db.selector(UserInfoEntity.class).where(WhereBuilder.b("uid", "=", assigned)).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
