package cn.ambermoe.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import cn.ambermoe.weather.utils.GetJsonUtils;
import cn.ambermoe.weather.utils.NetworkUtils;
import cn.ambermoe.weather.utils.Weather;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Weather[]>,
        SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    //循环视图
    private RecyclerView mRecyclerView;
    //RecyclerView 的适配器
    private WeatherAdapter mWeatherAdapter;
    //从网络加载数据出错是显示的 错误文本
    private TextView mErrorMessageDisplay;
    //加载提示符
    private ProgressBar mLoadingIndicator;
    //locader id
    private static final int LOADER_ID = 0;
    //此变量标志是否有设置发生变化
    private static boolean HAVE_UPDATED = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //去除toolbar的阴影
        getSupportActionBar().setElevation(0);
        //获取控件
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message);
        //设置recyclerView 为竖向显示
        int recyclerViewOrientation = LinearLayoutManager.VERTICAL;
        boolean shouldReverseLayout = false;
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, recyclerViewOrientation, shouldReverseLayout);
        mRecyclerView.setLayoutManager(layoutManager);
        //设置RecyclerView保持固定大小
        mRecyclerView.setHasFixedSize(true);
        //设置RecyclerView的适配器
        mWeatherAdapter = new WeatherAdapter();
        mRecyclerView.setAdapter(mWeatherAdapter);
        //获得提示器的引用
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading);

        LoaderManager.LoaderCallbacks<Weather[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        /**
         * 指定ID的装载器已经存在,则它使用这个装载器.
         * 如果不存在呢,它将创建一个新的
         * 它将保证activity被重建时使用原先的后台线程，而不是在创建一个造成 僵尸线程的存在
         */
        getSupportLoaderManager().initLoader(LOADER_ID, bundleForLoader, callback);

        /**
         * 注册监听（设置
         */
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //每次start时判断是否有设置发生了变化
        //如果有则重启加载器
        if (HAVE_UPDATED) {
            Log.d(TAG, "onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            HAVE_UPDATED = false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* 销毁时注销对设置的监听 */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public Loader<Weather[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Weather[]>(this) {
            /**
             * 此数组用于缓存天气数据
             * 当数组为空时会执行 forceLoad（）之后执行loadInBackground 从网络请求数据
             * 最终在deliverResult（）函数中被赋值
             * 当应用重启activity时（如旋转屏幕）mWeatherData已经缓存了数据 将不会重复请求网络缓存数据
             */
            Weather[] mWeatherData = null;
            @Override
            protected void onStartLoading() {
                if (mWeatherData != null) {
                    //直接使用缓存的数据
                    deliverResult(mWeatherData);
                } else {
                    //显示 加载提示器 执行forceLoad执行 loader会调用loadInBackground
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }
            @Override
            public void deliverResult(Weather[] data) {

                mWeatherData = data;
                super.deliverResult(data);
            }

            @Override
            public Weather[] loadInBackground() {

                try {
                    //请求数据的url
                    URL weatherUrl = NetworkUtils.getUrl(MainActivity.this);
                    String strJSON = NetworkUtils
                            .getJSONFromHttpUrl(weatherUrl);
                    //解析json数据
                    Weather[] weathers = GetJsonUtils
                            .getWeatherDataFromJson(MainActivity.this, strJSON);

                    return weathers;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Weather[]> loader, Weather[] data) {
        //隐藏加载提示器
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mWeatherAdapter.setWeatherData(data);
        if (null == data) {
            //没有从网络中获得数据
            //隐藏RecyclerView
            mRecyclerView.setVisibility(View.INVISIBLE);
            //显示错误信息
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        } else {
            //获得了数据
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Weather[]> loader) {
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate  res/menu file.
        // 添加应用程序菜单栏 设置
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 点击设置
            case R.id.action_setting:
                //跳转到设置页面
                Intent settingsIntent = new Intent(this, SettingActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //有设置发生变化时触发
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        HAVE_UPDATED = true;
    }
}
