package cn.ambermoe.memo;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import cn.ambermoe.memo.data.MemoContract.MemoEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    //长按列表项获得的id（数据库中的 必须使用CursorAdapter）
    private long mId ;
    /** 用于loader的标示符 只要数字是唯一就行 */
    private static final int MEMO_LOADER = 0;

    //内容数据集合
    public ArrayList<String> mContent =  new ArrayList<>();
    //id集合
    public ArrayList<Integer> mContentId = new ArrayList<>();
    /** listview的Adapter */
    public MemoCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView memoListView = (ListView) findViewById(R.id.list);

        //listview无数据时显示内容
        View emptyView = findViewById(R.id.empty_view);
        memoListView.setEmptyView(emptyView);
        //设置数据
        mCursorAdapter = new MemoCursorAdapter(this, null);
        memoListView.setAdapter(mCursorAdapter);
        /**此长按列表项监听事件只是为了获取被点击列表项在数据库中的id
         *为mId赋值供 长按 浮动上下文菜单 使用
         */
        memoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mId = id;
                return false;
            }
        });
        memoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailPagerActivity.class);
                intent.putExtra("currentPosition",position);
                intent.putStringArrayListExtra("content",mContent);
                intent.putIntegerArrayListExtra("contentId",mContentId);
                startActivity(intent);
            }
        });
        //浮动上下文菜单
        //注册后的视图收到长按事件时，系统将调用 onCreateContextMenu() 方法
        registerForContextMenu(memoListView);
        // 点击fab打开 EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        // 启动loader
        getLoaderManager().initLoader(MEMO_LOADER, null, this);
    }
    //视图收到长按事件时 调用
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.content_item, menu);

    }
    //浮动上下文菜单
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
        switch (item.getItemId()) {
            case R.id.content_delete:
                Toast.makeText(MainActivity.this,position+ "",Toast.LENGTH_SHORT).show();
                deleteMemo((int)mId);
                return true;
            case R.id.cancel:

                return true;
            //编辑现有的一条数据
            case R.id.content_edit:
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                // 将 id append 到 uri (向Provider查询该宠物信息的URI)
                Uri currentMemoUri = ContentUris.withAppendedId(MemoEntry.CONTENT_URI, mId);
                // 让intent携带 至 DditorActivity
                intent.setData(currentMemoUri);
                // 启动编辑界面
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate  res/menu file.
        // 添加应用程序菜单栏 设置
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 用户点击菜单栏选项
        switch (item.getItemId()) {
            // 点击设置
            case R.id.action_setting:
                //跳转到设置页面
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                startActivity(settingsIntent);
                return true;
            // 点击了插入一条数据
            case R.id.action_insert_memo:
                insertMemo();

                return true;
            //点击了删除所有数据
            case R.id.action_delete_all_memo:
                deleteAllMemo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertMemo() {
        // 创建一个ContentValues 存放键对值
        ContentValues values = new ContentValues();
        values.put(MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT, "这是一条测试数据shujuming");
        values.put(MemoEntry.COLUMN_MEMO_DEPOSIT_TIME, new Date().getTime() + "");

        // 使用contentresolver用于添加数据 它调用的应是PetProvider中德insert方法（根据uri）
        //返回的是一个uri
        Uri newUri = getContentResolver().insert(MemoEntry.CONTENT_URI, values);
    }

    /**
     * 删除所有数据
     */
    private void deleteAllMemo() {
        int rowsDeleted = getContentResolver().delete(MemoEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " 行从数据库被删除");
    }
    /**
     * 删除单条数据
     */
    private void deleteMemo(int position) {
        Uri currentMemoUri = ContentUris.withAppendedId(MemoEntry.CONTENT_URI, position);
        //返回的是被删除的行数
        int rowDeleted = getContentResolver().delete(currentMemoUri, null, null);
        if(rowDeleted == 0) {
            Toast.makeText(MainActivity.this,"没有数据被删除",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,"成功删除一条数据",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // 查询的字段
        String[] projection = {
                MemoEntry._ID,
                MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT,
                MemoEntry.COLUMN_MEMO_DEPOSIT_TIME };
        String orderby = MemoEntry.COLUMN_MEMO_DEPOSIT_TIME + " DESC";
        // 在后台线程执行Provider的query方法
        //后台线程只CursorLoader 它继承自AsyncTaskLoader（实现了Runnable）
        //这里实现的三个抽象方法 都是回调函数执行在主线程
        // （Activiity调用CursorLoader中的方法，CursorLoader再调用Activity中的方法）
        //如 数据发生更新时 调用 onLoadFinished 在ui线程完成界面数据更新
        return new CursorLoader(this,
                MemoEntry.CONTENT_URI, //执行Provider.query 方法时使用的uri
                projection, //返回的字段
                null,
                null,
                orderby);  // 返回的数据排序
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // 更新 PetCursorAdapter使用新的数据
        mCursorAdapter.swapCursor(data);
        mContent.clear();
        mContentId.clear();
        //没有数据 不执行余下操作
        if(data == null || data.getCount() < 1)
            return;
        int contentColumnIndex = data.getColumnIndex(MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT);
        int contentIdColumnIndex = data.getColumnIndex(MemoEntry._ID);
        /**
         * 在插入和更新之后 cursor指针指向了第一行（也就是0）
         * 而正常情况下 要经过一次 moveToNext（）才指向第一行
         * 所以先moveToFirst 再执行do while操作
         */
        data.moveToFirst();
        do {
            mContent.add(data.getString(contentColumnIndex));
            mContentId.add(Integer.parseInt(data.getString(contentIdColumnIndex)));
        } while (data.moveToNext());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // 当删除数据时调用
        //设为空 再设置新的数据
        mCursorAdapter.swapCursor(null);
        mContent.clear();
        mContentId.clear();
    }
}
