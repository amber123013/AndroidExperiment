package cn.ambermoe.memo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by ASUS on 2017-03-31.
 */

//viewPager 适配器
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> arrayList;
    private ArrayList<Integer> arrayListId;

    public SimpleFragmentPagerAdapter(FragmentManager fm, ArrayList<String> arrayList,ArrayList<Integer> arrayListId) {
        super(fm);
        this.arrayList = arrayList;
        this.arrayListId = arrayListId;
    }

    public int count;

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
            fragment = new MemoFragment(arrayList.get(position),arrayListId.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}