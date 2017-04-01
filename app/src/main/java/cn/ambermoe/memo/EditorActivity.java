package cn.ambermoe.memo;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import cn.ambermoe.memo.data.MemoContract;

/**编辑页 执行插入和更新操作*/
public class EditorActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    /** uri指向一个现有的记录，如果为空则为添加一个新的宠物 */
    private Uri mCurrentMemoUri;
    public SharedPreferences sharedPrefs;
    /**编辑框*/
    private EditText mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mContent = (EditText) findViewById(R.id.content_edit);
        //获取 SharedPreferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);;
        //获取color size
        String color = sharedPrefs.getString("fontcolor","#333");
        String fontsize = sharedPrefs.getString("fontsize","16");
        //设置color size
        mContent.setTextSize(Float.parseFloat(fontsize));
        mContent.setTextColor(Color.parseColor(color));
        /**
         *根据是否有传递过来URI
         * 来判断是添加还是编辑操作
         */
        Intent intent = getIntent();
        mCurrentMemoUri = intent.getData();

        // 如果为空 添加一个
        if (mCurrentMemoUri == null) {
            // 设置标题
            setTitle("添加");

            // 隐藏删除菜单选项
            /**
             * 执行invalidateOptionsMenu()方法
             * 系统将调用onPrepareOptionsMenu()
             */
            invalidateOptionsMenu();
        } else {
            // 设置标题
            setTitle("编辑");

            // 初始化加载程序读取数据库中的数据
            //并在编辑器中显示当前值
            //此时onLoadFinished 获得Cursor中只包含单个宠物信息
            getLoaderManager().initLoader(0, null, this);
        }
    }
    //在onCreateOptionsMenu之前调用
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //如果是新建则隐藏删除选项
        if (mCurrentMemoUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_memo);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate  res/menu file.
        // 添加应用程序菜单栏 设置
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        EditText editText  = ((EditText)findViewById(R.id.content_edit));
//        editText.setFocusableInTouchMode(true);
//        boolean flag = editText.requestFocus();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 用户点击菜单栏选项
        switch (item.getItemId()) {
            // 完成图标
            case R.id.action_complete:
                savePet();
                finish();
                return true;
            // 点击删除图标
            case R.id.action_delete_memo:
                //弹框让用户选择是否确认删除操作
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**此函数根据mCurrentMemoUri的值判断是
     *新建一条记录 还是更新一条记录
     */
    private void savePet() {

        // 创建一个ContentValues 存放键对值
        ContentValues values = new ContentValues();
        values.put(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT, mContent.getText().toString());
        //更新的同样使用当前的时间
        values.put(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_TIME, new Date().getTime() + "");

        // 使用contentresolver用于添加数据 它调用的应是PetProvider中的insert方法（根据uri）

        if (mCurrentMemoUri == null) { //新添一条记录
            //向数据库中插入一行 (放回一个uri
            Uri newUri = getContentResolver().insert(MemoContract.MemoEntry.CONTENT_URI, values);
            if (newUri == null) {
                // 如果uri为空 说明插入失败
                Toast.makeText(this, "数据插入失败",
                        Toast.LENGTH_SHORT).show();
            } else {
                // 否则插入成功
                Toast.makeText(this, "数据插入成",
                        Toast.LENGTH_SHORT).show();
            }
        } else { //更新一个宠物
            int rowsAffected = getContentResolver().update(mCurrentMemoUri, values, null, null);

            // 根据返回的 int 值 显示提示消息
            if (rowsAffected == 0) {
                // 显示失败消息
                Toast.makeText(this, "更新失败",
                        Toast.LENGTH_SHORT).show();
            } else {
                // 显示成功消息
                Toast.makeText(this, "更新成功",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    //从数据库中删除数据
    private void deleteMemo() {
        if (mCurrentMemoUri != null) {
            // 调用ContentResolver使用URI删除宠物
            int rowsDeleted = getContentResolver().delete(mCurrentMemoUri, null, null);

            if (rowsDeleted == 0) {
                // 如果没有数据被删除
                Toast.makeText(this, "数据未能被删除",
                        Toast.LENGTH_SHORT).show();
            } else {
                // 提示成功
                Toast.makeText(this, "删除成功",
                        Toast.LENGTH_SHORT).show();
            }
            finish();

        }
    }

    /**
     * 显示一个对话框，选择是否删除
     */
    private void showDeleteConfirmationDialog() {
        // 创建一个alertDialog 让用户选择是否确认删除
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否删除此记录？");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 用户选择删除
                deleteMemo();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // 用户选择取消，关闭对话框
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // 创建并显示alertdialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // 查询的字段
        String[] projection = {
                MemoContract.MemoEntry._ID,
                MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT,
                MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_TIME };
        // 在后台线程执行Provider的query方法
        //后台线程只CursorLoader 它继承自AsyncTaskLoader（实现了Runnable）
        //这里实现的三个抽象方法 都是回调函数执行在主线程
        // （Activiity调用CursorLoader中的方法，CursorLoader再调用Activity中的方法）
        //如 数据发生更新时 调用 onLoadFinished 在ui线程完成界面数据更新
        return new CursorLoader(this,
                mCurrentMemoUri, //执行Provider.query 方法时使用的uri这里只返回一个
                projection, //返回的字段
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // 没有数据的话
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // 获得第一行数据（只有一行
        if (cursor.moveToFirst()) {
            // 获取字段对应的column
            int contentColumnIndex = cursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT);
            // 根据column索引获取内容
            String content = cursor.getString(contentColumnIndex);

            // 设置数据
            mContent.setText(content);
        }
    }
    //loader 被重置从而使其数据无效时调用
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContent.setText("");
    }
}
