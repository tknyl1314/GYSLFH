package com.titan.data.source.local;

import android.content.Context;

import com.titan.data.greendao.DaoMaster;
import com.titan.data.greendao.DaoSession;
import com.titan.gyslfh.TitanApplication;

/**
 * Created by whs on 2017/5/9
 */

public class GreenDaoManager {
    //数据库名称
    private final  String  DBNAME="TITAN_DB_SLFH";

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;

    private Context mContext;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static volatile GreenDaoManager mInstance = null;
    private GreenDaoManager(Context context){
        if (mInstance == null) {

            /*DaoMaster.DevOpenHelper helper = new DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
            Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
            daoSession = new DaoMaster(db).newSession();*/
            //DBNAME=mContext.getString(R.string.dbname);
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(TitanApplication.getContext(), DBNAME);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
            this.mContext=context;
        }
    }
    public static GreenDaoManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                mInstance = new GreenDaoManager(context);
            }
            }
        }
        return mInstance;
    }
    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
