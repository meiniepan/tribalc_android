package com.gs.buluo.app.dao;


import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserAddressEntity;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by hjn on 2016/11/3.
 */
public class AddressInfoDao {
    private DbManager db;

    public AddressInfoDao(){
        db = x.getDb(TribeApplication.getInstance().getDaoConfig());
    }

    public void clear(){
        try {
            db.delete(AddressInfoDao.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveBindingId(UserAddressEntity addressInfo){
        try {
            db.saveBindingId(addressInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void update(UserAddressEntity addressInfo){
        try {
            db.update(addressInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public UserAddressEntity findFirst(String uid){
        try {
            return db.findFirst(UserAddressEntity.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserAddressEntity find(String uid,String aid){
        try {
            return db.selector(UserAddressEntity.class).where(WhereBuilder.b("address_id","=",aid).and(WhereBuilder.b("uid","=",uid))).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<UserAddressEntity> findAll(String uid) {
        try {
            return db.selector(UserAddressEntity.class).where(WhereBuilder.b("uid", "=", uid)).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAddress(UserAddressEntity entity) {
        try {
             db.delete(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
