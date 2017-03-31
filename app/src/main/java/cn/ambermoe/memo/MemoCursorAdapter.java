package cn.ambermoe.memo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ambermoe.memo.data.MemoContract.MemoEntry;
/**
 * Created by ASUS on 2017-03-31.
 */

/**
 * CursorAdapter 使用cursor作为数据源
 * 为listview提供列表项视图
 */
public class MemoCursorAdapter extends CursorAdapter{

    public MemoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     *新建一个空的视图，上面没有数据
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    /**
     * 将Cursor中当前行的数据绑定到列表项视图
     * 如 设置text的值
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 查找要修改的视图
        TextView contentTextView = (TextView) view.findViewById(R.id.content);
        TextView timeTextView = (TextView) view.findViewById(R.id.time);

        // 查找 需要的宠物属性所在的列
        int contentColumnIndex = cursor.getColumnIndex(MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT);
        int timeColumnIndex = cursor.getColumnIndex(MemoEntry.COLUMN_MEMO_DEPOSIT_TIME);

        // 从cursor获取宠物属性
        String content = cursor.getString(contentColumnIndex);
        //字符串转long再转Date
        Date date = new Date(Long.parseLong(cursor.getString(timeColumnIndex)));
        String strTime;
        //如果是当天显示 分钟 否则显示月日
        if(formatDate(date).equals(formatDate(new Date()))) {
            strTime = formatTime(date);
        } else {
            strTime = formatDate(date);
        }
        contentTextView.setText(content);
        timeTextView.setText(strTime);
    }

    /**
     * 从 Date 对象返回格式化的日期字符串
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLLdd日");
        return dateFormat.format(dateObject);
    }
    /**
     * 从 Date 对象返回格式化的时间字符串
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("a h:mm");
        return timeFormat.format(dateObject);
    }
}
