package com.threecats.ndictdataset.View

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.threecats.ndictdataset.FoodFragment.FoodPropertyFragment

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
}