package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.datatype.BmobPointer
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodVitamin
import com.threecats.ndictdataset.Enum.ChangeBlock
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_vitamin.*
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */

class FoodVitaminFragment : FoodPropertyFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_food_vitamin, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assignFields( currentFood.Vitamin!! )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        assemblyFields(currentFood.Vitamin!!)
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = checkTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.Vitamin)
        }
    }

    private fun assignFields(vit: BFoodVitamin){

        with (checkTextHelper) {
            addEditBox(VitaminAIEditText, vit.VitaminA.toString())
            addEditBox(CaroteneIEditText, vit.Carotene.toString())
            addEditBox(VitaminB1IEditText, vit.VitaminB1.toString())
            addEditBox(VitaminB2IEditText, vit.VitaminB2.toString())
            addEditBox(NiacinIEditText, vit.Niacin.toString())
            addEditBox(VitaminB6IEditText, vit.VitaminB6.toString())
            addEditBox(PantothenicAcidIEditText, vit.PantothenicAcid.toString())
            addEditBox(VitaminHIEditText, vit.VitaminH.toString())
            addEditBox(FolicAcidIEditText, vit.FolicAcid.toString())
            addEditBox(VitaminB12IEditText, vit.VitaminB12.toString())
            addEditBox(CholineIEditText, vit.Choline.toString())
            addEditBox(VitaminCIEditText, vit.VitaminC.toString())
            addEditBox(VitaminDIEditText, vit.VitaminD.toString())
            addEditBox(VitaminEIEditText, vit.VitaminE.toString())
            addEditBox(VitaminKIEditText, vit.VitaminK.toString())
            addEditBox(VitaminPIEditText, vit.VitaminP.toString())
            addEditBox(InositolIEditText, vit.Inositol.toString())
            addEditBox(PABAIEditText, vit.PABA.toString())
            initHash()
        }

    }

    private fun assemblyFields(vit: BFoodVitamin){

        checkTextHelper.textBoxs.forEach {
            when (it.editBox){

                VitaminAIEditText -> vit.VitaminA = it.editBox.text.toString().toFloat()
                //VitaminAIEditText -> vit.VitaminA = 471f
                CaroteneIEditText -> vit.Carotene = it.editBox.text.toString().toFloat()
                VitaminB1IEditText -> vit.VitaminB1 = it.editBox.text.toString().toFloat()
                VitaminB2IEditText -> vit.VitaminB2 = it.editBox.text.toString().toFloat()
                NiacinIEditText -> vit.Niacin = it.editBox.text.toString().toFloat()
                VitaminB6IEditText -> vit.VitaminB6 = it.editBox.text.toString().toFloat()
                PantothenicAcidIEditText -> vit.PantothenicAcid = it.editBox.text.toString().toFloat()
                VitaminHIEditText -> vit.VitaminH = it.editBox.text.toString().toFloat()
                FolicAcidIEditText -> vit.FolicAcid = it.editBox.text.toString().toFloat()
                VitaminB12IEditText -> vit.VitaminB12 = it.editBox.text.toString().toFloat()
                CholineIEditText -> vit.Choline = it.editBox.text.toString().toFloat()
                VitaminCIEditText -> vit.VitaminC = it.editBox.text.toString().toFloat()
                VitaminDIEditText -> vit.VitaminD = it.editBox.text.toString().toFloat()
                VitaminEIEditText -> vit.VitaminE = it.editBox.text.toString().toFloat()
                VitaminKIEditText -> vit.VitaminK = it.editBox.text.toString().toFloat()
                VitaminPIEditText -> vit.VitaminP = it.editBox.text.toString().toFloat()
                InositolIEditText -> vit.Inositol = it.editBox.text.toString().toFloat()
                PABAIEditText -> vit.PABA = it.editBox.text.toString().toFloat()

            }
        }

    }

    private fun findVitaminFromBmob(food: BFood){
        var query: BmobQuery<BFoodVitamin> = BmobQuery()
        query.addWhereEqualTo("Food", BmobPointer(food))
        query.setLimit(1)
        query.findObjects(object: FindListener<BFoodVitamin>(){
            override fun done(vitamins: MutableList<BFoodVitamin>?, e: BmobException?) {
                if (e == null) {
                    if (vitamins?.size == 0) {
                        shareSet.CurrentVitamin = BFoodVitamin()
                        currentVitamin = shareSet.CurrentVitamin
                        appendVitaminItem(currentVitamin!!)
                        assignFields(currentFood.Vitamin!!)
                    } else {
                        vitamins?.forEach { shareSet.CurrentVitamin = it }
                        currentVitamin = shareSet.CurrentVitamin
                        assignFields(currentFood.Vitamin!!)
                    }
                } else {
                    context.toast("${e.message}")
                }
            }
        })
    }

    private fun appendVitaminItem(vitaminItem: BFoodVitamin){
        vitaminItem.Food = currentFood
        vitaminItem.save(object: SaveListener<String>() {
            override fun done(objectID: String?, e: BmobException?) {
                if (e == null) {
                    context.toast("添补了维生素数据记录")
                } else {
                    context.toast("${e.message}")
                }
            }
        })
    }

}// Required empty public constructor
