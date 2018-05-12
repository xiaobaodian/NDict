package com.threecats.ndictdataset.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.threecats.ndictdataset.BDM
import com.threecats.ndictdataset.Bmob.BNutrient
import com.threecats.ndictdataset.Bmob.BTraceElement
import com.threecats.ndictdataset.Models.ProposedDosage
import com.threecats.ndictdataset.Enum.ENutrientType
import com.threecats.ndictdataset.EventClass.UpdateNutrient
import com.threecats.ndictdataset.Models.NumberRange
import com.threecats.ndictdataset.NutrientFragments.NutrientDosisFragment
import com.threecats.ndictdataset.NutrientFragments.NutrientContextFragment
import com.threecats.ndictdataset.NutrientFragments.NutrientSublistFragment
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.EditorShell.UpdateItemListener
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell
import kotlinx.android.synthetic.main.activity_nutrient_editer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class NutrientEditerActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!

    private val viewPagerShell = TabViewLayoutShell()
    private lateinit var workFragment: Fragment
    private lateinit var workTitle: String
    private var nutrientType: ENutrientType = ENutrientType.Vitamin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrient_editer)  //NutrientEditerToolbar
        setSupportActionBar(NutrientEditerToolbar)

        NutrientEditerToolbar.setNavigationOnClickListener { onBackPressed() }

        if (shareSet.currentTraceElement == null) {
            NutrientEditerToolbar.title = shareSet.currentNutrient?.name
            shareSet.editorNutrient.setOnUpdateItemListener(object : UpdateItemListener<BNutrient>{
                override fun onUpdateItem(item: BNutrient) {
                    item.update()
                }
            })
            when (shareSet.currentNutrient?.nutrientID){
                4 -> {
                    workFragment = NutrientSublistFragment()
                    workTitle = "脂类列表"
                    nutrientType = ENutrientType.Vitamin
                }
                5 -> {
                    workFragment = NutrientSublistFragment()
                    workTitle = "维生素列表"
                    nutrientType = ENutrientType.Vitamin
                }
                6 -> {
                    workFragment = NutrientSublistFragment()
                    workTitle = "微量元素列表"
                    nutrientType = ENutrientType.Mineral
                }
                else -> {
                    workFragment = NutrientDosisFragment()
                    workTitle = "日常需求量"
                    nutrientType = ENutrientType.Nutrient
                }
            }
        } else {
            NutrientEditerToolbar.title = shareSet.currentTraceElement?.name
            shareSet.editorTraceElement.let {
                it.setOnUpdateItemListener(object : UpdateItemListener<BTraceElement>{
                    override fun onUpdateItem(item: BTraceElement) {
                        item.update()
                    }
                })
                it.edit(shareSet.currentTraceElement!!)
            }
            workFragment = NutrientDosisFragment()
            workTitle = "日常需求量"
            nutrientType = ENutrientType.Nutrient
        }

        viewPagerShell.parent(this)
                .tab(NutrientPropertyTabs)
                .viewPage(NutrientEditerViewPage)
                .addFragment(workFragment, workTitle)
                .addFragment(NutrientContextFragment(), "描述")
                .link()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateNutrient(updateEvent: UpdateNutrient){
        if (shareSet.currentTraceElement == null) {
            shareSet.editorNutrient.commit()
        } else {
            shareSet.editorTraceElement.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nutrientediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        //menu!!.findItem(R.id.addDosisItem).isVisible = shareSet.currentNutrient?.nutrientID !in 5..6
        menu!!.findItem(R.id.addDosisItem).isVisible = workFragment !is NutrientSublistFragment
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.addDosisItem -> {
                when (nutrientType){
                    ENutrientType.Vitamin -> {

                    }
                    ENutrientType.Mineral -> {

                    }
                    ENutrientType.Nutrient -> {
                        //viewPagerShell.selectTab(1)
                        shareSet.editorProposedDosage.append(ProposedDosage())
                        val intent = Intent(this, DosisEditerActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        shareSet.currentTraceElement = null
        EventBus.getDefault().unregister(this)
    }

}
