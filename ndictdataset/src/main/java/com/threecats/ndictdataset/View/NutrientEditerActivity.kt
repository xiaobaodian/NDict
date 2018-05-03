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
import com.threecats.ndictdataset.Models.ProposedDosage
import com.threecats.ndictdataset.Enum.ENutrientType
import com.threecats.ndictdataset.Models.NumberRange
import com.threecats.ndictdataset.NutrientFragments.NutrientDosisFragment
import com.threecats.ndictdataset.NutrientFragments.NutrientContextFragment
import com.threecats.ndictdataset.NutrientFragments.NutrientSublistFragment
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.Shells.EditorShell.UpdateItemListener
import com.threecats.ndictdataset.Shells.TabViewShell.TabViewLayoutShell
import kotlinx.android.synthetic.main.activity_nutrient_editer.*
import org.jetbrains.anko.toast

class NutrientEditerActivity : AppCompatActivity() {

    private val shareSet = BDM.ShareSet!!

    private val viewPagerShell = TabViewLayoutShell()
    private lateinit var workFragment: Fragment
    private lateinit var workTitle: String
    private var dosisHashCode: Int = 0
    private var nutrientType: ENutrientType = ENutrientType.Vitamin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrient_editer)  //NutrientEditerToolbar
        setSupportActionBar(NutrientEditerToolbar)

        NutrientEditerToolbar.title = shareSet.currentNutrient?.name
        NutrientEditerToolbar.setNavigationOnClickListener { onBackPressed() }

        shareSet.editorNutrient.let {
            it.setOnUpdateItemListener(object : UpdateItemListener<BNutrient>{
                override fun onUpdateItem(item: BNutrient) {
                    item.update()
                }
            })
            it.edit(shareSet.currentNutrient!!)
        }

        when (shareSet.currentNutrient?.nutrientID){
            5 -> {
                workFragment = NutrientSublistFragment()
                workTitle = "人体所需维生素列表"
                nutrientType = ENutrientType.Vitamin
            }
            6 -> {
                workFragment = NutrientSublistFragment()
                workTitle = "人体所需微量元素列表"
                nutrientType = ENutrientType.Mineral
            }
            else -> {
                workFragment = NutrientDosisFragment()
                workTitle = "日常需求量"
                nutrientType = ENutrientType.Nutrient
                dosisHashCode = getProposedDosagesHashCode(shareSet.editorNutrient.currentItem!!)
            }
        }

        viewPagerShell.parent(this)
                .tab(NutrientPropertyTabs)
                .viewPage(NutrientEditerViewPage)
                .addFragment(workFragment, workTitle)
                .addFragment(NutrientContextFragment(), "描述")
                .link()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            val hashInt = getProposedDosagesHashCode(shareSet.editorNutrient.currentItem!!)
            if (dosisHashCode == hashInt) {
                //toast("没有修改数据")
            } else {
                toast("数据已经被修改，模拟保存")
                dosisHashCode = hashInt
            }
        },800)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nutrientediter_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.addDosisItem).isVisible = shareSet.currentNutrient?.nutrientID !in 5..6
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
//                        val rang: NumberRange = NumberRange()
//                        rang.put(" 0.6 -1.3 -3")
//                        toast("[${rang.start}] ~ [${rang.end}]")
                        viewPagerShell.selectTab(1)
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
        shareSet.editorNutrient.commit()
    }

    private fun getProposedDosagesHashCode(nutrient: BNutrient): Int{
        var hashInt = 0
        nutrient.proposedDosages.forEach {
            hashInt += it.toString().hashCode()
        }
        return hashInt
    }
}
