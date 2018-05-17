package com.threecats.ndict.View

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class CategoryFoodFragmentAdapter : FragmentPagerAdapter {

    private var fragmentList: List<CategoryFoodsFragment>

    //constructor(fm: FragmentManager, context: Context) : super(fm) {}

    constructor(fm: FragmentManager, fragmentList: List<CategoryFoodsFragment>) : super(fm) {
        this.fragmentList = fragmentList
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): CategoryFoodsFragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        val fragment = fragmentList[position]
        return fragment.category.shortTitle!!
    }
}