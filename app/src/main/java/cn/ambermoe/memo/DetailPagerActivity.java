package cn.ambermoe.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
//滑动详情页
public class DetailPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pager);

        Intent intent = getIntent();
        ArrayList arrayList = intent.getStringArrayListExtra("content");
        int currentPosition = intent.getIntExtra("currentPosition",0);
       // Toast.makeText(DetailPagerActivity.this,currentPosition+ "  "+ arrayList.get(0),Toast.LENGTH_SHORT).show();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);


        final SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),arrayList);
        viewPager.setAdapter(adapter);
//        String str = "";
//        for(int i=0;i<arrayList.size();i++) {
//            str += arrayList.get(i);
//        }
//        Toast.makeText(DetailPagerActivity.this,str,Toast.LENGTH_SHORT).show();
        viewPager.setCurrentItem(currentPosition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate  res/menu file.
        // 添加应用程序菜单栏 设置
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 用户点击菜单栏选项
        switch (item.getItemId()) {
            // 新建icon
            case R.id.action_add:
                Intent intent = new Intent(DetailPagerActivity.this, EditorActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
