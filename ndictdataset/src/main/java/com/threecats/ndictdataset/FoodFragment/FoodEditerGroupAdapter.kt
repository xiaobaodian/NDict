package com.threecats.ndictdataset.FoodFragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * 由 zhang 于 2018/2/22 创建
 */
class FoodEditerGroupAdapter: FragmentPagerAdapter {

    private var fragmentList: List<FoodPropertyFragment>

    constructor(fm: FragmentManager, fragmentList: List<FoodPropertyFragment>) : super(fm) {
        this.fragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        //var title: String = fragmentList[position].arguments.getString("name", "没有数据")
        var f = fragmentList[position]
        var s = f.tabTitle.toString()
        var title = fragmentList[position].tabTitle.toString()
        return title
    }
}