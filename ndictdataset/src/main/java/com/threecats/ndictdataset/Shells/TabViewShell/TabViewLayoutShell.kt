package com.threecats.ndictdataset.Shells.TabViewShell

//import android.app.Fragment
//import android.app.FragmentManager
import android.os.Bundle
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
    var currentFragment: Fragment? = null

    private var tabSelectedListener: onShellTabSelectedListener? = null
    private var tabUnselectedListener: onShellTabUnselectedListener? = null
    private var tabReselectedListener: onShellTabReselectedListener? = null

    private lateinit var fragmentManager: FragmentManager
    val fragments = mutableListOf<Fragment>()

    fun setOnTabSelectedListener(selectedListener: onShellTabSelectedListener){
        tabSelectedListener = selectedListener
    }
    fun setOnTabUnselectedListener(unselectedListener: onShellTabUnselectedListener){
        tabUnselectedListener = unselectedListener
    }
    fun setOnTabReselectedListener(reselectedListener: onShellTabReselectedListener){
        tabReselectedListener = reselectedListener
    }

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
                    tabReselectedListener?.let { it.onTabReselected(tab) }
                }
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentFragment = fragments[it.position]
                    tabSelectedListener?.let { it.onTabSelected(tab) }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    tabUnselectedListener?.let { it.onTabUnselected(tab) }
                }
            }
        })
        return this
    }

    fun addFragment(fragment: Fragment, name: String): TabViewLayoutShell {
        val bundle = Bundle()
        bundle.putString("name", name)
        fragment.setArguments(bundle)
        fragments.add(fragment)
        return this
    }

    fun link(){
        val count = fragments.size
        viewPager?.offscreenPageLimit = count
        if (count == 0) return
        if (count == 1) {
            tab?.visibility = View.GONE
        } else {
            tab?.visibility = View.VISIBLE
        }
        if (tab!= null && viewPager != null) {
            viewPager!!.adapter = ViewPagerFragmentAdapter(fragmentManager, fragments)
            tab!!.setupWithViewPager(viewPager)
        }
    }
}