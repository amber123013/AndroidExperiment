package cn.ambermoe.memo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    //此方法覆盖了 手机的返回按钮（点击实体返回键调用的函数
    //让手机的返回键 执行 导航栏的返回键（设置完成刷新listview
    @Override
    public void onBackPressed() {
        onOptionsItemSelected((MenuItem) findViewById(android.R.id.home));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //导航栏的返回键
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    //使用PreferenceFragment settings_main中的值会被自动存为 SharedPreferences
    //只执行onCreate中的前两行代码便可完成功能
    //余下代码是为实现 选中项 未点击前的预览
    public static class MemoPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            /**
             * 使用findPreference() 方法来获取偏好 对象，
             *使用 bindPreferenceSummaryToValue() 帮助程序方法来设置偏好
             */
            //color首选项
            Preference color = findPreference("fontcolor");
            bindPreferenceSummaryToValue(color);
            //textsize首选项
            Preference size = findPreference("fontsize");
            bindPreferenceSummaryToValue(size);


        }

        /**
         *在偏好更改后更新已显示的偏好摘要
         * (在每次选择后在选项的下方显示 该选择)
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference) { //list 和 edit 分开处理
                ListPreference listPreference = (ListPreference) preference;
                //点击的值所在项数
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    //在下方显示摘要
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /**
         *设置当前 MemoPreferenceFragment 实例作为 每个偏好上的侦听程序。  1
         * 读取 设备上 SharedPreferences 中存储的偏好当前值，
         * 在 偏好摘要中进行显示
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            //添加PreferenceChange监听 每次选择的内容改变
            //执行onPreferenceChange重新在下方显示选择的内容
            preference.setOnPreferenceChangeListener(this);

            //第一个 加载出设置页面时 手动绑定显示
            //因为打开setting页面是并不会触发onPreferenceChange
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
