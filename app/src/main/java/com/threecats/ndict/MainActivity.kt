package com.threecats.ndict

import android.app.Application
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.threecats.ndict.Models.DataModel
import com.threecats.ndict.Models.Person
import com.threecats.ndict.ndict.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dm: DataModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var persons = dm.personQuery.find()
        var man: Person = Person()
        when (item.itemId) {
            R.id.navigation_home -> {
                man = persons[0]
                //message.setText(R.string.title_home)
                //return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                man = persons[1]
            }
            R.id.navigation_notifications -> {
                man = persons[2]
            }
        }
        message.text = "姓名：${man.name}，年龄：${man.age.text}，身高：${man.height}，体重：${man.weight}"
        BMR.text = "基础卡路里：${man.BMR.mild}"
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dm = DataModel(application as App)
        dm.initPerson()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
