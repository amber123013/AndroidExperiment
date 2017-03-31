package cn.ambermoe.memo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by ASUS on 2017-03-31.
 */

//viewPager 适配器
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> arrayList;

    public SimpleFragmentPagerAdapter(FragmentManager fm, ArrayList<String> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    public int count;
    Fragment currentFragment;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (MemoFragment) object;
        super.setPrimaryItem(container, position, object);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
            fragment = new MemoFragment(arrayList.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}