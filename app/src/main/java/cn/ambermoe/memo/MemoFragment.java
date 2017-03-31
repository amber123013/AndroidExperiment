package cn.ambermoe.memo;

import android.os.Bundle;
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

        return view;
    }
}
