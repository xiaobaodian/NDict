package com.threecats.ndictdataset.Shells.TabViewShell

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment

/**
 * 由 zhang 于 2018/3/22 创建  onTabSelected(tab: TabLayout.Tab?)
 */
interface onShellTabSelectedListener {
    fun onTabSelected(tab: TabLayout.Tab, fragment: Fragment)
}
interface onShellTabUnselectedListener {
    fun onTabUnselected(tab: TabLayout.Tab, fragment: Fragment)
}
interface onShellTabReselectedListener {
    fun onTabReselected(tab: TabLayout.Tab, fragment: Fragment)
}