package com.marknote.android.viewPager.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    // 定义一个Fragment列表
    private List<Fragment> fragmentList = new ArrayList<>();

    // 构造方法，传入FragmentManager
    public FragmentAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);//指定 Fragment 的生命周期行为。
        // 仅当前显示的 Fragment 会处于 RESUMED 状态。其他 Fragment 会被暂停
    }

    // 添加Fragment到列表中
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    // 返回指定位置的Fragment
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    // 返回Fragment列表的大小
    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
