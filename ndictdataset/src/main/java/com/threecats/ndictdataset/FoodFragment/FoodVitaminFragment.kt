package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Bmob.BFoodVitamin
import com.threecats.ndictdataset.Enum.EChangeBlock
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_vitamin.*
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 */

class FoodVitaminFragment : FoodPropertyFragment() {

    private var isREMode = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_food_vitamin, container, false)
    }

    override fun onResume() {
        super.onResume()
        setREOrOther(isREMode)
        if (initFieldsFlag) {
            initFieldsFlag = false
            shareSet.CurrentFood?.let { importFields(it.self) }
        }
    }

    override fun blockChangeState(parent: FoodEditerActivity) {
        val changeNumber = foodEditTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(EChangeBlock.Vitamin)
        }
    }

    override fun importFields(food: BFood) {
        getFields(food.vitamin!!)
    }

    override fun exportFields(food: BFood) {
        setFields(food.vitamin!!)
    }

    override fun firstEditTextFocus(){
        with (REIEditText){
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

    fun setREOrOther(isRE: Boolean){
        if (isRE) {
            REIEditText.hint = "视黄醇当量（毫克）"
        } else {
            val food = shareSet.CurrentFood!!.self
            if (food.foodBased == 0) {
                REIEditText.hint = "aaa维生素A（毫克）"
            } else {
                REIEditText.hint = "bbb胡萝卜（毫克）"
            }
        }
    }

    private fun getFields(vit: BFoodVitamin){

        with (foodEditTextHelper) {
            textBoxs.clear()
            addEditBox(REIEditText, vit.RE.toString())
            addEditBox(VitaminB1IEditText, vit.vitaminB1.toString())
            addEditBox(VitaminB2IEditText, vit.vitaminB2.toString())
            addEditBox(NiacinIEditText, vit.niacin.toString())
            addEditBox(VitaminB6IEditText, vit.vitaminB6.toString())
            addEditBox(PantothenicAcidIEditText, vit.pantothenicAcid.toString())
            addEditBox(VitaminHIEditText, vit.vitaminH.toString())
            addEditBox(FolicAcidIEditText, vit.folicAcid.toString())
            addEditBox(VitaminB12IEditText, vit.vitaminB12.toString())
            addEditBox(CholineIEditText, vit.choline.toString())
            addEditBox(VitaminCIEditText, vit.vitaminC.toString())
            addEditBox(VitaminDIEditText, vit.vitaminD.toString())
            addEditBox(VitaminEIEditText, vit.vitaminE.toString())
            addEditBox(VitaminKIEditText, vit.vitaminK.toString())
            addEditBox(VitaminPIEditText, vit.vitaminP.toString())
            addEditBox(InositolIEditText, vit.inositol.toString())
            addEditBox(PABAIEditText, vit.PABA.toString())
            initHash()
        }

    }

    private fun setFields(vit: BFoodVitamin){

        foodEditTextHelper.CheckNull("0.0")
        foodEditTextHelper.textBoxs.forEach {
            when (it.editBox){

                REIEditText -> vit.RE = it.editBox.text.toString().toFloat()
                VitaminB1IEditText -> vit.vitaminB1 = it.editBox.text.toString().toFloat()
                VitaminB2IEditText -> vit.vitaminB2 = it.editBox.text.toString().toFloat()
                NiacinIEditText -> vit.niacin = it.editBox.text.toString().toFloat()
                VitaminB6IEditText -> vit.vitaminB6 = it.editBox.text.toString().toFloat()
                PantothenicAcidIEditText -> vit.pantothenicAcid = it.editBox.text.toString().toFloat()
                VitaminHIEditText -> vit.vitaminH = it.editBox.text.toString().toFloat()
                FolicAcidIEditText -> vit.folicAcid = it.editBox.text.toString().toFloat()
                VitaminB12IEditText -> vit.vitaminB12 = it.editBox.text.toString().toFloat()
                CholineIEditText -> vit.choline = it.editBox.text.toString().toFloat()
                VitaminCIEditText -> vit.vitaminC = it.editBox.text.toString().toFloat()
                VitaminDIEditText -> vit.vitaminD = it.editBox.text.toString().toFloat()
                VitaminEIEditText -> vit.vitaminE = it.editBox.text.toString().toFloat()
                VitaminKIEditText -> vit.vitaminK = it.editBox.text.toString().toFloat()
                VitaminPIEditText -> vit.vitaminP = it.editBox.text.toString().toFloat()
                InositolIEditText -> vit.inositol = it.editBox.text.toString().toFloat()
                PABAIEditText -> vit.PABA = it.editBox.text.toString().toFloat()

            }
        }

    }

}// Required empty public constructor
