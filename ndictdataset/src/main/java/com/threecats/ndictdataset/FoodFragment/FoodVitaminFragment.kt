package com.threecats.ndictdataset.FoodFragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Enum.EChangeBlock
import com.threecats.ndictdataset.R
import com.threecats.ndictdataset.View.FoodEditerActivity
import kotlinx.android.synthetic.main.fragment_food_vitamin.*


/**
 * A simple [Fragment] subclass.
 */

class FoodVitaminFragment : FoodPropertyFragment() {

    private var isREMode = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_food_vitamin, container, false)
    }

    override fun onResume() {
        super.onResume()
        //setREOrOther()
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
        getFields(food)
    }

    override fun exportFields(food: BFood) {
        setFields(food)
    }

    override fun firstEditTextFocus(){
        with (REIEditText){
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

    fun switchREMode(){
        isREMode = !isREMode!!
        setREOrOther()
    }

    fun setREOrOther(){
        val food = shareSet.CurrentFood!!.self
        if (food.foodBased == 0) {
            if (isREMode) {
                REILayout.hint = "视黄醇当量（毫克）"
                val value = REIEditText.text.toString().toFloat()
                if (value > 0) {
                    REIEditText.text.clear()
                    REIEditText.text.append((value/6).toString())
                }
            } else {
                REILayout.hint = "胡萝卜（毫克）"
                val value = REIEditText.text.toString().toFloat()
                if (value > 0) {
                    REIEditText.text.clear()
                    REIEditText.text.append((value*6).toString())
                }
            }
        } else {
            if (isREMode) {
                REILayout.hint = "视黄醇当量（毫克）"
            } else {
                REILayout.hint = "维生素A（毫克）"
            }
        }
    }

    private fun getFields(food: BFood){
        with (foodEditTextHelper) {
            textBoxs.clear()
            if (!isREMode && shareSet.CurrentFood!!.self.foodBased == 0) {
                addEditBox(REIEditText, (food.vitamins[0].content*6).toString())
            } else {
                addEditBox(REIEditText, food.vitamins[0].content.toString())
            }
            addEditBox(VitaminB1IEditText, food.vitamins[1].content.toString())
            addEditBox(VitaminB2IEditText, food.vitamins[2].content.toString())
            addEditBox(NiacinIEditText, food.vitamins[3].content.toString())
            addEditBox(VitaminB6IEditText, food.vitamins[4].content.toString())
            addEditBox(PantothenicAcidIEditText, food.vitamins[5].content.toString())
            addEditBox(VitaminHIEditText, food.vitamins[6].content.toString())
            addEditBox(FolicAcidIEditText, food.vitamins[7].content.toString())
            addEditBox(VitaminB12IEditText, food.vitamins[8].content.toString())
            addEditBox(CholineIEditText, food.vitamins[9].content.toString())
            addEditBox(VitaminCIEditText, food.vitamins[10].content.toString())
            addEditBox(VitaminDIEditText, food.vitamins[11].content.toString())
            addEditBox(VitaminEIEditText, food.vitamins[12].content.toString())
            addEditBox(VitaminKIEditText, food.vitamins[13].content.toString())
            addEditBox(VitaminPIEditText, food.vitamins[14].content.toString())
            addEditBox(InositolIEditText, food.vitamins[15].content.toString())
            addEditBox(PABAIEditText, food.vitamins[16].content.toString())
            initHash()
        }
    }

    private fun setFields(food: BFood){

        foodEditTextHelper.CheckNull("0.0")
        foodEditTextHelper.textBoxs.forEach {

            when (it.editBox){

                REIEditText ->{
                    if (!isREMode && shareSet.CurrentFood!!.self.foodBased == 0) {
                        food.vitamins[0].content = it.editBox.text.toString().toFloat()/6
                    } else {
                        food.vitamins[0].content = it.editBox.text.toString().toFloat()
                    }
                }
                VitaminB1IEditText -> food.vitamins[1].content = it.editBox.text.toString().toFloat()
                VitaminB2IEditText -> food.vitamins[2].content = it.editBox.text.toString().toFloat()
                NiacinIEditText -> food.vitamins[3].content = it.editBox.text.toString().toFloat()
                VitaminB6IEditText -> food.vitamins[4].content = it.editBox.text.toString().toFloat()
                PantothenicAcidIEditText -> food.vitamins[5].content = it.editBox.text.toString().toFloat()
                VitaminHIEditText -> food.vitamins[6].content = it.editBox.text.toString().toFloat()
                FolicAcidIEditText -> food.vitamins[7].content = it.editBox.text.toString().toFloat()
                VitaminB12IEditText -> food.vitamins[8].content = it.editBox.text.toString().toFloat()
                CholineIEditText -> food.vitamins[9].content = it.editBox.text.toString().toFloat()
                VitaminCIEditText -> food.vitamins[10].content = it.editBox.text.toString().toFloat()
                VitaminDIEditText -> food.vitamins[11].content = it.editBox.text.toString().toFloat()
                VitaminEIEditText -> food.vitamins[12].content = it.editBox.text.toString().toFloat()
                VitaminKIEditText -> food.vitamins[13].content = it.editBox.text.toString().toFloat()
                VitaminPIEditText -> food.vitamins[14].content = it.editBox.text.toString().toFloat()
                InositolIEditText -> food.vitamins[15].content = it.editBox.text.toString().toFloat()
                PABAIEditText -> food.vitamins[16].content = it.editBox.text.toString().toFloat()

            }
        }

    }

}// Required empty public constructor
