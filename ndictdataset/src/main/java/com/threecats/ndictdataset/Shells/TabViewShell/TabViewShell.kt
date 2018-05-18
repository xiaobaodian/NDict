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
class TabViewShell {

    var currentFragment: Fragment? = null
    var currentTabPosition = 0
    var viewPageOffscreenPageLimit: Int = 1          // 如果预加载数设置为-1（所有负数）就设置为加载fragments.size

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
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

    fun parent(activity: AppCompatActivity): TabViewShell {
        fragmentManager = activity.supportFragmentManager
        return this
    }

    fun parent(fragment: Fragment): TabViewShell {
        fragmentManager = fragment.childFragmentManager
        return this
    }

    fun viewPage(viewPager: ViewPager): TabViewShell {
        this.viewPager = viewPager
        return this
    }

    fun tab(tab: TabLayout): TabViewShell {
        this.tabLayout = tab
        tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentFragment = fragments[it.position]
                    val fragment = fragments[it.position]
                    tabReselectedListener?.onTabReselected(tab, fragment)
                }
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val fragment = fragments[it.position]
                    currentTabPosition = it.position
                    tabSelectedListener?.onTabSelected(tab, fragment)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    val fragment = fragments[it.position]
                    tabUnselectedListener?.onTabUnselected(tab, fragment)
                }
            }
        })
        return this
    }

    fun addFragment(fragment: Fragment, name: String): TabViewShell {
        val bundle = Bundle()
        bundle.putString("name", name)
        fragment.arguments = bundle
        fragments.add(fragment)
        return this
    }

    fun link(){
        val count = fragments.size
        viewPager?.offscreenPageLimit = if (viewPageOffscreenPageLimit < 0) count else viewPageOffscreenPageLimit
        if (count == 0) return
        if (count == 1) {
            tabLayout?.visibility = View.GONE
        } else {
            tabLayout?.visibility = View.VISIBLE
        }
        if (tabLayout!= null && viewPager != null) {
            viewPager!!.adapter = ViewPagerFragmentAdapter(fragmentManager, fragments)
            tabLayout!!.setupWithViewPager(viewPager)
        }
    }

    fun next(){
        val length = fragments.size
        if (length == 0) return
        val position = if (currentTabPosition == length - 1) 0 else currentTabPosition+1
        tabLayout?.let {
            it.getTabAt(position)?.select()
        }
    }

    fun selectTab(index: Int){
        val length = fragments.size
        if (length == 0 || index >= length) return
        tabLayout?.let {
            it.getTabAt(index)?.select()
        }
    }
}