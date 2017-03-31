package cn.ambermoe.memo;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Date;

import cn.ambermoe.memo.data.MemoContract;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate  res/menu file.
        // 添加应用程序菜单栏 设置
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText editText  = ((EditText)findViewById(R.id.content_edit));
        editText.setFocusableInTouchMode(true);
        boolean flag = editText.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 用户点击菜单栏选项
        switch (item.getItemId()) {
            // 完成图标
            case R.id.action_complete:
                insertMemo();
                finish();
                return true;
            // 点击删除图标
            case R.id.action_delete_memo:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //插入一条数据
    private void insertMemo() {
        EditText editText = (EditText) findViewById(R.id.content_edit);
        // 创建一个ContentValues 存放键对值
        ContentValues values = new ContentValues();
        values.put(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_CONTENT, editText.getText().toString());
        values.put(MemoContract.MemoEntry.COLUMN_MEMO_DEPOSIT_TIME, new Date().getTime() + "");

        // 使用contentresolver用于添加数据 它调用的应是PetProvider中德insert方法（根据uri）
        //返回的是一个uri
        Uri newUri = getContentResolver().insert(MemoContract.MemoEntry.CONTENT_URI, values);
    }
}
