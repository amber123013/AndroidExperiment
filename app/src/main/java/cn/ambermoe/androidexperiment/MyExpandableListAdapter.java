package cn.ambermoe.androidexperiment;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ASUS on 2017-03-24.
 */

public class MyExpandableListAdapter implements ExpandableListAdapter{

    public Activity activity;
    public ExpandableListView list;
    public MyExpandableListAdapter(Activity activity, ExpandableListView list) {
        this.activity = activity;
        this.list = list;
    }
    private String[] armTypes = new String[]{
            "信息工程学院", "海峡动漫学院", "资源化工学院"
    };
    private String[][] arms = new String[][]{
            {"软件", "计嵌", "网工"},
            {"动画", "媒体创意"},
            {"化学工程与工艺", "生物技术", "资源环境科学"},
    };

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return armTypes.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arms[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return armTypes[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arms[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //父项显示视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout ll = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = getTextView();
        textView.setText(getGroup(groupPosition).toString());
        textView.setPadding(100, 0, 0, 0);
        ll.addView(textView);
        return ll;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getTextView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"选择了 "+getGroup(groupPosition).toString()+ " " + getChild(groupPosition,childPosition) ,Toast.LENGTH_SHORT).show();
                //关闭当前分组
                list.collapseGroup(groupPosition);
            }
        });
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        for(int i = 0; i < getGroupCount(); i++) {
            if (groupPosition != i) {// 关闭其他分组
                list.collapseGroup(i);
            }
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
    //子项显示的视图
    private TextView getTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);
        TextView textView = new TextView(activity);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(110, 0, 0, 0);
        //textView.setTextSize(20);
        return textView;
    }
}
