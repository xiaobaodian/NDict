package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.BDM
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

        return inflater!!.inflate(R.layout.fragment_food_vitamin, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        if (initFieldsFlag) {
            initFieldsFlag = false
            ImportFields(shareSet.CurrentFood!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //setFields(shareSet.CurrentFood?.Vitamin!!)
    }

    override fun BlockChangeState(parent: FoodEditerActivity) {
        val changeNumber = foodEditTextHelper.ChangeNumber()
        if (changeNumber > 0) {
            parent.addChangeBlock(ChangeBlock.Vitamin)
        }
    }

    override fun ImportFields(food: BFood) {
        getFields(food.Vitamin!!)
    }

    override fun ExportFields(food: BFood) {
        setFields(food.Vitamin!!)
    }

    private fun getFields(vit: BFoodVitamin){

        with (foodEditTextHelper) {
            textBoxs.clear()
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

    private fun setFields(vit: BFoodVitamin){

        foodEditTextHelper.CheckNull("0.0")
        foodEditTextHelper.textBoxs.forEach {
            when (it.editBox){

                VitaminAIEditText -> vit.VitaminA = it.editBox.text.toString().toFloat()
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

}// Required empty public constructor
