package com.threecats.ndict

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.threecats.ndict.Helper.PersonBMI
import com.threecats.ndict.Models.DataModel
import com.threecats.ndict.Models.PersonPlus
import com.threecats.ndict.ndict.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dm: DataModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var persons = dm.personQuery.find()
        lateinit var man: PersonPlus
        when (item.itemId) {
            R.id.navigation_home -> {
                man = PersonPlus(persons[0])
                //message.setText(R.string.title_home)
                //return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                man = PersonPlus(persons[1])
            }
            R.id.navigation_notifications -> {
                man = PersonPlus(persons[2])
            }
        }
        message.text = "姓名：${man.name}，年龄：${man.age.text}，身高：${man.height}，体重：${man.weight}"
        BMR.text = "基础：${man.BMR.base}，日常${man.BMR.mild}，中体力：${man.BMR.medium}，重体力：${man.BMR.sevete}"
        Power.text = "蛋白质：${man.dailyDemand.protein}，脂肪：${man.dailyDemand.fat}，碳水：${man.dailyDemand.carbohydrate}。BMI：${man.BMI.type(PersonBMI.EArea.WHO)}"
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
