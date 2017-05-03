package cn.ambermoe.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    //使用PreferenceFragment settings_main中的值会被自动存为 SharedPreferences

    public static class MemoPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_main);
            /**
             * 使用findPreference() 方法来获取偏好 对象，
             *使用 bindPreferenceSummaryToValue() 帮助程序方法来设置偏好
             */

            //location
            Preference location = findPreference("location");
            bindPreferenceSummaryToValue(location);

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
            //因为打开setting页面时并不会触发onPreferenceChange
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
