package com.threecats.ndictdataset.Shells.TabViewShell


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
//import android.support.v4.app.FragmentStatePagerAdapter

/**
 * 由 zhang 于 2018/2/22 创建
 */
class ViewPagerFragmentAdapter: FragmentPagerAdapter {

    private var fragments: List<ViewPagerFragment>

    constructor(fragmentManager: FragmentManager, fragments: List<ViewPagerFragment>) : super(fragmentManager) {
        this.fragments = fragments
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        var title: String = fragments[position].arguments.getString("name", "没有数据")
        return title
    }
}