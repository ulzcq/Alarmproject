package com.yu.alarmproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import static java.sql.DriverManager.println;

public class Database {
    private static final String TAG = "Database";

    private static Database database;
    public static int DATABASE_VERSION = 1;

    public static String TABLE_ALARM = "ALARM";
    public static String TABLE_LATENESS = "LATENESS";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private Database(Context context){
        this.context = context;
    }

    public static Database getInstance(Context context){
        if(database == null){
            database = new Database(context);
    }
        return database;
    }

    public boolean open(){
        Log.d(TAG,"opening database [" + Constants.DATABASE_NAME+"].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }

    public void close(){
        Log.d(TAG,"closing database [" + Constants.DATABASE_NAME+"].");
        db.close();

        database = null;
    }

    public Cursor rawQuery(String SQL){
        Log.d(TAG,"\nexecuteQuery called\n");

        Cursor cursor = null;
        try{
            cursor = db.rawQuery(SQL,null);
            Log.d(TAG,"cursor count: " + cursor.getCount());
        } catch(Exception e){
            Log.e(TAG,"Exception in excuteQuery",e);
        }
        return cursor;
    }

    public boolean execSQL(String SQL){
        Log.d(TAG,"\nexecute called.\n");

        try{
            db.execSQL(SQL);
        }catch(Exception e){
            Log.e(TAG,"Exception in excuteQuery",e);
            return false;
        }
        return true;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context){
            super(context, Constants.DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL("drop table if exists "+TABLE_ALARM);
                db.execSQL("drop table if exists "+TABLE_LATENESS);
            }catch(Exception e){
                Log.e(TAG, "Exception in DORP_SQL",e);
            }

            String CREATE_SQL1 = "create table "+TABLE_ALARM + "("
                    +" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    +" LABLE TEXT DEFAULT '일정', "

                    +" SCHEDTIME TEXT NOT NULL, "
                    +" READY_ALARMTIME TEXT, "
                    +" GOOUT_ALARMTIME TEXT, "

                    +" REDAY_ENABLED INTEGER DEFAULT 0,"
                    +" GOOUT_ENABLED INTEGERT DEFAULT 0,"
                    +" SCHED_ENABLED INTEGER DEFAULT 0, "

                    +" VIEWTYPE INTEGER NOT NULL, "

                    +" READY_H INEGER, "
                    +" READY_M INEGER, "
                    +" MOVE_H INEGER, "
                    +" MOVE_M INEGER "
                    + ")";

            String CREATE_SQL2 = "create table "+TABLE_LATENESS + "("
                    +" _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    +" LABLE TEXT DEFAULT '일정', "
                    +" SCHEDTIME TEXT NOT NULL, " //일정 calendar -> 요일포함돼있음
                    +" LATENESS INT DEFAULT 0 " //지각안한게 default
                    + ")";

            try{
                db.execSQL(CREATE_SQL1);
                db.execSQL(CREATE_SQL2);
            }catch(Exception e){
                Log.e(TAG, "Exception in CREATE_SQL",e);
            }
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            Log.d(TAG,"opended database ["+Constants.DATABASE_NAME+"].");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG,"Upgrading database from version " + oldVersion + " to " + newVersion +".");
        }//필수라서 어쩔수없..
    }
}
