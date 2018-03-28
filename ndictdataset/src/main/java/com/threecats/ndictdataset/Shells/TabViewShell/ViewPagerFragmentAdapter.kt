package com.threecats.ndictdataset.Shells.TabViewShell


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
//import android.support.v4.app.FragmentStatePagerAdapter

/**
 * 由 zhang 于 2018/2/22 创建
 */
class ViewPagerFragmentAdapter: FragmentPagerAdapter {

    //private var fragments: List<ViewPagerFragment>
    private var fragments: List<out Fragment>

    constructor(fragmentManager: FragmentManager, fragments: List<out Fragment>) : super(fragmentManager) {
        this.fragments = fragments
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        val fragment = fragments[position]
        var title = "没有Fragment标题数据"
        fragment.arguments?.let {
            title = it.getString("name")
        }
        return title
    }
}