package cn.ambermoe.memo.data;

/**
 * Created by ASUS on 2017-03-31.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**Provider 封装数据库调用逻辑*/
public class MemoProvider extends ContentProvider{

    /** tag */
    public static final String LOG_TAG = MemoProvider.class.getSimpleName();

    /**查询memo表格的URI matcher code*/
    private static final int MEMO = 100;
    /**查询memo表格中单个记录的URI matcher code*/
    private static final int MEMO_ID = 101;

    /**urimatcher用于匹配contentURI
     *到相应的 URI matcher code
     * 如100 查询memo表的所有数据
     */

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //静态代码块 只初始化一次
    static {
        /**uri 及其对应的matcher code*/
        sUriMatcher.addURI(MemoContract.CONTENT_AUTHORITY,MemoContract.PATH_MEMO,MEMO);
        //#匹配一个数
        sUriMatcher.addURI(MemoContract.CONTENT_AUTHORITY,MemoContract.PATH_MEMO + "/#",MEMO_ID);
    }

    private MemoDbHelper mDbHelper;

    /**
     * 初始化Provider 和数据库辅助对象MemoDbHelper
     * Android 系统会在创建提供程序(Provider)后立即调用此方法。
     * ContentResolver 对象尝试访问提供程序时，系统才会创建它
     */
    @Override
    public boolean onCreate() {
        // 初始化一个 PetDBHelper 对象访问数据库
        mDbHelper = new MemoDbHelper(getContext());
        return true;
    }

    /**
     * 执行给定的URI查询使用给定的
     * projection, 返回的字段
     * selection,selection arguments where XX = XX
     * sort order. paixu
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // 获得readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // 保存查询的返回值
        Cursor cursor = null;

        // 找出匹配的uri  code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMO:
                /**查询宠物列表
                 * projection 返回的列
                 */
                cursor  = database.query(MemoContract.MemoEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MEMO_ID:
                // 从uri获取要查询的id
                // 下面两句解析相当于 where _ID = #
                selection = MemoContract.MemoEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // 执行查询
                cursor = database.query(MemoContract.MemoEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("未能识别出此URI " + uri);
        }
        /**在cursor上设置通知uri
         *当此uri指示的数据发生变化时 就能得知需要更新cursor
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * 返回内容 URI 对应的 MIME 类型
     * content://com.example.android.pets/memo → Returns directory MIME type 数据目录
     * content://com.example.android.pets/memo/# → Returns item MIME type 单个数据行即单个数据项
     * MIME 类型字符串按约定以“vnd.android.cursor…”开头，后面跟宠物内容主机名，以及数据路径
     * 目录 MIME 类型： vnd.android.cursor.dir/com.example.android.memo/memo
     * 项 MIME 类型： vnd.android.cursor.item/com.example.android.memo/memo
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMO:
                return MemoContract.MemoEntry.CONTENT_LIST_TYPE;
            case MEMO_ID:
                return MemoContract.MemoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("未识别 URI " + uri + "    " + match);
        }
    }
    /**
     * 使用contentValues插入新的内容
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMO:
                return insertMemo(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    //插入数据
    private Uri insertMemo(Uri uri,ContentValues values) {
        /**
         * 插入之前 对数据进行完整性检查
         */
        //内容
        String content = values.getAsString(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT);
        if (content == null) {
            throw new IllegalArgumentException("内容不能为空");
        }
        /**
         * 数据验证通过，执行插入操作
         */

        // 创建或者打开数据库 读取
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //插入成功 返回id号
        long id = db.insert(MemoContract.MemoEntry.TABLE_NAME, null, values);
        //返回添加id的uri
        if (id == -1) { //添加失败
            Log.e(LOG_TAG, "添加失败 " + uri);
            //返回空的uri
            return null;
        }
        //插入成功 表格已被修改
        // 通知监听器 数据已经被修改
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }
    /**
     * 删除内容 使用给定的 selection and selectionArgs
     *
     * URI: content://com.example.android.pets/pets
     *Selection: “id=?”
     *SelectionArgs: { 2 }
     *==DELETE pets WHERE id= 2
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case MEMO:
                // 删除所有数据
                rowsDeleted = database.delete(MemoContract.MemoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MEMO_ID:

               // 根据id 删除单个记录
                selection = MemoContract.MemoEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                Toast.makeText(getContext(),selection +""+selectionArgs[0],Toast.LENGTH_SHORT).show();
                //删除一个记录
                rowsDeleted = database.delete(MemoContract.MemoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("此URI不可用于删除 " + uri);
        }
        //有数据被删除
        if (rowsDeleted != 0) {
            //通知监听器数据发生变化
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // 返回被删除的行数
        return rowsDeleted;
    }
    /**
     * 使用给定的内容更新数据
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEMO:
                return updateMemo(uri, values, selection, selectionArgs);
            case MEMO_ID:
                //根据uri获取要修改的行id
                selection = MemoContract.MemoEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMemo(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("此URI不可用于更新 " + uri);
        }
    }

    //更新数据
    private int updateMemo(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //如果不存在参数 return 0
        if (values.size() == 0) {
            return 0;
        }

        //是否包含内容 -- 内容是否为空
        if (values.containsKey(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT)) {
            String content = values.getAsString(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT);
            if (content == null) {
                throw new IllegalArgumentException("内容不能为空");
            }
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        //受影响的行数
        int rowsUpdated = database.update(MemoContract.MemoEntry.TABLE_NAME, values, selection, selectionArgs);
        //表格被update 通知监听器 数据已经被修改
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // 返回受影响的行数
        return rowsUpdated;

    }
}