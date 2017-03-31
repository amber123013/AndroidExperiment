package cn.ambermoe.memo.data;

/**
 * Created by ASUS on 2017-03-31.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**Memo数据库 合约类*/
public final class MemoContract {

    /**与Mainifest中定义的一致*/
    public static final String CONTENT_AUTHORITY = "cn.ambermoe.memo";

    /**
     *content:// scheme + content_authority
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * 其中一个可能路径
     * 这里是memo表
     */
    public static final String PATH_MEMO = "memo";

    public MemoContract() {

    }
    /**每一个内部类表示memo数据库中的一个表 这个表也叫memo*/
    public static final class MemoEntry implements BaseColumns {
        /**
         * CURSOR_DIR_BASE_TYPE（映射到常数“vnd.android.cursor.dir”）
         * CURSOR_ITEM_BASE_TYPE（映射到常数“vnd.android.cursor.item”）
         */
        /**
         * mime类型 为memo列表
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEMO;

        /**
         * mime类型 为单个memo
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MEMO;
        /** 访问provider 中数据 使用的Uri */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMO);
        /**表名*/
        public final static String TABLE_NAME = "memo";

        //id(仅用在表中).
        public final static String _ID = BaseColumns._ID;

        //存入时间 (存时间戳)
        public final static String COLUMN_MEMO_DEPOSIT_TIME = "time";

        //存储内容
        public final static String COLUMN_MEMO_DEPOSIT_CONTENT = "content";
    }
}
