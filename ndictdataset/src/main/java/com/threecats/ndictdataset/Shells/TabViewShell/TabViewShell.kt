package com.threecats.ndictdataset.Shells.TabViewShell

import android.app.Activity
//import android.app.Fragment
//import android.app.FragmentManager
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * 由 zhang 于 2018/3/22 创建
 */
class TabViewLayoutShell {

    var tab: TabLayout? = null
    var viewPager: ViewPager? = null

    var onTabSelectedListener: onShellTabSelectedListener? = null
    var onTabUnselectedListener: onShellTabUnselectedListener? = null
    var onTabReselectedListener: onShellTabReselectedListener? = null

    private lateinit var fragmentManager: FragmentManager
    private val fragments = mutableListOf<viewPagerFragment>()

    fun parent(activity: AppCompatActivity): TabViewLayoutShell {
        fragmentManager = activity.supportFragmentManager
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
        tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    onTabReselectedListener?.let { it.onTabReselected(tab) }
                }
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    onTabSelectedListener?.let { it.onTabSelected(tab) }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    onTabUnselectedListener?.let { it.onTabUnselected(tab) }
                }
            }
        })
        return this
    }

    fun addFragment(fragment: viewPagerFragment): TabViewLayoutShell {
        fragments.add(fragment)
        return this
    }

    fun addFragments(fragments: List<viewPagerFragment>): TabViewLayoutShell {
        this.fragments.addAll(fragments)
        return this
    }

    fun link(){
        when (fragments.size){
            0 -> return
            1 -> tab?.visibility = View.GONE
            else -> {
                tab?.visibility = View.VISIBLE
                if (tab!= null && viewPager != null) {
                    viewPager!!.adapter = ViewPagerFragmentAdapter(fragmentManager, fragments)
                    tab!!.setupWithViewPager(viewPager)
                }
            }
        }
    }
}