package com.threecats.ndictdataset.View

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cn.bmob.v3.Bmob
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Models.PublicSet
import kotlinx.android.synthetic.main.activity_main.*
import com.threecats.ndictdataset.R


class BDMMainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private var traceElementFragment: TraceElementFragment? = null
    private var categoryFoodsFragment: CategoryFoodsFragment? = null
    private var foodEnergyFragment: FoodEnergyFragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                MainToolbar.title = "食材数据"
                if (categoryFoodsFragment == null) categoryFoodsFragment = CategoryFoodsFragment()
                loadFragment(categoryFoodsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                MainToolbar.title = "食物能量"
                if (traceElementFragment == null) traceElementFragment = TraceElementFragment()
                loadFragment(traceElementFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                MainToolbar.title = "微量元素"
                if (foodEnergyFragment == null) foodEnergyFragment = FoodEnergyFragment()
                loadFragment(foodEnergyFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initShareSet()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (categoryFoodsFragment == null) {
            MainToolbar.title = "食材数据"
            categoryFoodsFragment = CategoryFoodsFragment()
        }
        loadFragment(categoryFoodsFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        BDM.ShareSet = null
    }

    private fun loadFragment(fragment: Fragment?){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrament, fragment)
        fragmentTransaction.commit()
    }

    private fun initShareSet(){
        if (BDM.ShareSet == null) {
            BDM.ShareSet = PublicSet()
            //Toast.makeText(applicationContext,"生成 ShareSet 对象！",Toast.LENGTH_SHORT).show()
        }
    }
}
