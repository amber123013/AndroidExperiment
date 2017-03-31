package cn.ambermoe.memo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.ambermoe.memo.data.MemoContract.MemoEntry;

/**
 * Created by ASUS on 2017-03-31.
 */

public class MemoDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MemoDbHelper.class.getSimpleName();

    /** 数据库名 */
    private static final String DATABASE_NAME = "memo.db";

    /**
     * 数据库版本，在修改数据库架构时修改此版本
     */
    private static final int DATABASE_VERSION = 1;
    /**构建一个PetDbHelper*/
    public MemoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建memo表的sq语句
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + MemoEntry.TABLE_NAME + " ("
                + MemoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT + " TEXT NOT NULL, "
                + MemoEntry.COLUMN_MEMO_DEPOSIT_TIME + " TEXT NOT NULL);";

        // 执行sql语句
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
