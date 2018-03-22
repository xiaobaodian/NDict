package com.threecats.ndictdataset.Shells

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager

/**
 * 由 zhang 于 2018/3/22 创建
 */
class TabViewLayoutShell(context: Context) {
    var tab: TabLayout? = null
    var viewPager: ViewPager? = null
    private lateinit var fragmentManager: FragmentManager

    init {
        //fragmentManager = supportFragmentManager()
        //fragmentManager = getSupportFragmentManager()
    }
}