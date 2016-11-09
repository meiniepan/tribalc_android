package com.gs.buluo.app;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gs.buluo.app.utils.FreImageLoader;

import org.xutils.DbManager;
import org.xutils.x;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by hjn on 2016/11/1.
 */
public class TribeApplication extends Application {
    private static TribeApplication instance;
    private DbManager.DaoConfig daoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        x.Ext.init(this);//X utils初始化
        x.Ext.setDebug(BuildConfig.DEBUG);
        Fresco.initialize(this);
        initGallery();
        initDb();
    }

    private void initGallery() {
        ThemeConfig theme = new ThemeConfig.Builder()
        .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .setEnablePreview(true)
        .build();

        //配置 imageloader
        ImageLoader imageloader = new FreImageLoader(this);
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig)
        .build();
        GalleryFinal.init(coreConfig);
    }

    private void initDb() {
        daoConfig=new DbManager.DaoConfig()
                .setDbName("tribe")
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging(); //WAL
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() { //更新数据库
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                });
    }

    public static synchronized TribeApplication getInstance(){
        return instance;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
}
