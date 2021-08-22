package com.yumore.utility.greendao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nathaniel.utility.BuildConfig;
import com.yumore.provider.EmptyUtils;
import com.yumore.provider.MigrationHelper;
import com.yumore.provider.SQLiteOpenHelper;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * @author Nathaniel
 * @date 2020/1/4 - 15:11
 */
public class DaoManager {
    private static final String DATABASE_DEFAULT = "Nathaniel.db";
    @SuppressLint("StaticFieldLeak")
    private static volatile DaoManager daoManager;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    @SuppressLint("StaticFieldLeak")
    private static DaoMaster.DevOpenHelper devOpenHelper;
    private Context context;

    private DaoManager() {
        if (BuildConfig.DEBUG) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        } else {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    public static synchronized DaoManager getInstance() {
        if (EmptyUtils.isEmpty(daoManager)) {
            daoManager = new DaoManager();
        }
        return daoManager;
    }

    public void initialized(Context context) {
        this.context = context;
        MigrationHelper.DEBUG = BuildConfig.debuggable;
        SQLiteOpenHelper helper = new SQLiteOpenHelper(context, DATABASE_DEFAULT, null);
        SQLiteDatabase database = helper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public DaoMaster getDaoMaster() {
        if (EmptyUtils.isEmpty(daoMaster)) {
            devOpenHelper = new DaoMaster.DevOpenHelper(context, DATABASE_DEFAULT, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        if (EmptyUtils.isEmpty(daoSession)) {
            daoSession = getDaoMaster().newSession();
        }
        return daoSession;
    }

    private void closeHelper() {
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    private void closeSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    public void closeConnection() {
        closeHelper();
        closeSession();
    }
}
