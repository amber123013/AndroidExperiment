package cn.ambermoe.memo;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ambermoe.memo.data.MemoContract;

/**
 * Created by ASUS on 2017-03-31.
 */

public class MemoFragment extends Fragment {
    public String str;
    private int id;

    public MemoFragment(String str,int id) {
        super();
        this.str = str;
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editor_item_view, container, false);
        TextView textView = (TextView) view.findViewById(R.id.edit);
        textView.setText(str);
        //点击textview进入编辑页
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                // 将 id append 到 uri (向Provider查询该宠物信息的URI)
                Uri currentMemoUri = ContentUris.withAppendedId(MemoContract.MemoEntry.CONTENT_URI, id);
                // 让intent携带 至 DditorActivity
                intent.setData(currentMemoUri);
                // 启动编辑界面
                startActivity(intent);
            }
        });

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
