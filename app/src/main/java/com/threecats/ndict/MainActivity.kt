package com.threecats.ndict

import android.app.Application
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.threecats.ndict.Models.DataModel
import com.threecats.ndict.Models.Person
import com.threecats.ndict.Models.PersonTarget
import com.threecats.ndict.ndict.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dm: DataModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var persons = dm.personQuery.find()
        lateinit var man: PersonTarget
        when (item.itemId) {
            R.id.navigation_home -> {
                man = PersonTarget(persons[0])
                //message.setText(R.string.title_home)
                //return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                man = PersonTarget(persons[1])
            }
            R.id.navigation_notifications -> {
                man = PersonTarget(persons[2])
            }
        }
        message.text = "姓名：${man.name}，年龄：${man.age.text}，身高：${man.height}，体重：${man.weight}"
        BMR.text = "卡路里：${man.BMR.mild}，蛋白质：${man.dailyDemand.protein}，脂肪：${man.dailyDemand.fat}，碳水：${man.dailyDemand.carbohydrate}"
        HR.text = "最大心率：${man.EHR.max}，最佳心率范围：${man.EHR.auto(0.6f)} ~ ${man.EHR.auto(0.85f)}"
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
