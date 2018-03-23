package com.threecats.ndictdataset.Shells.TabViewShell

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

/**
 * 由 zhang 于 2018/3/22 创建
 */
class TabViewLayoutShell {

    var tab: TabLayout? = null
    var viewPager: ViewPager? = null
    var activity: Activity? = null

    var onTabSelectedListener: onShellTabSelectedListener? = null
    var onTabUnselectedListener: onShellTabUnselectedListener? = null
    var onTabReselectedListener: onShellTabReselectedListener? = null

    private lateinit var fragmentManager: FragmentManager
    private val fragments = mutableListOf<viewPagerFragment>()

    init {
        //fragmentManager = supportFragmentManager()
        //fragmentManager = getSupportFragmentManager()
    }

    fun parent(activity: Activity): TabViewLayoutShell {
        fragmentManager = activity.fragmentManager
        return this
    }

    fun parent(fragment: Fragment): TabViewLayoutShell {
        fragmentManager = fragment.childFragmentManager
        return this
    }

    fun viewPage(viewPager: ViewPager): TabViewLayoutShell {
        this.viewPager = viewPager
        return this
    }

    fun tab(tab: TabLayout): TabViewLayoutShell {
        this.tab = tab
        return this
    }

    fun addFragment(fragment: viewPagerFragment){
        fragments.add(fragment)
    }
}