package com.threecats.ndictdataset.FoodFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threecats.ndictdataset.Bmob.BFood
import com.threecats.ndictdataset.Helper.EditTextHelper

import com.threecats.ndictdataset.R
import kotlinx.android.synthetic.main.fragment_food_mineral.*

/**
 * A simple [Fragment] subclass.
 */

class FoodMineralFragment : FoodPropertyFragment() {

    private val checkTextExtHelper = EditTextHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_food_mineral, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SIEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return@setOnKeyListener  true
                }
            }
            false
        }

    }

    override fun onResume() {
        super.onResume()
        if (initFields) {
            initFields = false
            shareSet.editorFood.item?.let { itemToViewFields(it) }
        }
    }

    override fun itemToViewFields(food: BFood) {
        with (foodEditTextHelper) {
            textBoxs.clear()
            addEditBox(KIEditText, food.minerals[0].content.toString())
            addEditBox(NIEditText, food.minerals[1].content.toString())
            addEditBox(CaIEditText, food.minerals[2].content.toString())
            addEditBox(MgIEditText, food.minerals[3].content.toString())
            addEditBox(FeIEditText, food.minerals[4].content.toString())
            addEditBox(MnIEditText, food.minerals[5].content.toString())
            addEditBox(ZnIEditText, food.minerals[6].content.toString())
            addEditBox(CuIEditText, food.minerals[7].content.toString())
            addEditBox(SeIEditText, food.minerals[8].content.toString())
            addEditBox(PIEditText, food.minerals[9].content.toString())
            addEditBox(IIEditText, food.minerals[10].content.toString())
            addEditBox(MoIEditText, food.minerals[11].content.toString())
            addEditBox(CrIEditText, food.minerals[12].content.toString())
            addEditBox(CeIEditText, food.minerals[13].content.toString())
            addEditBox(CoIEditText, food.minerals[14].content.toString())
            addEditBox(SnIEditText, food.minerals[15].content.toString())
            addEditBox(NiIEditText, food.minerals[16].content.toString())
            addEditBox(VIEditText, food.minerals[17].content.toString())
            addEditBox(SiIEditText, food.minerals[18].content.toString())
            addEditBox(CiIEditText, food.minerals[19].content.toString())
            addEditBox(SIEditText, food.minerals[20].content.toString())
        }
    }

    override fun viewFieldsToItem(food: BFood) {
        foodEditTextHelper.checkNull("0.0")
        foodEditTextHelper.textBoxs.forEach {
            when (it.editBox){
                KIEditText    ->  food.minerals[0].content  = it.editBox.text.toString().toFloat()
                NIEditText    ->  food.minerals[1].content  = it.editBox.text.toString().toFloat()
                CaIEditText   ->  food.minerals[2].content = it.editBox.text.toString().toFloat()
                MgIEditText   ->  food.minerals[3].content = it.editBox.text.toString().toFloat()
                FeIEditText   ->  food.minerals[4].content = it.editBox.text.toString().toFloat()
                MnIEditText   ->  food.minerals[5].content = it.editBox.text.toString().toFloat()
                ZnIEditText   ->  food.minerals[6].content = it.editBox.text.toString().toFloat()
                CuIEditText   ->  food.minerals[7].content = it.editBox.text.toString().toFloat()
                SeIEditText   ->  food.minerals[8].content = it.editBox.text.toString().toFloat()
                PIEditText    ->  food.minerals[9].content  = it.editBox.text.toString().toFloat()
                IIEditText    ->  food.minerals[10].content  = it.editBox.text.toString().toFloat()
                MoIEditText   ->  food.minerals[11].content = it.editBox.text.toString().toFloat()
                CrIEditText   ->  food.minerals[12].content = it.editBox.text.toString().toFloat()
                CeIEditText   ->  food.minerals[13].content = it.editBox.text.toString().toFloat()
                CoIEditText   ->  food.minerals[14].content = it.editBox.text.toString().toFloat()
                SnIEditText   ->  food.minerals[15].content = it.editBox.text.toString().toFloat()
                NiIEditText   ->  food.minerals[16].content = it.editBox.text.toString().toFloat()
                VIEditText    ->  food.minerals[17].content  = it.editBox.text.toString().toFloat()
                SiIEditText   ->  food.minerals[18].content = it.editBox.text.toString().toFloat()
                CiIEditText   ->  food.minerals[19].content = it.editBox.text.toString().toFloat()
                SIEditText    ->  food.minerals[20].content = it.editBox.text.toString().toFloat()
            }
        }
    }

    override fun firstEditTextFocus(){

        with (KIEditText){
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

}// Required empty public constructor
