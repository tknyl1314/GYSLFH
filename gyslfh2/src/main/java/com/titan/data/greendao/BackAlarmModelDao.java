package com.titan.data.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.titan.gyslfh.backalarm.BackAlarmModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BACK_ALARM_MODEL".
*/
public class BackAlarmModelDao extends AbstractDao<BackAlarmModel, Void> {

    public static final String TABLENAME = "BACK_ALARM_MODEL";

    /**
     * Properties of entity BackAlarmModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property BACKID = new Property(0, String.class, "BACKID", false, "BACKID");
        public final static Property RECEPID = new Property(1, String.class, "RECEPID", false, "RECEPID");
        public final static Property USERID = new Property(2, String.class, "USERID", false, "USERID");
        public final static Property DQID = new Property(3, String.class, "DQID", false, "DQID");
        public final static Property DQNAME = new Property(4, String.class, "DQNAME", false, "DQNAME");
        public final static Property CHECKER = new Property(5, String.class, "CHECKER", false, "CHECKER");
        public final static Property BACKTIME = new Property(6, String.class, "BACKTIME", false, "BACKTIME");
        public final static Property BACKSTATUS = new Property(7, String.class, "BACKSTATUS", false, "BACKSTATUS");
        public final static Property ISFIRE = new Property(8, String.class, "ISFIRE", false, "ISFIRE");
        public final static Property FIRETYPE = new Property(9, String.class, "FIRETYPE", false, "FIRETYPE");
        public final static Property FIRESIUATION = new Property(10, String.class, "FIRESIUATION", false, "FIRESIUATION");
        public final static Property POLICESIUATION = new Property(11, String.class, "POLICESIUATION", false, "POLICESIUATION");
        public final static Property BELONGAREAID = new Property(12, String.class, "BELONGAREAID", false, "BELONGAREAID");
        public final static Property BELONGAREANAME = new Property(13, String.class, "BELONGAREANAME", false, "BELONGAREANAME");
        public final static Property INRELATION = new Property(14, String.class, "INRELATION", false, "INRELATION");
        public final static Property REMARK = new Property(15, String.class, "REMARK", false, "REMARK");
        public final static Property REMARK1 = new Property(16, String.class, "REMARK1", false, "REMARK1");
        public final static Property REMARK2 = new Property(17, String.class, "REMARK2", false, "REMARK2");
    }


    public BackAlarmModelDao(DaoConfig config) {
        super(config);
    }
    
    public BackAlarmModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BACK_ALARM_MODEL\" (" + //
                "\"BACKID\" TEXT," + // 0: BACKID
                "\"RECEPID\" TEXT," + // 1: RECEPID
                "\"USERID\" TEXT," + // 2: USERID
                "\"DQID\" TEXT," + // 3: DQID
                "\"DQNAME\" TEXT," + // 4: DQNAME
                "\"CHECKER\" TEXT," + // 5: CHECKER
                "\"BACKTIME\" TEXT," + // 6: BACKTIME
                "\"BACKSTATUS\" TEXT," + // 7: BACKSTATUS
                "\"ISFIRE\" TEXT," + // 8: ISFIRE
                "\"FIRETYPE\" TEXT," + // 9: FIRETYPE
                "\"FIRESIUATION\" TEXT," + // 10: FIRESIUATION
                "\"POLICESIUATION\" TEXT," + // 11: POLICESIUATION
                "\"BELONGAREAID\" TEXT," + // 12: BELONGAREAID
                "\"BELONGAREANAME\" TEXT," + // 13: BELONGAREANAME
                "\"INRELATION\" TEXT," + // 14: INRELATION
                "\"REMARK\" TEXT," + // 15: REMARK
                "\"REMARK1\" TEXT," + // 16: REMARK1
                "\"REMARK2\" TEXT);"); // 17: REMARK2
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BACK_ALARM_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BackAlarmModel entity) {
        stmt.clearBindings();
 
        String BACKID = entity.getBACKID();
        if (BACKID != null) {
            stmt.bindString(1, BACKID);
        }
 
        String RECEPID = entity.getRECEPID();
        if (RECEPID != null) {
            stmt.bindString(2, RECEPID);
        }
 
        String USERID = entity.getUSERID();
        if (USERID != null) {
            stmt.bindString(3, USERID);
        }
 
        String DQID = entity.getDQID();
        if (DQID != null) {
            stmt.bindString(4, DQID);
        }
 
        String DQNAME = entity.getDQNAME();
        if (DQNAME != null) {
            stmt.bindString(5, DQNAME);
        }
 
        String CHECKER = entity.getCHECKER();
        if (CHECKER != null) {
            stmt.bindString(6, CHECKER);
        }
 
        String BACKTIME = entity.getBACKTIME();
        if (BACKTIME != null) {
            stmt.bindString(7, BACKTIME);
        }
 
        String BACKSTATUS = entity.getBACKSTATUS();
        if (BACKSTATUS != null) {
            stmt.bindString(8, BACKSTATUS);
        }
 
        String ISFIRE = entity.getISFIRE();
        if (ISFIRE != null) {
            stmt.bindString(9, ISFIRE);
        }
 
        String FIRETYPE = entity.getFIRETYPE();
        if (FIRETYPE != null) {
            stmt.bindString(10, FIRETYPE);
        }
 
        String FIRESIUATION = entity.getFIRESIUATION();
        if (FIRESIUATION != null) {
            stmt.bindString(11, FIRESIUATION);
        }
 
        String POLICESIUATION = entity.getPOLICESIUATION();
        if (POLICESIUATION != null) {
            stmt.bindString(12, POLICESIUATION);
        }
 
        String BELONGAREAID = entity.getBELONGAREAID();
        if (BELONGAREAID != null) {
            stmt.bindString(13, BELONGAREAID);
        }
 
        String BELONGAREANAME = entity.getBELONGAREANAME();
        if (BELONGAREANAME != null) {
            stmt.bindString(14, BELONGAREANAME);
        }
 
        String INRELATION = entity.getINRELATION();
        if (INRELATION != null) {
            stmt.bindString(15, INRELATION);
        }
 
        String REMARK = entity.getREMARK();
        if (REMARK != null) {
            stmt.bindString(16, REMARK);
        }
 
        String REMARK1 = entity.getREMARK1();
        if (REMARK1 != null) {
            stmt.bindString(17, REMARK1);
        }
 
        String REMARK2 = entity.getREMARK2();
        if (REMARK2 != null) {
            stmt.bindString(18, REMARK2);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BackAlarmModel entity) {
        stmt.clearBindings();
 
        String BACKID = entity.getBACKID();
        if (BACKID != null) {
            stmt.bindString(1, BACKID);
        }
 
        String RECEPID = entity.getRECEPID();
        if (RECEPID != null) {
            stmt.bindString(2, RECEPID);
        }
 
        String USERID = entity.getUSERID();
        if (USERID != null) {
            stmt.bindString(3, USERID);
        }
 
        String DQID = entity.getDQID();
        if (DQID != null) {
            stmt.bindString(4, DQID);
        }
 
        String DQNAME = entity.getDQNAME();
        if (DQNAME != null) {
            stmt.bindString(5, DQNAME);
        }
 
        String CHECKER = entity.getCHECKER();
        if (CHECKER != null) {
            stmt.bindString(6, CHECKER);
        }
 
        String BACKTIME = entity.getBACKTIME();
        if (BACKTIME != null) {
            stmt.bindString(7, BACKTIME);
        }
 
        String BACKSTATUS = entity.getBACKSTATUS();
        if (BACKSTATUS != null) {
            stmt.bindString(8, BACKSTATUS);
        }
 
        String ISFIRE = entity.getISFIRE();
        if (ISFIRE != null) {
            stmt.bindString(9, ISFIRE);
        }
 
        String FIRETYPE = entity.getFIRETYPE();
        if (FIRETYPE != null) {
            stmt.bindString(10, FIRETYPE);
        }
 
        String FIRESIUATION = entity.getFIRESIUATION();
        if (FIRESIUATION != null) {
            stmt.bindString(11, FIRESIUATION);
        }
 
        String POLICESIUATION = entity.getPOLICESIUATION();
        if (POLICESIUATION != null) {
            stmt.bindString(12, POLICESIUATION);
        }
 
        String BELONGAREAID = entity.getBELONGAREAID();
        if (BELONGAREAID != null) {
            stmt.bindString(13, BELONGAREAID);
        }
 
        String BELONGAREANAME = entity.getBELONGAREANAME();
        if (BELONGAREANAME != null) {
            stmt.bindString(14, BELONGAREANAME);
        }
 
        String INRELATION = entity.getINRELATION();
        if (INRELATION != null) {
            stmt.bindString(15, INRELATION);
        }
 
        String REMARK = entity.getREMARK();
        if (REMARK != null) {
            stmt.bindString(16, REMARK);
        }
 
        String REMARK1 = entity.getREMARK1();
        if (REMARK1 != null) {
            stmt.bindString(17, REMARK1);
        }
 
        String REMARK2 = entity.getREMARK2();
        if (REMARK2 != null) {
            stmt.bindString(18, REMARK2);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public BackAlarmModel readEntity(Cursor cursor, int offset) {
        BackAlarmModel entity = new BackAlarmModel( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // BACKID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // RECEPID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // USERID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // DQID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // DQNAME
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // CHECKER
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // BACKTIME
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // BACKSTATUS
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // ISFIRE
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // FIRETYPE
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // FIRESIUATION
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // POLICESIUATION
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // BELONGAREAID
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // BELONGAREANAME
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // INRELATION
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // REMARK
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // REMARK1
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17) // REMARK2
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BackAlarmModel entity, int offset) {
        entity.setBACKID(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setRECEPID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUSERID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDQID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDQNAME(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCHECKER(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBACKTIME(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setBACKSTATUS(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setISFIRE(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFIRETYPE(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFIRESIUATION(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPOLICESIUATION(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setBELONGAREAID(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setBELONGAREANAME(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setINRELATION(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setREMARK(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setREMARK1(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setREMARK2(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(BackAlarmModel entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(BackAlarmModel entity) {
        return null;
    }

    @Override
    public boolean hasKey(BackAlarmModel entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
