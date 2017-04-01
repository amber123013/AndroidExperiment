package cn.ambermoe.memo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ASUS on 2017-03-31.
 */

public class MemoFragment extends Fragment {
    public String str;

    public MemoFragment(String str) {
        super();
        this.str = str;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor_item_view, container, false);
        TextView textView = (TextView) view.findViewById(R.id.edit);
        textView.setText(str);

        //获取 SharedPreferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());;
        //获取color size
        String color = sharedPrefs.getString("fontcolor","#333");
        String fontsize = sharedPrefs.getString("fontsize","16");
        //设置color size
        textView.setTextSize(Float.parseFloat(fontsize));
        textView.setTextColor(Color.parseColor(color));
        return view;
    }
}
