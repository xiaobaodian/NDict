package com.threecats.ndictdataset.Shells.TabViewShell


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
//import android.support.v4.app.FragmentStatePagerAdapter
import com.threecats.ndictdataset.BuildConfig
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * 由 zhang 于 2018/2/22 创建
 */
class ViewPagerFragmentAdapter: FragmentPagerAdapter {

    private var fragmentList: List<viewPagerFragment>

    constructor(fragmentManager: FragmentManager, fragmentList: List<viewPagerFragment>) : super(fragmentManager) {
        this.fragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        var title: String = fragmentList[position].title
//        var title: String = fragmentList[position].arguments.getString("name", "没有数据")
//        if (BuildConfig.DEBUG) {
//            val logshow = AnkoLogger("NDIC")
//            logshow.info { "适配器读取名称" }
//        }
        return title
    }
}