package com.gs.buluo.app.dao;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by hjn on 2016/11/3.
 */
public class UserSensitiveDao {
    private DbManager db;

    public UserSensitiveDao(){
        db = x.getDb(TribeApplication.getInstance().getDaoConfig());
    }

    public void clear(){
        try {
            db.delete(UserSensitiveEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveBindingId(UserSensitiveEntity userInfo){
        try {
            db.saveBindingId(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void update(UserSensitiveEntity userInfo){
        try {
            db.update(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public UserSensitiveEntity findFirst(){
        try {
            return db.findFirst(UserSensitiveEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserSensitiveEntity find(String assigned) {
        try {
            return db.selector(UserSensitiveEntity.class).where(WhereBuilder.b("uid", "=", assigned)).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
