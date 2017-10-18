package kolyae318rub.shoplist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB {

    public static final String DATABASE_NAME = "shopListDB.db";
    public static final int DATABASE_VERSION = 1;
    public static final String MAIN_TABLE_NAME = "mainTable";
    public static final String KEY_ID = "_id";
    public static final String KEY_SLOT = "slot";
    public static final String KEY_LIST = "list";
    public static final String KEY_STATUS = "status";
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public DB(Context context) {
        mCtx = context;
    }

    public void openDB() {
        mDBHelper = new DBHelper(mCtx);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void closeDB() {
        if (mDBHelper != null) {
            mDBHelper.close();
        }
    }

    public Cursor getMainActivityCursor() {
        if (mDBHelper == null) {
            openDB();
        } else mDatabase = mDBHelper.getWritableDatabase();
        return mDatabase.query(MAIN_TABLE_NAME, null, null, null, null, null, null);
    }

    public void putListIntoDB(ArrayList<String> list, String status, String slName) {
        openDB();
        mDatabase.execSQL("DROP TABLE IF EXISTS " + slName + ";");
        ContentValues contentValues = new ContentValues();
        ContentValues contentValuesList = new ContentValues();
        mDatabase.execSQL("CREATE TABLE " + slName + " (" + DB.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DB.KEY_SLOT + " TEXT);");

        for (int i = 0; i < list.size(); i++) {
            contentValues.put(DB.KEY_SLOT, list.get(i));
            mDatabase.insert(slName, null, contentValues);
        }
        contentValuesList.put(DB.KEY_LIST, slName);
        contentValuesList.put(DB.KEY_STATUS, status);
        mDatabase.insert(DB.MAIN_TABLE_NAME, null, contentValuesList);
        closeDB();
    }

    public ArrayList<String> getListFromDB(String tableName) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(DB.KEY_SLOT)));
        }
        cursor.close();
        closeDB();
        return list;
    }

    public void deleteList(String slName) {
        openDB();
        mDatabase.execSQL("DROP TABLE IF EXISTS " + slName + ";");
        mDatabase.delete(MAIN_TABLE_NAME, KEY_LIST + " = '" + slName + "'", null);
        closeDB();
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DB.MAIN_TABLE_NAME + " (" + DB.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DB.KEY_LIST + " TEXT, " + DB.KEY_STATUS + " TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}