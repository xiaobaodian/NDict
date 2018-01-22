package com.threecats.ndict.View

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.threecats.ndict.App
import com.threecats.ndict.Models.DataModel
import com.threecats.ndict.R
import com.threecats.ndict.ViewModels.PersonPlus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dm: DataModel

    private val fragmentManager = supportFragmentManager
    private var dietRecordsFragment: DietRecordsFragment? = null
    private var foodsFragment: FoodsFragment? = null
    private var personFragment: PersonFragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var persons = dm.personQuery.find()
        lateinit var man: PersonPlus
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
        setContentView(R.layout.activity_main)

        dm = DataModel(application as App)
        dm.initPerson()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun loadFragment(fragment: Fragment?){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrament, fragment)
        fragmentTransaction.commit()
    }

}
