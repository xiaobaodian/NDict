package com.threecats.ndict.View

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.threecats.ndict.App
import com.threecats.ndict.Models.DataSet
import com.threecats.ndict.R
import kotlinx.android.synthetic.main.activity_main.*
import android.view.WindowManager
import android.os.Build



class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private var dietRecordsFragment: DietRecordsFragment? = null
    private var foodsFragment: FoodsFragment? = null
    private var personFragment: PersonFragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                if (dietRecordsFragment == null) dietRecordsFragment = DietRecordsFragment()
                loadFragment(dietRecordsFragment)
            }
            R.id.navigation_dashboard -> {
                if (foodsFragment == null) foodsFragment = FoodsFragment()
                loadFragment(foodsFragment)
            }
            R.id.navigation_notifications -> {
                if (personFragment == null) personFragment = PersonFragment()
                loadFragment(personFragment)
            }
        }

        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = window
            // Translucent status bar  FLAG_TRANSLUCENT_NAVIGATION  FLAG_TRANSLUCENT_STATUS
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        setContentView(R.layout.activity_main)

        DataSet.init(application as App)
        DataSet.initPerson()
        DataSet.initObjectBox()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (dietRecordsFragment == null) dietRecordsFragment = DietRecordsFragment()
        loadFragment(dietRecordsFragment)
    }

    private fun loadFragment(fragment: Fragment?){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrament, fragment)
        fragmentTransaction.commit()
    }

}
